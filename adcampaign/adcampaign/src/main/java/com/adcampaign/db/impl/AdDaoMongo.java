package com.adcampaign.db.impl;

import com.adcampaign.converter.AdConverter;
import com.adcampaign.db.AdDao;
import com.adcampaign.db.model.DbAd;
import com.adcampaign.db.model.MongoDbAd;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

public class AdDaoMongo implements AdDao{

    private static Logger LOG = LoggerFactory.getLogger(AdDaoMongo.class);

    private Datastore datastore;

    public AdDaoMongo(Datastore datastore) {
        this.datastore = datastore;
    }

    @Override
    public DbAd upsert(DbAd dbAd) {
        MongoDbAd mongoDbAd = AdConverter.convertToMongoDbAd(dbAd);
        Key<MongoDbAd> key = datastore.save(mongoDbAd);
        //dbAd.setId(((ObjectId)key.getId()).toHexString());
        dbAd.setId(mongoDbAd.getId().toHexString());
        return dbAd;
    }

    @Override
    public Boolean delete(DbAd dbAd) {
        return false;
    }

    @Override
    public List<DbAd> findByPartnerId(String id) {
        Query<MongoDbAd> query = datastore.createQuery(MongoDbAd.class);

        List<MongoDbAd> result = new LinkedList<>();

        if(id != null && id.length() > 0) {
            query.field("partnerId").equalIgnoreCase(id);
            result = query.asList();
        }

        List<DbAd> dbAds = new LinkedList<>();

        for(MongoDbAd mongoDbAd : result) {
            dbAds.add(AdConverter.convertToDbAd(mongoDbAd));
        }

        return AdDaoTempStorage.findLiveAds(dbAds);
    }

    @Override
    public List<DbAd> getAllAds() {
        List<MongoDbAd> allAds = datastore.createQuery(MongoDbAd.class).asList();
        List<DbAd> dbAds = new LinkedList<>();

        for(MongoDbAd mongoDbAd : allAds) {
            dbAds.add(AdConverter.convertToDbAd(mongoDbAd));
        }

        return AdDaoTempStorage.findLiveAds(dbAds);
    }
}
