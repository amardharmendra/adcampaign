package com.adcampaign.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mongodb.ServerAddress;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MongoConfiguration {

    @NotNull
    @JsonProperty
    private List<HostInfo> hosts;

    @NotNull
    @JsonProperty
    private String database;

    @JsonProperty
    private String replicaSetName;

    @NotNull
    @JsonProperty
    private int connectTimeout;

    @NotNull
    @JsonProperty
    private int heartbeatConnectTimeout;


    @Getter
    @Setter
    public static class HostInfo {

        @NotNull
        @JsonProperty
        protected String host;

        @NotNull
        @JsonProperty
        protected int port;

    }

    public List<ServerAddress> getAddress() {
        List<ServerAddress> mongoServers = new ArrayList<>();
        for (HostInfo host : this.hosts) {
            mongoServers.add(new ServerAddress(host.getHost(), host.getPort()));
        }
        return mongoServers;

    }

}
