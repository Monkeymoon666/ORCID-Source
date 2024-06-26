package org.orcid.core.manager.v3.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.orcid.core.manager.v3.GivenPermissionToManager;
import org.orcid.core.manager.v3.NotificationManager;
import org.orcid.core.manager.v3.ProfileEntityManager;
import org.orcid.core.togglz.Features;
import org.orcid.persistence.dao.GivenPermissionToDao;
import org.orcid.persistence.jpa.entities.GivenPermissionByEntity;
import org.orcid.persistence.jpa.entities.GivenPermissionToEntity;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

public class GivenPermissionToManagerImpl implements GivenPermissionToManager {

    @Resource
    private GivenPermissionToDao givenPermissionToDao;

    @Resource
    private TransactionTemplate transactionTemplate;
    
    @Resource(name = "notificationManagerV3")
    private NotificationManager notificationManager;

    @Resource(name = "profileEntityManagerV3")
    private ProfileEntityManager profileEntityManager;
    
    @Override
    public void remove(String giverOrcid, String receiverOrcid) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                givenPermissionToDao.remove(giverOrcid, receiverOrcid);
                profileEntityManager.updateLastModifed(giverOrcid);
                profileEntityManager.updateLastModifed(receiverOrcid);
            }
        });            
    }

    @Override
    public void create(String userOrcid, String delegateOrcid) {
        GivenPermissionToEntity existing = givenPermissionToDao.findByGiverAndReceiverOrcid(userOrcid, delegateOrcid);
        if (existing == null) {
            transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    Date approvalDate = new Date();
                    // Create the delegate relationship
                    GivenPermissionToEntity permission = new GivenPermissionToEntity();
                    permission.setGiver(userOrcid);
                    permission.setReceiver(delegateOrcid);
                    permission.setApprovalDate(approvalDate);
                    givenPermissionToDao.merge(permission);

                    // Notify
                    notificationManager.sendNotificationToAddedDelegate(userOrcid, delegateOrcid);
                    notificationManager.sendNotificationToUserGrantingPermission(userOrcid, delegateOrcid);

                    // Update last modified on both records so the
                    // permission is visible to them immediately
                    profileEntityManager.updateLastModifed(delegateOrcid);
                    profileEntityManager.updateLastModifed(userOrcid);
                }
            });
        }
    }
    
    /**
     * Removes all trusted individuals from this record and this record from all others.
     * @param orcid
     */
    @Override
    public void removeAllForProfile(String orcid) {
        List<GivenPermissionToEntity> permissionsGiven = givenPermissionToDao.findByGiver(orcid);
        permissionsGiven.stream().forEach(e -> remove(e.getGiver(), e.getReceiver()));
        
        List<GivenPermissionByEntity> permissionsReceived = givenPermissionToDao.findByReceiver(orcid);
        permissionsReceived.stream().forEach(e -> remove(e.getGiver(), e.getReceiver()));
    }

}
