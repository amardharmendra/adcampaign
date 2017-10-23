package com.adcampaign.db.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class DbAd {

    private static Logger LOG = LoggerFactory.getLogger(DbAd.class);

    private String id;
    private String partnerId;
    private Long duration;
    private String adContent;
    private Long startTime;
}
