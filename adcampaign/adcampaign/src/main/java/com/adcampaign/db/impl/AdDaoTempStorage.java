package com.adcampaign.db.impl;

import com.adcampaign.db.AdDao;
import com.adcampaign.db.model.DbAd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.*;

public class AdDaoTempStorage implements AdDao {

    private static Logger LOG = LoggerFactory.getLogger(AdDaoTempStorage.class);

    private Map<String, DbAd> storage;
    private Map<String, Set<DbAd>> storageByPartnerId;

    public AdDaoTempStorage(){
        storage = new HashMap<>();
        storageByPartnerId = new HashMap<>();
    }


    @Override
    public DbAd upsert(DbAd dbAd) {
        if(dbAd == null) return null;

        if(dbAd.getId() != null && storage.containsKey(dbAd.getId()) ) {
            update(dbAd);
        } else {
            insert(dbAd);
        }

        return dbAd;
    }

    private DbAd insert(DbAd dbAd) {
        String newKey = UUID.randomUUID().toString();

        while(storage.containsKey(newKey)) {
            newKey = UUID.randomUUID().toString();
        }

        dbAd.setId(newKey);
        storage.put(newKey, dbAd);

        if(!storageByPartnerId.containsKey(dbAd.getPartnerId()))
            storageByPartnerId.put(dbAd.getPartnerId(), new HashSet<>());

        storageByPartnerId.get(dbAd.getPartnerId()).add(dbAd);


        return dbAd;
    }

    private DbAd update(DbAd dbAd) {
        DbAd fromStorage = storage.get(dbAd.getId());

        fromStorage.setAdContent(dbAd.getAdContent());
        fromStorage.setDuration(dbAd.getDuration());

        return dbAd;
    }

    @Override
    public Boolean delete(DbAd dbAd) {
        if(storage.containsKey(dbAd.getId())) {
            storage.remove(dbAd.getId());
            storageByPartnerId.get(dbAd.getPartnerId()).remove(dbAd);
            if(storageByPartnerId.get(dbAd.getPartnerId()).size() == 0) {
                storageByPartnerId.remove(dbAd.getPartnerId());
            }
            return true;
        }
        return false;
    }

    @Override
    public List<DbAd> findByPartnerId(String partenerId) {
        if(storageByPartnerId.containsKey(partenerId)) {
            return findLiveAds(storageByPartnerId.get(partenerId));
        } else {
            throw new WebApplicationException("no active ad campaigns exist for the specified partner", Response.Status
                    .NOT_FOUND);
        }
    }

    public static List<DbAd> findLiveAds(Collection<DbAd> dbAds) {
        List<DbAd> liveCampaigns = new LinkedList<>();
        Long currentTimeInMillis = System.currentTimeMillis();
        for(DbAd dbAd : dbAds) {
            if(dbAd.getDuration() * 1000 + dbAd.getStartTime() > currentTimeInMillis) {
                liveCampaigns.add(dbAd);
            }
        }
        return liveCampaigns;
    }

    @Override
    public List<DbAd> getAllAds() {
        List<DbAd> dbAds = new LinkedList<>();
        for(Map.Entry<String, Set<DbAd>> entry : storageByPartnerId.entrySet()) {
            dbAds.addAll(findLiveAds(entry.getValue()));
        }
        return dbAds;
    }
}
