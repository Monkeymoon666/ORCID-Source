package org.orcid.core.manager.impl;

import javax.annotation.Resource;

import org.orcid.core.manager.SalesForceManager;

import net.sf.ehcache.constructs.blocking.CacheEntryFactory;

public class SalesForceMembersListCacheEntryFactory implements CacheEntryFactory {

    @Resource
    private SalesForceManager salesForceManager;

    @Override
    public Object createEntry(Object key) throws Exception {
        return salesForceManager.retrieveFreshMembers();
    }

}
