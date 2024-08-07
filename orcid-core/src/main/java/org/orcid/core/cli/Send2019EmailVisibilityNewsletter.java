package org.orcid.core.cli;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.LocaleUtils;
import org.orcid.core.email.trickle.producer.EmailQueueProducer;
import org.orcid.core.email.trickle.producer.EmailTrickleItem;
import org.orcid.core.manager.EncryptionManager;
import org.orcid.core.manager.TemplateManager;
import org.orcid.core.manager.impl.OrcidUrlManager;
import org.orcid.core.manager.v3.EmailMessage;
import org.orcid.core.manager.v3.RecordNameManager;
import org.orcid.jaxb.model.common.AvailableLocales;
import org.orcid.persistence.dao.EmailDao;
import org.orcid.persistence.dao.EmailFrequencyDao;
import org.orcid.persistence.dao.ProfileDao;
import org.orcid.persistence.jpa.entities.EmailEntity;
import org.orcid.persistence.jpa.entities.EmailFrequencyEntity;
import org.orcid.persistence.jpa.entities.ProfileEntity;
import org.orcid.persistence.jpa.entities.ProfileEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Send2019EmailVisibilityNewsletter {
    /**
     * Size of email address batches to be fetched from DB (not to be sent email, which uses trickle mechanism
     */
    private static final int BATCH_SIZE = 1000;
    
    private String fromAddress = "updates@comms.orcid.org";
    
    private EmailDao emailDaoReadOnly;
    
    private RecordNameManager recordNameManager;
    
    private EmailFrequencyDao emailFrequencyDaoReadOnly;
    
    private OrcidUrlManager orcidUrlManager;
    
    private EncryptionManager encryptionManager;
    
    private TemplateManager templateManager;
    
    private MessageSource messages;
    
    private EmailQueueProducer emailQueueProducer;
    
    private ProfileDao profileDao;
    
    private static final Logger LOG = LoggerFactory.getLogger(Send2019EmailVisibilityNewsletter.class);
    
    @SuppressWarnings("resource")
    private void init() {
        ApplicationContext context = new ClassPathXmlApplicationContext("orcid-core-context.xml");
        emailDaoReadOnly = (EmailDao) context.getBean("emailDaoReadOnly");
        emailFrequencyDaoReadOnly = (EmailFrequencyDao) context.getBean("emailFrequencyDaoReadOnly");
        recordNameManager = (RecordNameManager) context.getBean("recordNameManager");
        encryptionManager = (EncryptionManager) context.getBean("encryptionManager");
        orcidUrlManager = (OrcidUrlManager) context.getBean("orcidUrlManager");
        templateManager = (TemplateManager) context.getBean("templateManager");
        messages = (MessageSource) context.getBean("messageSource");
        emailQueueProducer = (EmailQueueProducer) context.getBean("emailQueueProducer");
        profileDao = (ProfileDao) context.getBean("profileDao");
    }
    
    private void send() {
        int offset = 0;
        List<EmailEntity> emails = emailDaoReadOnly.get2019VisibilityEmailRecipients(offset, BATCH_SIZE);
        LOG.info("Fetched {} emails for queuing...", emails.size());
        while (!emails.isEmpty()) {
            sendToEmails(emails);
            offset += BATCH_SIZE;
            emails = emailDaoReadOnly.get2019VisibilityEmailRecipients(offset, BATCH_SIZE);
            LOG.info("Fetched {} emails for queuing...", emails.size());
        }
    }
    
    private void sendToEmails(List<EmailEntity> emails) {
        for (EmailEntity email : emails) {
            EmailMessage emailMessage = getEmailMessage(email);
            EmailTrickleItem item = new EmailTrickleItem();
            item.setEmailMessage(emailMessage);
            item.setOrcid(email.getOrcid());
            item.setSuccessType(ProfileEventType.EMAIL_VIS_2019_SENT);
            item.setSkippedType(ProfileEventType.EMAIL_VIS_2019_SKIPPED);
            item.setFailureType(ProfileEventType.EMAIL_VIS_2019_FAILED);
            item.setMarketingMail(true);
            emailQueueProducer.queueEmail(item);
        }
        LOG.info("Queued {} emails", emails.size());
    }
    
    private EmailMessage getEmailMessage(EmailEntity email) {
        Locale locale = getUserLocaleFromProfileEntity(profileDao.find(email.getOrcid()));
        String orcid = email.getOrcid();
        String emailName = recordNameManager.deriveEmailFriendlyName(orcid);
        Map<String, Object> params = new HashMap<>();
        params.put("locale", locale);
        params.put("messages", messages);
        params.put("messageArgs", new Object[0]);
        params.put("emailName", emailName);
        params.put("orcidId", email.getOrcid()); 
        params.put("baseUri", orcidUrlManager.getBaseUrl());
        params.put("unsubscribeLink", getUnsubscribeLink(email.getOrcid()));        
        
        String subject = messages.getMessage("email.2019.vis_settings.subject", null, locale);
        String bodyText = templateManager.processTemplate("jul_2019_email_visibility_settings.ftl", params, locale);
        String bodyHtml = templateManager.processTemplate("jul_2019_email_visibility_settings_html.ftl", params, locale);
        
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setSubject(subject);
        emailMessage.setBodyText(bodyText);
        emailMessage.setBodyHtml(bodyHtml);
        emailMessage.setFrom(fromAddress);
        emailMessage.setTo(email.getEmail());
        return emailMessage;
    }

    private Locale getUserLocaleFromProfileEntity(ProfileEntity profile) {
        String locale = profile.getLocale();
        if (locale != null) {
            AvailableLocales loc = AvailableLocales.valueOf(locale);
            return LocaleUtils.toLocale(loc.value());
        }
        
        return LocaleUtils.toLocale("en");
    }
    
    protected String getUnsubscribeLink(String orcidId) {
        EmailFrequencyEntity entity = emailFrequencyDaoReadOnly.findByOrcid(orcidId);
        String uuid = entity.getId();
        String encryptedId = encryptionManager.encryptForExternalUse(uuid);
        String base64EncodedId = Base64.encodeBase64URLSafeString(encryptedId.getBytes());
        return orcidUrlManager.getBaseUrl() + "/unsubscribe/" + base64EncodedId;
    }

    public static void main(String[] args) {
        Send2019EmailVisibilityNewsletter sender = new Send2019EmailVisibilityNewsletter();
        if (args.length > 0) {
            sender.fromAddress = args[0];
        }
        sender.init();
        sender.send();
    }

}
