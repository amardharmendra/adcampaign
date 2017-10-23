package com.adcampaign.service;

import com.adcampaign.api.Ad;
import com.adcampaign.api.BaseQueryResponse;

public interface AdManager {
    BaseQueryResponse<Ad> createAd(Ad ad);
    BaseQueryResponse<Ad> getActiveAdsByPartnerId(String partnerId);
    BaseQueryResponse<Ad> getAllActiveAds();
}
