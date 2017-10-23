package com.adcampaign.db;

import com.adcampaign.configuration.MongoConfiguration;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ReadPreference;
import com.mongodb.client.MongoDatabase;
import io.dropwizard.lifecycle.Managed;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

public class MongoManager implements Managed {

    private MongoClient mongoClient;
    private MongoDatabase mongoDb;
    private Datastore datastore;

    public MongoManager(MongoConfiguration mongoConfig) {

        Morphia morphia = new Morphia();
        morphia.mapPackage("com.adcampaign.db.model");

        MongoClientOptions mongoClientOptions = MongoClientOptions.builder()
            .readPreference(ReadPreference.secondaryPreferred())
            .connectTimeout(mongoConfig.getConnectTimeout())
            .heartbeatConnectTimeout(mongoConfig.getHeartbeatConnectTimeout())
            .socketKeepAlive(true)
            .build();

        mongoClient = new MongoClient(mongoConfig.getAddress(), mongoClientOptions);

        mongoDb = mongoClient.getDatabase(mongoConfig.getDatabase());

        datastore = morphia.createDatastore(mongoClient, mongoConfig.getDatabase());
        datastore.ensureIndexes();
    }

    @Override
    public void start() throws Exception {
    }

    @Override
    public void stop() throws Exception {
        mongoClient.close();
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }

    public MongoDatabase getMongoDb() {
        return mongoDb;
    }

    public Datastore getDatastore() {
        return datastore;
    }
}
