package com.adcampaign.converter;

import com.adcampaign.api.Ad;
import com.adcampaign.db.model.DbAd;
import com.adcampaign.db.model.MongoDbAd;
import com.adcampaign.util.ServiceUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class AdConverter {

    private static Logger LOG = LoggerFactory.getLogger(AdConverter.class);

    public static DbAd convertToDbAd(Ad ad) {
        if(ad == null) return null;

        DbAd dbAd = new DbAd();
        dbAd.setAdContent(ad.getAdContent());
        dbAd.setId(ad.getId());
        dbAd.setDuration(ad.getDuration());
        dbAd.setPartnerId(ad.getPartnerId());

        return dbAd;
    }

    public static Ad convertToDbAd(DbAd dbAd) {
        if(dbAd == null) {
            return null;
        }

        Ad ad = new Ad();
        ad.setAdContent(dbAd.getAdContent());
        ad.setDuration(dbAd.getDuration());
        ad.setId(dbAd.getId());
        ad.setPartnerId(dbAd.getPartnerId());

        return ad;
    }

    public static DbAd convertToDbAd(MongoDbAd mongoDbAd) {
        if(mongoDbAd == null) return null;

        DbAd ad = new DbAd();
        ad.setPartnerId(mongoDbAd.getPartnerId());
        if(mongoDbAd.getId() != null)
            ad.setId(mongoDbAd.getId().toHexString());

        ad.setDuration(mongoDbAd.getDuration());
        ad.setAdContent(mongoDbAd.getAdContent());
        ad.setStartTime(mongoDbAd.getStartTime());

        return ad;
    }

    public static MongoDbAd convertToMongoDbAd(DbAd ad) {
        if(ad == null) return null;

        MongoDbAd mongoDbAd = new MongoDbAd();
        mongoDbAd.setAdContent(ad.getAdContent());
        mongoDbAd.setDuration(ad.getDuration());
        mongoDbAd.setPartnerId(ad.getPartnerId());
        mongoDbAd.setStartTime(ad.getStartTime());

        if(ad.getId() != null && ObjectId.isValid(ad.getId())) {
            mongoDbAd.setId(new ObjectId(ad.getId()));
        }


        return mongoDbAd;
    }

}
