package com.adcampaign.db;

import com.adcampaign.db.model.DbAd;

import java.util.List;

public interface AdDao {
    DbAd upsert(DbAd dbAd);
    Boolean delete(DbAd dbAd);
    List<DbAd> findByPartnerId(String id);
    List<DbAd> getAllAds();
}
