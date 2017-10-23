package com.adcampaign.health;

import com.adcampaign.db.MongoManager;
import com.codahale.metrics.health.HealthCheck;

public class MongoHealthCheck extends HealthCheck {

    private MongoManager mongoManager;

    public MongoHealthCheck(MongoManager mongoManager) {
        this.mongoManager = mongoManager;
    }

    @Override
    protected Result check() throws Exception {
        mongoManager.getMongoDb();
        return Result.healthy();
    }
}
