package com.adcampaign.service.impl;

import com.adcampaign.api.Ad;
import com.adcampaign.api.BaseQueryResponse;
import com.adcampaign.converter.AdConverter;
import com.adcampaign.db.AdDao;
import com.adcampaign.db.model.DbAd;
import com.adcampaign.service.AdManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class AdManagerImpl implements AdManager {

    private static Logger LOG = LoggerFactory.getLogger(AdManagerImpl.class);

    private AdDao adDao;

    public AdManagerImpl(final AdDao adDao) {
        this.adDao = adDao;
    }

    @Override
    public BaseQueryResponse<Ad> createAd(Ad ad) {
        DbAd dbAd = AdConverter.convertToDbAd(ad);
        dbAd.setStartTime(System.currentTimeMillis());
        dbAd = adDao.upsert(dbAd);
        BaseQueryResponse<Ad> response = new BaseQueryResponse<>();
        response.setCount(1l);
        response.setResponse(Collections.singletonList(AdConverter.convertToDbAd(dbAd)));
        response.setStart(0l);
        response.setTotal(1l);
        return response;
    }

    @Override
    public BaseQueryResponse<Ad> getActiveAdsByPartnerId(String partnerId) {
        List<DbAd> partnerDbActiveAds = adDao.findByPartnerId(partnerId);
        List<Ad> partnerActiveAds = new LinkedList<>();
        for(DbAd dbAd : partnerDbActiveAds) {
            partnerActiveAds.add(AdConverter.convertToDbAd(dbAd));
        }

        BaseQueryResponse<Ad> response = new BaseQueryResponse<>();

        response.setTotal((long) partnerActiveAds.size());
        response.setStart((long) 0);
        response.setResponse(partnerActiveAds);
        response.setCount((long) partnerActiveAds.size());

        return response;
    }

    @Override
    public BaseQueryResponse<Ad> getAllActiveAds() {
        List<DbAd> dbActiveAds = adDao.getAllAds();
        List<Ad> partnerActiveAds = new LinkedList<>();
        for(DbAd dbAd : dbActiveAds) {
            partnerActiveAds.add(AdConverter.convertToDbAd(dbAd));
        }

        BaseQueryResponse<Ad> response = new BaseQueryResponse<>();

        response.setTotal((long) partnerActiveAds.size());
        response.setStart((long) 0);
        response.setResponse(partnerActiveAds);
        response.setCount((long) partnerActiveAds.size());

        return response;
    }
}
