package com.adcampaign;

import com.adcampaign.db.AdDao;
import com.adcampaign.db.MongoManager;
import com.adcampaign.db.impl.AdDaoMongo;
import com.adcampaign.db.impl.AdDaoTempStorage;
import com.adcampaign.health.ApiHealthCheck;
import com.adcampaign.health.MongoHealthCheck;
import com.adcampaign.resources.AdResource;
import com.adcampaign.service.AdManager;
import com.adcampaign.service.impl.AdManagerImpl;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdcampaignApplication extends Application<AdcampaignConfiguration> {

    private static Logger LOG = LoggerFactory.getLogger(AdcampaignApplication.class);

    public static void main(final String[] args) throws Exception {
        new AdcampaignApplication().run(args);
    }

    @Override
    public String getName() {
        return "adcampaign";
    }

    @Override
    public void initialize(final Bootstrap<AdcampaignConfiguration> bootstrap) {
        bootstrap.addBundle(new SwaggerBundle<AdcampaignConfiguration>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(AdcampaignConfiguration configuration) {
                return configuration.getSwaggerBundleConfiguration();
            }
        });
    }

    @Override
    public void run(final AdcampaignConfiguration configuration,
                    final Environment environment) {
        // Ignores Unknown JSON Properties and does not error out with "Unable to process JSON"
        environment.getObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        environment.getObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);


        // Dao
        MongoManager mongoManager = new MongoManager(configuration.getMongoConfiguration());
        AdDao adDao = new AdDaoTempStorage();

        // Managed Instances
        environment.lifecycle().manage(mongoManager);

        // Health Check
        MongoHealthCheck mongoHealthCheck =new MongoHealthCheck(mongoManager);
        environment.healthChecks().register("APIHealthCheck", new ApiHealthCheck(configuration));
        environment.healthChecks().register("mongoHealthCheck", mongoHealthCheck);

        // Managers
        AdManager adManager = null;

        if(mongoHealthCheck.execute().isHealthy()) {
            adManager = new AdManagerImpl(new AdDaoMongo(mongoManager.getDatastore()));
            LOG.info("Using permanent storage.");
        } else {
            LOG.warn("Using temporary storage.");
            adManager = new AdManagerImpl(adDao);
        }


        //Add resource
        environment.jersey().register(new AdResource(adManager));
    }

}
