package com.adcampaign.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;

@Getter
@Setter
public class Ad {

    private static Logger LOG = LoggerFactory.getLogger(Ad.class);

    private String id;
    @JsonProperty("partner_id")
    private String partnerId;
    private Long duration;
    @JsonProperty("ad_content")
    private String adContent;
}
