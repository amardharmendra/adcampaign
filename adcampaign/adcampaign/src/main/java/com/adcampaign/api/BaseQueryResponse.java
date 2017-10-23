package com.adcampaign.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Getter
@Setter
public class BaseQueryResponse<T> {

    @JsonProperty
    private Long start;
    @JsonProperty
    private Long total;
    @JsonProperty
    private Long count;
    @JsonProperty
    private List<T> response;
    @JsonProperty
    private List<String> warnings;
    @JsonProperty
    private List<String> errors;

}
