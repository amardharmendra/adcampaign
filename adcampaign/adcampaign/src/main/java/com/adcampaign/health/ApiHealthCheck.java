package com.adcampaign.health;

import com.adcampaign.AdcampaignConfiguration;
import com.codahale.metrics.health.HealthCheck;
import io.dropwizard.jetty.HttpConnectorFactory;
import io.dropwizard.server.DefaultServerFactory;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Test the health of the API. Just calls a dummy api endpoint.
 */
public class ApiHealthCheck extends HealthCheck {
    private Logger log = LoggerFactory.getLogger(ApiHealthCheck.class);
    private AdcampaignConfiguration configuration;

    public ApiHealthCheck(AdcampaignConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    protected HealthCheck.Result check() throws Exception {
        Client client = new JerseyClientBuilder().build();
        int port = ((HttpConnectorFactory) ((DefaultServerFactory) configuration.getServerFactory())
                                           .getApplicationConnectors().get(0)).getPort();

        String url = "http://localhost:" + port + "/apihealthcheck";
        WebTarget webTarget = client.target(url);
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.get();
        if (response.getStatus() == 200) {
            return Result.healthy();
        }
        else {
            this.log.error("Healthcheck call failed. response={}", response);
            return Result.unhealthy("Received " + response.getStatus() + " response from the " + url);
        }
    }
}
