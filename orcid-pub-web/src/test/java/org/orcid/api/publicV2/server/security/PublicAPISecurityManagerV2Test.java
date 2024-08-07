package org.orcid.api.publicV2.server.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.orcid.jaxb.model.common_v2.Filterable;
import org.orcid.jaxb.model.common_v2.Visibility;
import org.orcid.jaxb.model.common_v2.VisibilityType;
import org.orcid.jaxb.model.record.summary_v2.ActivitiesSummary;
import org.orcid.jaxb.model.record.summary_v2.EducationSummary;
import org.orcid.jaxb.model.record.summary_v2.Educations;
import org.orcid.jaxb.model.record.summary_v2.EmploymentSummary;
import org.orcid.jaxb.model.record.summary_v2.Employments;
import org.orcid.jaxb.model.record.summary_v2.FundingGroup;
import org.orcid.jaxb.model.record.summary_v2.FundingSummary;
import org.orcid.jaxb.model.record.summary_v2.Fundings;
import org.orcid.jaxb.model.record.summary_v2.PeerReviewGroup;
import org.orcid.jaxb.model.record.summary_v2.PeerReviewSummary;
import org.orcid.jaxb.model.record.summary_v2.PeerReviews;
import org.orcid.jaxb.model.record.summary_v2.WorkGroup;
import org.orcid.jaxb.model.record.summary_v2.WorkSummary;
import org.orcid.jaxb.model.record.summary_v2.Works;
import org.orcid.jaxb.model.record_v2.ActivitiesContainer;
import org.orcid.jaxb.model.record_v2.Address;
import org.orcid.jaxb.model.record_v2.Addresses;
import org.orcid.jaxb.model.record_v2.Biography;
import org.orcid.jaxb.model.record_v2.Email;
import org.orcid.jaxb.model.record_v2.Emails;
import org.orcid.jaxb.model.record_v2.Group;
import org.orcid.jaxb.model.record_v2.GroupsContainer;
import org.orcid.jaxb.model.record_v2.Keyword;
import org.orcid.jaxb.model.record_v2.Keywords;
import org.orcid.jaxb.model.record_v2.Name;
import org.orcid.jaxb.model.record_v2.OtherName;
import org.orcid.jaxb.model.record_v2.OtherNames;
import org.orcid.jaxb.model.record_v2.Person;
import org.orcid.jaxb.model.record_v2.PersonExternalIdentifier;
import org.orcid.jaxb.model.record_v2.PersonExternalIdentifiers;
import org.orcid.jaxb.model.record_v2.PersonalDetails;
import org.orcid.jaxb.model.record_v2.Record;
import org.orcid.jaxb.model.record_v2.ResearcherUrl;
import org.orcid.jaxb.model.record_v2.ResearcherUrls;
import org.orcid.core.exception.OrcidNonPublicElementException;
import org.orcid.test.OrcidJUnit4ClassRunner;
import org.springframework.test.context.ContextConfiguration;

@RunWith(OrcidJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-orcid-t1-web-context.xml" })
public class PublicAPISecurityManagerV2Test {

    @Resource
    PublicAPISecurityManagerV2 publicAPISecurityManagerV2;

    @Test
    public void checkIsPublicFilterableTest() {
        publicAPISecurityManagerV2.checkIsPublic(getFilterableElement(Visibility.PUBLIC));

        try {
            publicAPISecurityManagerV2.checkIsPublic(getFilterableElement(Visibility.LIMITED));
            fail();
        } catch (OrcidNonPublicElementException e) {

        }

        try {
            publicAPISecurityManagerV2.checkIsPublic(getFilterableElement(Visibility.PRIVATE));
            fail();
        } catch (OrcidNonPublicElementException e) {

        }
    }

    @Test
    public void checkIsPublicVisibilityTypeTest() {
        publicAPISecurityManagerV2.checkIsPublic(getVisibilityTypeElement(Visibility.PUBLIC));

        try {
            publicAPISecurityManagerV2.checkIsPublic(getVisibilityTypeElement(Visibility.LIMITED));
            fail();
        } catch (OrcidNonPublicElementException e) {

        }

        try {
            publicAPISecurityManagerV2.checkIsPublic(getVisibilityTypeElement(Visibility.PRIVATE));
            fail();
        } catch (OrcidNonPublicElementException e) {

        }
    }

    @Test
    public void checkIsPublicBiographyTest() {
        Biography b = new Biography();
        b.setVisibility(Visibility.PUBLIC);
        b.setContent("Bio test");
        publicAPISecurityManagerV2.checkIsPublic(b);

        try {
            b.setVisibility(Visibility.LIMITED);
            publicAPISecurityManagerV2.checkIsPublic(b);
            fail();
        } catch (OrcidNonPublicElementException e) {

        }

        try {
            b.setVisibility(Visibility.PRIVATE);
            publicAPISecurityManagerV2.checkIsPublic(b);
            fail();
        } catch (OrcidNonPublicElementException e) {

        }
    }

    @Test
    public void checkIsPublicNameTest() {
        Name n = new Name();
        n.setVisibility(Visibility.PUBLIC);
        publicAPISecurityManagerV2.checkIsPublic(n);

        try {
            n.setVisibility(Visibility.LIMITED);
            publicAPISecurityManagerV2.checkIsPublic(n);
            fail();
        } catch (OrcidNonPublicElementException e) {

        }

        try {
            n.setVisibility(Visibility.PRIVATE);
            publicAPISecurityManagerV2.checkIsPublic(n);
            fail();
        } catch (OrcidNonPublicElementException e) {
        }
    }

    @Test
    public void filterAddressesTest() {
        Addresses x = getAddressesElement(Visibility.PUBLIC, Visibility.PUBLIC, Visibility.PUBLIC);
        assertEquals(3, x.getAddress().size());
        publicAPISecurityManagerV2.filter(x);
        assertEquals(3, x.getAddress().size());
        assertAllArePublic(x.getAddress());

        x = getAddressesElement(Visibility.PUBLIC, Visibility.PUBLIC, Visibility.LIMITED);
        assertEquals(3, x.getAddress().size());
        publicAPISecurityManagerV2.filter(x);
        assertEquals(2, x.getAddress().size());
        assertAllArePublic(x.getAddress());

        x = getAddressesElement(Visibility.PUBLIC, Visibility.LIMITED, Visibility.PRIVATE);
        assertEquals(3, x.getAddress().size());
        publicAPISecurityManagerV2.filter(x);
        assertEquals(1, x.getAddress().size());
        assertAllArePublic(x.getAddress());

        x = getAddressesElement(Visibility.PRIVATE, Visibility.LIMITED, Visibility.PRIVATE);
        assertEquals(3, x.getAddress().size());
        publicAPISecurityManagerV2.filter(x);
        assertTrue(x.getAddress().isEmpty());
    }

    @Test
    public void filterEmailsTest() {
        Emails x = getEmailsElement(Visibility.PUBLIC, Visibility.PUBLIC, Visibility.PUBLIC);
        assertEquals(3, x.getEmails().size());
        publicAPISecurityManagerV2.filter(x);
        assertEquals(3, x.getEmails().size());
        assertAllArePublic(x.getEmails());

        x = getEmailsElement(Visibility.PUBLIC, Visibility.PUBLIC, Visibility.LIMITED);
        assertEquals(3, x.getEmails().size());
        publicAPISecurityManagerV2.filter(x);
        assertEquals(2, x.getEmails().size());
        assertAllArePublic(x.getEmails());

        x = getEmailsElement(Visibility.PUBLIC, Visibility.LIMITED, Visibility.PRIVATE);
        assertEquals(3, x.getEmails().size());
        publicAPISecurityManagerV2.filter(x);
        assertEquals(1, x.getEmails().size());
        assertAllArePublic(x.getEmails());

        x = getEmailsElement(Visibility.PRIVATE, Visibility.LIMITED, Visibility.PRIVATE);
        assertEquals(3, x.getEmails().size());
        publicAPISecurityManagerV2.filter(x);
        assertTrue(x.getEmails().isEmpty());

    }

    @Test
    public void filterExternalIdentifiersTest() {
        PersonExternalIdentifiers x = getPersonExternalIdentifiersElement(Visibility.PUBLIC, Visibility.PUBLIC, Visibility.PUBLIC);
        assertEquals(3, x.getExternalIdentifiers().size());
        publicAPISecurityManagerV2.filter(x);
        assertEquals(3, x.getExternalIdentifiers().size());
        assertAllArePublic(x.getExternalIdentifiers());

        x = getPersonExternalIdentifiersElement(Visibility.PUBLIC, Visibility.PUBLIC, Visibility.LIMITED);
        assertEquals(3, x.getExternalIdentifiers().size());
        publicAPISecurityManagerV2.filter(x);
        assertEquals(2, x.getExternalIdentifiers().size());
        assertAllArePublic(x.getExternalIdentifiers());

        x = getPersonExternalIdentifiersElement(Visibility.PUBLIC, Visibility.LIMITED, Visibility.PRIVATE);
        assertEquals(3, x.getExternalIdentifiers().size());
        publicAPISecurityManagerV2.filter(x);
        assertEquals(1, x.getExternalIdentifiers().size());
        assertAllArePublic(x.getExternalIdentifiers());

        x = getPersonExternalIdentifiersElement(Visibility.PRIVATE, Visibility.LIMITED, Visibility.PRIVATE);
        assertEquals(3, x.getExternalIdentifiers().size());
        publicAPISecurityManagerV2.filter(x);
        assertTrue(x.getExternalIdentifiers().isEmpty());
    }

    @Test
    public void filterKeywordsTest() {
        Keywords x = getKeywordsElement(Visibility.PUBLIC, Visibility.PUBLIC, Visibility.PUBLIC);
        assertEquals(3, x.getKeywords().size());
        publicAPISecurityManagerV2.filter(x);
        assertEquals(3, x.getKeywords().size());
        assertAllArePublic(x.getKeywords());

        x = getKeywordsElement(Visibility.PUBLIC, Visibility.PUBLIC, Visibility.LIMITED);
        assertEquals(3, x.getKeywords().size());
        publicAPISecurityManagerV2.filter(x);
        assertEquals(2, x.getKeywords().size());
        assertAllArePublic(x.getKeywords());

        x = getKeywordsElement(Visibility.PUBLIC, Visibility.LIMITED, Visibility.PRIVATE);
        assertEquals(3, x.getKeywords().size());
        publicAPISecurityManagerV2.filter(x);
        assertEquals(1, x.getKeywords().size());
        assertAllArePublic(x.getKeywords());

        x = getKeywordsElement(Visibility.PRIVATE, Visibility.LIMITED, Visibility.PRIVATE);
        assertEquals(3, x.getKeywords().size());
        publicAPISecurityManagerV2.filter(x);
        assertTrue(x.getKeywords().isEmpty());
    }

    @Test
    public void filterOtherNamesTest() {
        OtherNames x = getOtherNamesElement(Visibility.PUBLIC, Visibility.PUBLIC, Visibility.PUBLIC);
        assertEquals(3, x.getOtherNames().size());
        publicAPISecurityManagerV2.filter(x);
        assertEquals(3, x.getOtherNames().size());
        assertAllArePublic(x.getOtherNames());

        x = getOtherNamesElement(Visibility.PUBLIC, Visibility.PUBLIC, Visibility.LIMITED);
        assertEquals(3, x.getOtherNames().size());
        publicAPISecurityManagerV2.filter(x);
        assertEquals(2, x.getOtherNames().size());
        assertAllArePublic(x.getOtherNames());

        x = getOtherNamesElement(Visibility.PUBLIC, Visibility.LIMITED, Visibility.PRIVATE);
        assertEquals(3, x.getOtherNames().size());
        publicAPISecurityManagerV2.filter(x);
        assertEquals(1, x.getOtherNames().size());
        assertAllArePublic(x.getOtherNames());

        x = getOtherNamesElement(Visibility.PRIVATE, Visibility.LIMITED, Visibility.PRIVATE);
        assertEquals(3, x.getOtherNames().size());
        publicAPISecurityManagerV2.filter(x);
        assertTrue(x.getOtherNames().isEmpty());
    }

    @Test
    public void filterResearcherUrlsTest() {
        ResearcherUrls x = getResearcherUrlsElement(Visibility.PUBLIC, Visibility.PUBLIC, Visibility.PUBLIC);
        assertEquals(3, x.getResearcherUrls().size());
        publicAPISecurityManagerV2.filter(x);
        assertEquals(3, x.getResearcherUrls().size());
        assertAllArePublic(x.getResearcherUrls());

        x = getResearcherUrlsElement(Visibility.PUBLIC, Visibility.PUBLIC, Visibility.LIMITED);
        assertEquals(3, x.getResearcherUrls().size());
        publicAPISecurityManagerV2.filter(x);
        assertEquals(2, x.getResearcherUrls().size());
        assertAllArePublic(x.getResearcherUrls());

        x = getResearcherUrlsElement(Visibility.PUBLIC, Visibility.LIMITED, Visibility.PRIVATE);
        assertEquals(3, x.getResearcherUrls().size());
        publicAPISecurityManagerV2.filter(x);
        assertEquals(1, x.getResearcherUrls().size());
        assertAllArePublic(x.getResearcherUrls());

        x = getResearcherUrlsElement(Visibility.PRIVATE, Visibility.LIMITED, Visibility.PRIVATE);
        assertEquals(3, x.getResearcherUrls().size());
        publicAPISecurityManagerV2.filter(x);
        assertTrue(x.getResearcherUrls().isEmpty());
    }

    @Test
    public void filterEmploymentsTest() {
        Employments e = getEmployments(Visibility.PUBLIC, Visibility.PUBLIC, Visibility.PUBLIC);
        assertEquals(3, e.getSummaries().size());
        publicAPISecurityManagerV2.filter(e);
        assertEquals(3, e.getSummaries().size());
        assertContainerContainsOnlyPublicElements(e);

        e = getEmployments(Visibility.PUBLIC, Visibility.PUBLIC, Visibility.LIMITED);
        assertEquals(3, e.getSummaries().size());
        publicAPISecurityManagerV2.filter(e);
        assertEquals(2, e.getSummaries().size());
        assertContainerContainsOnlyPublicElements(e);

        e = getEmployments(Visibility.PUBLIC, Visibility.LIMITED, Visibility.PRIVATE);
        assertEquals(3, e.getSummaries().size());
        publicAPISecurityManagerV2.filter(e);
        assertEquals(1, e.getSummaries().size());
        assertContainerContainsOnlyPublicElements(e);

        e = getEmployments(Visibility.PRIVATE, Visibility.LIMITED, Visibility.PRIVATE);
        assertEquals(3, e.getSummaries().size());
        publicAPISecurityManagerV2.filter(e);
        assertTrue(e.getSummaries().isEmpty());
    }

    @Test
    public void filterEducationsTest() {
        Educations e = getEducations(Visibility.PUBLIC, Visibility.PUBLIC, Visibility.PUBLIC);
        assertEquals(3, e.getSummaries().size());
        publicAPISecurityManagerV2.filter(e);
        assertEquals(3, e.getSummaries().size());
        assertContainerContainsOnlyPublicElements(e);

        e = getEducations(Visibility.PUBLIC, Visibility.PUBLIC, Visibility.LIMITED);
        assertEquals(3, e.getSummaries().size());
        publicAPISecurityManagerV2.filter(e);
        assertEquals(2, e.getSummaries().size());
        assertContainerContainsOnlyPublicElements(e);

        e = getEducations(Visibility.PUBLIC, Visibility.LIMITED, Visibility.PRIVATE);
        assertEquals(3, e.getSummaries().size());
        publicAPISecurityManagerV2.filter(e);
        assertEquals(1, e.getSummaries().size());
        assertContainerContainsOnlyPublicElements(e);

        e = getEducations(Visibility.LIMITED, Visibility.LIMITED, Visibility.PRIVATE);
        assertEquals(3, e.getSummaries().size());
        publicAPISecurityManagerV2.filter(e);
        assertTrue(e.getSummaries().isEmpty());
    }

    @Test
    public void filterWorksTest() {
        Works w = getWorks(Visibility.PUBLIC, Visibility.PUBLIC, Visibility.PUBLIC);
        assertEquals(3, w.getWorkGroup().size());
        publicAPISecurityManagerV2.filter(w);
        assertEquals(3, w.getWorkGroup().size());
        assertGroupContainsOnlyPublicElements(w);

        w = getWorks(Visibility.PUBLIC, Visibility.PUBLIC, Visibility.LIMITED);
        assertEquals(3, w.getWorkGroup().size());
        publicAPISecurityManagerV2.filter(w);
        assertEquals(2, w.getWorkGroup().size());
        assertGroupContainsOnlyPublicElements(w);

        w = getWorks(Visibility.PUBLIC, Visibility.LIMITED, Visibility.PRIVATE);
        assertEquals(3, w.getWorkGroup().size());
        publicAPISecurityManagerV2.filter(w);
        assertEquals(1, w.getWorkGroup().size());
        assertGroupContainsOnlyPublicElements(w);

        w = getWorks(Visibility.PRIVATE, Visibility.LIMITED, Visibility.PRIVATE);
        assertEquals(3, w.getWorkGroup().size());
        publicAPISecurityManagerV2.filter(w);
        assertTrue(w.getWorkGroup().isEmpty());

    }

    @Test
    public void filterFundingsTest() {
        Fundings f = getFundings(Visibility.PUBLIC, Visibility.PUBLIC, Visibility.PUBLIC);
        assertEquals(3, f.getFundingGroup().size());
        publicAPISecurityManagerV2.filter(f);
        assertEquals(3, f.getFundingGroup().size());
        assertGroupContainsOnlyPublicElements(f);

        f = getFundings(Visibility.PUBLIC, Visibility.PUBLIC, Visibility.LIMITED);
        assertEquals(3, f.getFundingGroup().size());
        publicAPISecurityManagerV2.filter(f);
        assertEquals(2, f.getFundingGroup().size());
        assertGroupContainsOnlyPublicElements(f);

        f = getFundings(Visibility.PUBLIC, Visibility.LIMITED, Visibility.PRIVATE);
        assertEquals(3, f.getFundingGroup().size());
        publicAPISecurityManagerV2.filter(f);
        assertEquals(1, f.getFundingGroup().size());
        assertGroupContainsOnlyPublicElements(f);

        f = getFundings(Visibility.PRIVATE, Visibility.LIMITED, Visibility.PRIVATE);
        assertEquals(3, f.getFundingGroup().size());
        publicAPISecurityManagerV2.filter(f);
        assertTrue(f.getFundingGroup().isEmpty());
    }

    @Test
    public void filterPeerReviewsTest() {
        PeerReviews p = getPeerReviews(Visibility.PUBLIC, Visibility.PUBLIC, Visibility.PUBLIC);
        assertEquals(3, p.getPeerReviewGroup().size());
        publicAPISecurityManagerV2.filter(p);
        assertEquals(3, p.getPeerReviewGroup().size());
        assertGroupContainsOnlyPublicElements(p);

        p = getPeerReviews(Visibility.PUBLIC, Visibility.PUBLIC, Visibility.LIMITED);
        assertEquals(3, p.getPeerReviewGroup().size());
        publicAPISecurityManagerV2.filter(p);
        assertEquals(2, p.getPeerReviewGroup().size());
        assertGroupContainsOnlyPublicElements(p);

        p = getPeerReviews(Visibility.PUBLIC, Visibility.LIMITED, Visibility.PRIVATE);
        assertEquals(3, p.getPeerReviewGroup().size());
        publicAPISecurityManagerV2.filter(p);
        assertEquals(1, p.getPeerReviewGroup().size());
        assertGroupContainsOnlyPublicElements(p);

        p = getPeerReviews(Visibility.PRIVATE, Visibility.LIMITED, Visibility.PRIVATE);
        assertEquals(3, p.getPeerReviewGroup().size());
        publicAPISecurityManagerV2.filter(p);
        assertTrue(p.getPeerReviewGroup().isEmpty());
    }

    @Test
    public void checkIsPublicActivitiesSummaryTest() {
        ActivitiesSummary as = getActivitiesSummaryElement();
        publicAPISecurityManagerV2.filter(as);
        // Assert it have all activities
        assertEquals(3, as.getEducations().getSummaries().size());
        assertContainerContainsOnlyPublicElements(as.getEducations());
        assertEquals(3, as.getEmployments().getSummaries().size());
        assertContainerContainsOnlyPublicElements(as.getEmployments());
        assertEquals(3, as.getFundings().getFundingGroup().size());
        assertGroupContainsOnlyPublicElements(as.getFundings());
        assertEquals(3, as.getPeerReviews().getPeerReviewGroup().size());
        assertGroupContainsOnlyPublicElements(as.getPeerReviews());
        assertEquals(3, as.getWorks().getWorkGroup().size());
        assertGroupContainsOnlyPublicElements(as.getWorks());

        // Assert it filters educations
        as = getActivitiesSummaryElement();
        setVisibility(as.getEducations().getSummaries(), Visibility.LIMITED, Visibility.PRIVATE, Visibility.LIMITED);
        publicAPISecurityManagerV2.filter(as);

        assertNotNull(as.getEducations());
        assertTrue(as.getEducations().getSummaries().isEmpty());
        assertEquals(3, as.getEmployments().getSummaries().size());
        assertContainerContainsOnlyPublicElements(as.getEmployments());
        assertEquals(3, as.getFundings().getFundingGroup().size());
        assertGroupContainsOnlyPublicElements(as.getFundings());
        assertEquals(3, as.getPeerReviews().getPeerReviewGroup().size());
        assertGroupContainsOnlyPublicElements(as.getPeerReviews());
        assertEquals(3, as.getWorks().getWorkGroup().size());
        assertGroupContainsOnlyPublicElements(as.getWorks());

        // Assert it filters employments
        as = getActivitiesSummaryElement();
        setVisibility(as.getEmployments().getSummaries(), Visibility.LIMITED, Visibility.PRIVATE, Visibility.LIMITED);
        publicAPISecurityManagerV2.filter(as);

        assertEquals(3, as.getEducations().getSummaries().size());
        assertContainerContainsOnlyPublicElements(as.getEducations());
        assertNotNull(as.getEmployments());
        assertTrue(as.getEmployments().getSummaries().isEmpty());
        assertEquals(3, as.getFundings().getFundingGroup().size());
        assertGroupContainsOnlyPublicElements(as.getFundings());
        assertEquals(3, as.getPeerReviews().getPeerReviewGroup().size());
        assertGroupContainsOnlyPublicElements(as.getPeerReviews());
        assertEquals(3, as.getWorks().getWorkGroup().size());
        assertGroupContainsOnlyPublicElements(as.getWorks());

        // Assert it filters funding
        as = getActivitiesSummaryElement();
        setVisibility(as.getFundings(), Visibility.LIMITED, Visibility.PRIVATE, Visibility.LIMITED);
        publicAPISecurityManagerV2.filter(as);

        assertEquals(3, as.getEducations().getSummaries().size());
        assertContainerContainsOnlyPublicElements(as.getEducations());
        assertEquals(3, as.getEmployments().getSummaries().size());
        assertContainerContainsOnlyPublicElements(as.getEmployments());
        assertTrue(as.getFundings().getFundingGroup().isEmpty());
        assertEquals(3, as.getPeerReviews().getPeerReviewGroup().size());
        assertGroupContainsOnlyPublicElements(as.getPeerReviews());
        assertEquals(3, as.getWorks().getWorkGroup().size());
        assertGroupContainsOnlyPublicElements(as.getWorks());

        // Assert it filters peer reviews
        as = getActivitiesSummaryElement();
        setVisibility(as.getPeerReviews(), Visibility.LIMITED, Visibility.PRIVATE, Visibility.LIMITED);
        publicAPISecurityManagerV2.filter(as);

        assertEquals(3, as.getEducations().getSummaries().size());
        assertContainerContainsOnlyPublicElements(as.getEducations());
        assertEquals(3, as.getEmployments().getSummaries().size());
        assertContainerContainsOnlyPublicElements(as.getEmployments());
        assertEquals(3, as.getFundings().getFundingGroup().size());
        assertGroupContainsOnlyPublicElements(as.getFundings());
        assertTrue(as.getPeerReviews().getPeerReviewGroup().isEmpty());
        assertEquals(3, as.getWorks().getWorkGroup().size());
        assertGroupContainsOnlyPublicElements(as.getWorks());

        // Assert it filters works
        as = getActivitiesSummaryElement();
        setVisibility(as.getWorks(), Visibility.LIMITED, Visibility.PRIVATE, Visibility.LIMITED);
        publicAPISecurityManagerV2.filter(as);

        assertEquals(3, as.getEducations().getSummaries().size());
        assertContainerContainsOnlyPublicElements(as.getEducations());
        assertEquals(3, as.getEmployments().getSummaries().size());
        assertContainerContainsOnlyPublicElements(as.getEmployments());
        assertEquals(3, as.getFundings().getFundingGroup().size());
        assertGroupContainsOnlyPublicElements(as.getFundings());
        assertEquals(3, as.getPeerReviews().getPeerReviewGroup().size());
        assertGroupContainsOnlyPublicElements(as.getPeerReviews());
        assertTrue(as.getWorks().getWorkGroup().isEmpty());
    }

    @Test
    public void checkIsPublicPersonalDetailsTest() {
        PersonalDetails p = getPersonalDetailsElement(Visibility.PUBLIC, Visibility.PUBLIC, Visibility.PUBLIC);
        publicAPISecurityManagerV2.filter(p);
        assertEquals(Visibility.PUBLIC, p.getName().getVisibility());
        assertEquals(Visibility.PUBLIC, p.getBiography().getVisibility());
        assertNotNull(p.getOtherNames().getOtherNames());
        p.getOtherNames().getOtherNames().forEach(e -> {
            assertIsPublic(e);
        });

        // Should not fail, but name should be empty
        p = getPersonalDetailsElement(Visibility.LIMITED, Visibility.PUBLIC, Visibility.PUBLIC);
        publicAPISecurityManagerV2.filter(p);
        assertNull(p.getName());
        assertNotNull(p.getBiography());
        assertNotNull(p.getOtherNames().getOtherNames());
        p.getOtherNames().getOtherNames().forEach(e -> {
            assertIsPublic(e);
        });

        // Should not fail, but bio should be null
        p = getPersonalDetailsElement(Visibility.PUBLIC, Visibility.LIMITED, Visibility.PUBLIC);
        publicAPISecurityManagerV2.filter(p);
        assertNotNull(p.getName());
        assertNull(p.getBiography());
        assertNotNull(p.getOtherNames().getOtherNames());
        p.getOtherNames().getOtherNames().forEach(e -> {
            assertIsPublic(e);
        });

        p = getPersonalDetailsElement(Visibility.PUBLIC, Visibility.PUBLIC, Visibility.LIMITED);
        publicAPISecurityManagerV2.filter(p);
        assertNotNull(p.getName());
        assertNotNull(p.getBiography());
        assertNotNull(p.getOtherNames());
        assertTrue(p.getOtherNames().getOtherNames().isEmpty());
        
        p = getPersonalDetailsElement(Visibility.PUBLIC, Visibility.PUBLIC, Visibility.PRIVATE);
        publicAPISecurityManagerV2.filter(p);
        assertNotNull(p.getName());
        assertNotNull(p.getBiography());
        assertNotNull(p.getOtherNames());
        assertTrue(p.getOtherNames().getOtherNames().isEmpty());
    }

    @Test
    public void checkIsPublicPersonTest() {
        Person p = getPersonElement();
        publicAPISecurityManagerV2.filter(p);

        // Nothing is filtered yet
        assertEquals(3, p.getAddresses().getAddress().size());
        p.getAddresses().getAddress().forEach(e -> assertIsPublic(e));
        assertEquals(Visibility.PUBLIC, p.getBiography().getVisibility());
        assertEquals(3, p.getEmails().getEmails().size());
        p.getEmails().getEmails().forEach(e -> assertIsPublic(e));
        assertEquals(3, p.getExternalIdentifiers().getExternalIdentifiers().size());
        p.getExternalIdentifiers().getExternalIdentifiers().forEach(e -> assertIsPublic(e));
        assertEquals(3, p.getKeywords().getKeywords().size());
        p.getKeywords().getKeywords().forEach(e -> assertIsPublic(e));
        assertEquals(Visibility.PUBLIC, p.getName().getVisibility());
        assertEquals(3, p.getOtherNames().getOtherNames().size());
        p.getOtherNames().getOtherNames().forEach(e -> assertIsPublic(e));
        assertEquals(3, p.getResearcherUrls().getResearcherUrls().size());
        p.getResearcherUrls().getResearcherUrls().forEach(e -> assertIsPublic(e));

        // Addresses filtered
        p = getPersonElement();
        setVisibility(p.getAddresses().getAddress(), Visibility.LIMITED, Visibility.PRIVATE, Visibility.LIMITED);

        publicAPISecurityManagerV2.filter(p);
        // --- filtered ---
        assertNotNull(p.getAddresses());
        assertTrue(p.getAddresses().getAddress().isEmpty());
        assertEquals(Visibility.PUBLIC, p.getBiography().getVisibility());
        assertEquals(3, p.getEmails().getEmails().size());
        p.getEmails().getEmails().forEach(e -> assertIsPublic(e));
        assertEquals(3, p.getExternalIdentifiers().getExternalIdentifiers().size());
        p.getExternalIdentifiers().getExternalIdentifiers().forEach(e -> assertIsPublic(e));
        assertEquals(3, p.getKeywords().getKeywords().size());
        p.getKeywords().getKeywords().forEach(e -> assertIsPublic(e));
        assertEquals(Visibility.PUBLIC, p.getName().getVisibility());
        assertEquals(3, p.getOtherNames().getOtherNames().size());
        p.getOtherNames().getOtherNames().forEach(e -> assertIsPublic(e));
        assertEquals(3, p.getResearcherUrls().getResearcherUrls().size());
        p.getResearcherUrls().getResearcherUrls().forEach(e -> assertIsPublic(e));

        // Bio filtered
        p = getPersonElement();
        p.getBiography().setVisibility(Visibility.LIMITED);

        publicAPISecurityManagerV2.filter(p);
        assertEquals(3, p.getAddresses().getAddress().size());
        p.getAddresses().getAddress().forEach(e -> assertIsPublic(e));
        // --- filtered ---
        assertNull(p.getBiography());
        assertEquals(3, p.getEmails().getEmails().size());
        p.getEmails().getEmails().forEach(e -> assertIsPublic(e));
        assertEquals(3, p.getExternalIdentifiers().getExternalIdentifiers().size());
        p.getExternalIdentifiers().getExternalIdentifiers().forEach(e -> assertIsPublic(e));
        assertEquals(3, p.getKeywords().getKeywords().size());
        p.getKeywords().getKeywords().forEach(e -> assertIsPublic(e));
        assertEquals(Visibility.PUBLIC, p.getName().getVisibility());
        assertEquals(3, p.getOtherNames().getOtherNames().size());
        p.getOtherNames().getOtherNames().forEach(e -> assertIsPublic(e));
        assertEquals(3, p.getResearcherUrls().getResearcherUrls().size());
        p.getResearcherUrls().getResearcherUrls().forEach(e -> assertIsPublic(e));

        // Emails filtered
        p = getPersonElement();
        setVisibility(p.getEmails().getEmails(), Visibility.LIMITED, Visibility.PRIVATE, Visibility.LIMITED);

        publicAPISecurityManagerV2.filter(p);
        assertEquals(3, p.getAddresses().getAddress().size());
        p.getAddresses().getAddress().forEach(e -> assertIsPublic(e));
        assertEquals(Visibility.PUBLIC, p.getBiography().getVisibility());
        // --- filtered ---
        assertNotNull(p.getEmails());
        assertTrue(p.getEmails().getEmails().isEmpty());
        assertEquals(3, p.getExternalIdentifiers().getExternalIdentifiers().size());
        p.getExternalIdentifiers().getExternalIdentifiers().forEach(e -> assertIsPublic(e));
        assertEquals(3, p.getKeywords().getKeywords().size());
        p.getKeywords().getKeywords().forEach(e -> assertIsPublic(e));
        assertEquals(Visibility.PUBLIC, p.getName().getVisibility());
        assertEquals(3, p.getOtherNames().getOtherNames().size());
        p.getOtherNames().getOtherNames().forEach(e -> assertIsPublic(e));
        assertEquals(3, p.getResearcherUrls().getResearcherUrls().size());
        p.getResearcherUrls().getResearcherUrls().forEach(e -> assertIsPublic(e));

        // External ids filtered
        p = getPersonElement();
        setVisibility(p.getExternalIdentifiers().getExternalIdentifiers(), Visibility.LIMITED, Visibility.PRIVATE, Visibility.LIMITED);

        publicAPISecurityManagerV2.filter(p);
        assertEquals(3, p.getAddresses().getAddress().size());
        p.getAddresses().getAddress().forEach(e -> assertIsPublic(e));
        assertEquals(Visibility.PUBLIC, p.getBiography().getVisibility());
        assertEquals(3, p.getEmails().getEmails().size());
        p.getEmails().getEmails().forEach(e -> assertIsPublic(e));
        // --- filtered ---
        assertNotNull(p.getExternalIdentifiers());
        assertTrue(p.getExternalIdentifiers().getExternalIdentifiers().isEmpty());
        assertEquals(3, p.getKeywords().getKeywords().size());
        p.getKeywords().getKeywords().forEach(e -> assertIsPublic(e));
        assertEquals(Visibility.PUBLIC, p.getName().getVisibility());
        assertEquals(3, p.getOtherNames().getOtherNames().size());
        p.getOtherNames().getOtherNames().forEach(e -> assertIsPublic(e));
        assertEquals(3, p.getResearcherUrls().getResearcherUrls().size());
        p.getResearcherUrls().getResearcherUrls().forEach(e -> assertIsPublic(e));

        // Keywords filtered
        p = getPersonElement();
        setVisibility(p.getKeywords().getKeywords(), Visibility.LIMITED, Visibility.PRIVATE, Visibility.LIMITED);

        publicAPISecurityManagerV2.filter(p);
        assertEquals(3, p.getAddresses().getAddress().size());
        p.getAddresses().getAddress().forEach(e -> assertIsPublic(e));
        assertEquals(Visibility.PUBLIC, p.getBiography().getVisibility());
        assertEquals(3, p.getEmails().getEmails().size());
        p.getEmails().getEmails().forEach(e -> assertIsPublic(e));
        assertEquals(3, p.getExternalIdentifiers().getExternalIdentifiers().size());
        p.getExternalIdentifiers().getExternalIdentifiers().forEach(e -> assertIsPublic(e));
        // --- filtered ---
        assertNotNull(p.getKeywords());
        assertTrue(p.getKeywords().getKeywords().isEmpty());
        assertEquals(Visibility.PUBLIC, p.getName().getVisibility());
        assertEquals(3, p.getOtherNames().getOtherNames().size());
        p.getOtherNames().getOtherNames().forEach(e -> assertIsPublic(e));
        assertEquals(3, p.getResearcherUrls().getResearcherUrls().size());
        p.getResearcherUrls().getResearcherUrls().forEach(e -> assertIsPublic(e));

        // Name filtered
        p = getPersonElement();
        p.getName().setVisibility(Visibility.LIMITED);

        publicAPISecurityManagerV2.filter(p);
        assertEquals(3, p.getAddresses().getAddress().size());
        p.getAddresses().getAddress().forEach(e -> assertIsPublic(e));
        assertEquals(Visibility.PUBLIC, p.getBiography().getVisibility());
        assertEquals(3, p.getEmails().getEmails().size());
        p.getEmails().getEmails().forEach(e -> assertIsPublic(e));
        assertEquals(3, p.getExternalIdentifiers().getExternalIdentifiers().size());
        p.getExternalIdentifiers().getExternalIdentifiers().forEach(e -> assertIsPublic(e));
        assertEquals(3, p.getKeywords().getKeywords().size());
        p.getKeywords().getKeywords().forEach(e -> assertIsPublic(e));
        // --- filtered ---
        assertNull(p.getName());
        assertEquals(3, p.getOtherNames().getOtherNames().size());
        p.getOtherNames().getOtherNames().forEach(e -> assertIsPublic(e));
        assertEquals(3, p.getResearcherUrls().getResearcherUrls().size());
        p.getResearcherUrls().getResearcherUrls().forEach(e -> assertIsPublic(e));

        // Other names filtered
        p = getPersonElement();
        setVisibility(p.getOtherNames().getOtherNames(), Visibility.LIMITED, Visibility.PRIVATE, Visibility.LIMITED);

        publicAPISecurityManagerV2.filter(p);
        assertEquals(3, p.getAddresses().getAddress().size());
        p.getAddresses().getAddress().forEach(e -> assertIsPublic(e));
        assertEquals(Visibility.PUBLIC, p.getBiography().getVisibility());
        assertEquals(3, p.getEmails().getEmails().size());
        p.getEmails().getEmails().forEach(e -> assertIsPublic(e));
        assertEquals(3, p.getExternalIdentifiers().getExternalIdentifiers().size());
        p.getExternalIdentifiers().getExternalIdentifiers().forEach(e -> assertIsPublic(e));
        assertEquals(3, p.getKeywords().getKeywords().size());
        p.getKeywords().getKeywords().forEach(e -> assertIsPublic(e));
        assertEquals(Visibility.PUBLIC, p.getName().getVisibility());
        // --- filtered ---
        assertNotNull(p.getOtherNames());
        assertTrue(p.getOtherNames().getOtherNames().isEmpty());
        assertEquals(3, p.getResearcherUrls().getResearcherUrls().size());
        p.getResearcherUrls().getResearcherUrls().forEach(e -> assertIsPublic(e));

        // Researcher urls filtered
        p = getPersonElement();
        setVisibility(p.getResearcherUrls().getResearcherUrls(), Visibility.LIMITED, Visibility.PRIVATE, Visibility.LIMITED);

        publicAPISecurityManagerV2.filter(p);
        assertEquals(3, p.getAddresses().getAddress().size());
        p.getAddresses().getAddress().forEach(e -> assertIsPublic(e));
        assertEquals(Visibility.PUBLIC, p.getBiography().getVisibility());
        assertEquals(3, p.getEmails().getEmails().size());
        p.getEmails().getEmails().forEach(e -> assertIsPublic(e));
        assertEquals(3, p.getExternalIdentifiers().getExternalIdentifiers().size());
        p.getExternalIdentifiers().getExternalIdentifiers().forEach(e -> assertIsPublic(e));
        assertEquals(3, p.getKeywords().getKeywords().size());
        p.getKeywords().getKeywords().forEach(e -> assertIsPublic(e));
        assertEquals(Visibility.PUBLIC, p.getName().getVisibility());
        assertEquals(3, p.getOtherNames().getOtherNames().size());
        p.getOtherNames().getOtherNames().forEach(e -> assertIsPublic(e));
        // --- filtered ---
        assertNotNull(p.getResearcherUrls());
        assertTrue(p.getResearcherUrls().getResearcherUrls().isEmpty());
    }

    @Test
    public void checkIsPublicRecordTest() {
        Record r = getRecordElement();
        publicAPISecurityManagerV2.filter(r);

        // Verify activities - nothing filtered
        ActivitiesSummary as = r.getActivitiesSummary();
        assertEquals(3, as.getEducations().getSummaries().size());
        assertContainerContainsOnlyPublicElements(as.getEducations());
        assertEquals(3, as.getEmployments().getSummaries().size());
        assertContainerContainsOnlyPublicElements(as.getEmployments());
        assertEquals(3, as.getFundings().getFundingGroup().size());
        assertGroupContainsOnlyPublicElements(as.getFundings());
        assertEquals(3, as.getPeerReviews().getPeerReviewGroup().size());
        assertGroupContainsOnlyPublicElements(as.getPeerReviews());
        assertEquals(3, as.getWorks().getWorkGroup().size());
        assertGroupContainsOnlyPublicElements(as.getWorks());

        // Verify bio sections - nothing filtered
        Person p = r.getPerson();
        assertEquals(3, p.getAddresses().getAddress().size());
        assertAllArePublic(p.getAddresses().getAddress());
        assertEquals(3, p.getEmails().getEmails().size());
        assertAllArePublic(p.getEmails().getEmails());
        assertEquals(3, p.getExternalIdentifiers().getExternalIdentifiers().size());
        assertAllArePublic(p.getExternalIdentifiers().getExternalIdentifiers());
        assertEquals(3, p.getKeywords().getKeywords().size());
        assertAllArePublic(p.getKeywords().getKeywords());
        assertEquals(3, p.getOtherNames().getOtherNames().size());
        assertAllArePublic(p.getOtherNames().getOtherNames());
        assertEquals(3, p.getResearcherUrls().getResearcherUrls().size());
        assertAllArePublic(p.getResearcherUrls().getResearcherUrls());
        assertNotNull(p.getBiography());
        assertNotNull(p.getName());

        // Filter biography, name, educations and funding
        r = getRecordElement();
        r.getPerson().getBiography().setVisibility(Visibility.LIMITED);
        r.getPerson().getName().setVisibility(Visibility.LIMITED);
        setVisibility(r.getActivitiesSummary().getEducations().getSummaries(), Visibility.LIMITED, Visibility.PRIVATE, Visibility.LIMITED);
        setVisibility(r.getActivitiesSummary().getFundings(), Visibility.LIMITED, Visibility.PRIVATE, Visibility.LIMITED);

        publicAPISecurityManagerV2.filter(r);

        // Verify activities - educations and funding filtered
        as = r.getActivitiesSummary();
        assertNotNull(as.getEducations());
        assertTrue(as.getEducations().getSummaries().isEmpty());
        assertEquals(3, as.getEmployments().getSummaries().size());
        assertTrue(as.getFundings().getFundingGroup().isEmpty());
        assertEquals(3, as.getPeerReviews().getPeerReviewGroup().size());
        assertEquals(3, as.getWorks().getWorkGroup().size());

        // Verify bio sections - bio and name filtered
        p = r.getPerson();
        assertEquals(3, p.getAddresses().getAddress().size());
        assertEquals(3, p.getEmails().getEmails().size());
        assertEquals(3, p.getExternalIdentifiers().getExternalIdentifiers().size());
        assertEquals(3, p.getKeywords().getKeywords().size());
        assertEquals(3, p.getOtherNames().getOtherNames().size());
        assertEquals(3, p.getResearcherUrls().getResearcherUrls().size());
        assertNull(p.getBiography());
        assertNull(p.getName());

        // Filter emails, external identifiers, peer reviews and works
        r = getRecordElement();
        setVisibility(r.getPerson().getEmails().getEmails(), Visibility.LIMITED, Visibility.PRIVATE, Visibility.LIMITED);
        setVisibility(r.getPerson().getExternalIdentifiers().getExternalIdentifiers(), Visibility.LIMITED, Visibility.PRIVATE, Visibility.LIMITED);
        setVisibility(r.getActivitiesSummary().getPeerReviews(), Visibility.LIMITED, Visibility.PRIVATE, Visibility.LIMITED);
        setVisibility(r.getActivitiesSummary().getWorks(), Visibility.LIMITED, Visibility.PRIVATE, Visibility.LIMITED);

        publicAPISecurityManagerV2.filter(r);

        // Verify activities - peer reviews and works filtered
        as = r.getActivitiesSummary();
        assertEquals(3, as.getEducations().getSummaries().size());
        assertEquals(3, as.getEmployments().getSummaries().size());
        assertEquals(3, as.getFundings().getFundingGroup().size());
        assertTrue(as.getPeerReviews().getPeerReviewGroup().isEmpty());
        assertTrue(as.getWorks().getWorkGroup().isEmpty());

        // Verify bio sections - emails, external identifiers filtered
        p = r.getPerson();
        assertEquals(3, p.getAddresses().getAddress().size());
        assertNotNull(p.getEmails());
        assertTrue(p.getEmails().getEmails().isEmpty());
        assertNotNull(p.getExternalIdentifiers());
        assertTrue(p.getExternalIdentifiers().getExternalIdentifiers().isEmpty());
        assertEquals(3, p.getKeywords().getKeywords().size());
        assertEquals(3, p.getOtherNames().getOtherNames().size());
        assertEquals(3, p.getResearcherUrls().getResearcherUrls().size());
        assertNotNull(p.getBiography());
        assertNotNull(p.getName());

        // Filter keywords and other names
        r = getRecordElement();
        setVisibility(r.getPerson().getOtherNames().getOtherNames(), Visibility.LIMITED, Visibility.PRIVATE, Visibility.LIMITED);
        setVisibility(r.getPerson().getKeywords().getKeywords(), Visibility.LIMITED, Visibility.PRIVATE, Visibility.LIMITED);

        publicAPISecurityManagerV2.filter(r);

        // Verify activities - nothing filtered
        as = r.getActivitiesSummary();
        assertEquals(3, as.getEducations().getSummaries().size());
        assertEquals(3, as.getEmployments().getSummaries().size());
        assertEquals(3, as.getFundings().getFundingGroup().size());
        assertEquals(3, as.getPeerReviews().getPeerReviewGroup().size());
        assertEquals(3, as.getWorks().getWorkGroup().size());

        // Verify bio sections - keywords and other names filtered
        p = r.getPerson();
        assertEquals(3, p.getAddresses().getAddress().size());
        assertEquals(3, p.getEmails().getEmails().size());
        assertEquals(3, p.getExternalIdentifiers().getExternalIdentifiers().size());
        assertNotNull(p.getKeywords());
        assertTrue(p.getKeywords().getKeywords().isEmpty());
        assertNotNull(p.getOtherNames());
        assertTrue(p.getOtherNames().getOtherNames().isEmpty());
        assertEquals(3, p.getResearcherUrls().getResearcherUrls().size());
        assertNotNull(p.getBiography());
        assertNotNull(p.getName());
    }

    /**
     * Utilities
     */
    private Filterable getFilterableElement(Visibility v) {
        EducationSummary s = new EducationSummary();
        s.setVisibility(v);
        return s;
    }

    private VisibilityType getVisibilityTypeElement(Visibility v) {
        EducationSummary s = new EducationSummary();
        s.setVisibility(v);
        return s;
    }

    private Employments getEmployments(Visibility... vs) {
        Employments e = new Employments();
        for (Visibility v : vs) {
            EmploymentSummary s = new EmploymentSummary();
            s.setVisibility(v);
            e.getSummaries().add(s);
        }
        return e;
    }

    private Educations getEducations(Visibility... vs) {
        Educations e = new Educations();
        for (Visibility v : vs) {
            EducationSummary s = new EducationSummary();
            s.setVisibility(v);
            e.getSummaries().add(s);
        }
        return e;
    }

    private Works getWorks(Visibility... vs) {
        Works works = new Works();
        for (Visibility v : vs) {
            WorkGroup g = new WorkGroup();
            WorkSummary s = new WorkSummary();
            s.setVisibility(v);
            g.getWorkSummary().add(s);
            works.getWorkGroup().add(g);
        }
        return works;
    }

    private Fundings getFundings(Visibility... vs) {
        Fundings fundings = new Fundings();
        for (Visibility v : vs) {
            FundingGroup g = new FundingGroup();
            FundingSummary s = new FundingSummary();
            s.setVisibility(v);
            g.getFundingSummary().add(s);
            fundings.getFundingGroup().add(g);
        }
        return fundings;
    }

    private PeerReviews getPeerReviews(Visibility... vs) {
        PeerReviews peerReviews = new PeerReviews();
        for (Visibility v : vs) {
            PeerReviewGroup g = new PeerReviewGroup();
            PeerReviewSummary s = new PeerReviewSummary();
            s.setVisibility(v);
            g.getPeerReviewSummary().add(s);
            peerReviews.getPeerReviewGroup().add(g);
        }
        return peerReviews;
    }

    private ActivitiesSummary getActivitiesSummaryElement() {
        Visibility[] vs = { Visibility.PUBLIC, Visibility.PUBLIC, Visibility.PUBLIC };
        ActivitiesSummary s = new ActivitiesSummary();
        s.setEducations(getEducations(vs));
        s.setEmployments(getEmployments(vs));
        s.setFundings(getFundings(vs));
        s.setPeerReviews(getPeerReviews(vs));
        s.setWorks(getWorks(vs));
        return s;
    }

    private OtherNames getOtherNamesElement(Visibility... vs) {
        OtherNames otherNames = new OtherNames();
        for (Visibility v : vs) {
            OtherName o = new OtherName();
            o.setVisibility(v);
            if (otherNames.getOtherNames() == null) {
                otherNames.setOtherNames(new ArrayList<OtherName>());
            }
            otherNames.getOtherNames().add(o);
        }
        return otherNames;
    }

    private Addresses getAddressesElement(Visibility... vs) {
        Addresses elements = new Addresses();
        for (Visibility v : vs) {
            Address element = new Address();
            element.setVisibility(v);
            if (elements.getAddress() == null) {
                elements.setAddress(new ArrayList<Address>());
            }
            elements.getAddress().add(element);
        }
        return elements;
    }

    private Emails getEmailsElement(Visibility... vs) {
        Emails elements = new Emails();
        for (Visibility v : vs) {
            Email element = new Email();
            element.setVisibility(v);
            if (elements.getEmails() == null) {
                elements.setEmails(new ArrayList<Email>());
            }
            elements.getEmails().add(element);
        }
        return elements;
    }

    private PersonExternalIdentifiers getPersonExternalIdentifiersElement(Visibility... vs) {
        PersonExternalIdentifiers elements = new PersonExternalIdentifiers();
        for (Visibility v : vs) {
            PersonExternalIdentifier element = new PersonExternalIdentifier();
            element.setVisibility(v);
            if (elements.getExternalIdentifiers() == null) {
                elements.setExternalIdentifiers(new ArrayList<PersonExternalIdentifier>());
            }
            elements.getExternalIdentifiers().add(element);
        }
        return elements;
    }

    private Keywords getKeywordsElement(Visibility... vs) {
        Keywords elements = new Keywords();
        for (Visibility v : vs) {
            Keyword element = new Keyword();
            element.setVisibility(v);
            if (elements.getKeywords() == null) {
                elements.setKeywords(new ArrayList<Keyword>());
            }
            elements.getKeywords().add(element);
        }
        return elements;
    }

    private ResearcherUrls getResearcherUrlsElement(Visibility... vs) {
        ResearcherUrls elements = new ResearcherUrls();
        for (Visibility v : vs) {
            ResearcherUrl element = new ResearcherUrl();
            element.setVisibility(v);
            if (elements.getResearcherUrls() == null) {
                elements.setResearcherUrls(new ArrayList<ResearcherUrl>());
            }
            elements.getResearcherUrls().add(element);
        }
        return elements;
    }

    private PersonalDetails getPersonalDetailsElement(Visibility nameVisibility, Visibility bioVisiblity, Visibility otherNamesVisibility) {
        PersonalDetails p = new PersonalDetails();
        Name name = new Name();
        name.setVisibility(nameVisibility);
        p.setName(name);
        Biography bio = new Biography();
        bio.setVisibility(bioVisiblity);
        bio.setContent("Bio test");
        p.setBiography(bio);
        p.setOtherNames(getOtherNamesElement(otherNamesVisibility));
        return p;
    }

    private Person getPersonElement() {
        Visibility[] vs = { Visibility.PUBLIC, Visibility.PUBLIC, Visibility.PUBLIC };
        Person p = new Person();
        p.setAddresses(getAddressesElement(vs));
        p.setEmails(getEmailsElement(vs));
        p.setExternalIdentifiers(getPersonExternalIdentifiersElement(vs));
        p.setKeywords(getKeywordsElement(vs));
        p.setOtherNames(getOtherNamesElement(vs));
        p.setResearcherUrls(getResearcherUrlsElement(vs));

        Name name = new Name();
        name.setVisibility(Visibility.PUBLIC);
        p.setName(name);

        Biography b = new Biography();
        b.setVisibility(Visibility.PUBLIC);
        b.setContent("Biography test");
        p.setBiography(b);

        return p;
    }

    private Record getRecordElement() {
        Record r = new Record();
        r.setActivitiesSummary(getActivitiesSummaryElement());
        r.setPerson(getPersonElement());
        return r;
    }

    private void setVisibility(List<? extends Filterable> elements, Visibility... vs) {
        assertEquals(elements.size(), vs.length);
        for (int i = 0; i < vs.length; i++) {
            elements.get(i).setVisibility(vs[i]);
        }
    }

    private void setVisibility(GroupsContainer container, Visibility... vs) {
        assertEquals(container.retrieveGroups().size(), vs.length);
        int idx = 0;
        for (Group g : container.retrieveGroups()) {
            // Every group have just one element
            assertEquals(1, g.getActivities().size());
            for (Filterable f : g.getActivities()) {
                f.setVisibility(vs[idx++]);
            }
        }
    }

    /**
     * Assert helpers
     */
    private void assertIsPublic(Filterable a) {
        assertEquals(Visibility.PUBLIC, a.getVisibility());
    }

    private void assertAllArePublic(List<? extends Filterable> list) {
        if (list == null) {
            return;
        }
        list.forEach(e -> {
            assertIsPublic(e);
        });
    }

    private void assertGroupContainsOnlyPublicElements(GroupsContainer container) {
        if (container == null || container.retrieveGroups() == null || container.retrieveGroups().isEmpty()) {
            fail("No activities");
        }
        container.retrieveGroups().forEach(x -> {
            assertNotNull(x.getActivities());
            x.getActivities().forEach(e -> {
                assertIsPublic(e);
            });
        });
    }

    private void assertContainerContainsOnlyPublicElements(ActivitiesContainer container) {
        if (container == null || container.retrieveActivities() == null || container.retrieveActivities().isEmpty()) {
            fail("No activities");
        }
        container.retrieveActivities().forEach(x -> {
            assertIsPublic(x);
        });
    }
}
