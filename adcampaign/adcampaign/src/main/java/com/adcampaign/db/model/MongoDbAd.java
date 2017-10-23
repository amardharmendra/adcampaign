package com.adcampaign.db.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity("ads")
public class MongoDbAd {

    private static Logger LOG = LoggerFactory.getLogger(MongoDbAd.class);

    @Id
    private ObjectId id;
    private String partnerId;
    private Long duration;
    private String adContent;
    private Long startTime;
}
