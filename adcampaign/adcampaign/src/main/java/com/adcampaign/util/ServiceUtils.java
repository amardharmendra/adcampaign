package com.adcampaign.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class ServiceUtils {

    private static Logger LOG = LoggerFactory.getLogger(ServiceUtils.class);
    public ServiceUtils() {
    }

    public static Response buildErrorResponse(Response.Status status, Error... errors) {
        Map<String, Object> response = new LinkedHashMap();
        response.put("errors", errors);
        return Response.status(status).entity(response).build();
    }

    public static Response buildErrorResponse(Response.Status status, Collection<Error> errors) {
        return buildErrorResponse(status, (Error[])errors.toArray(new Error[errors.size()]));
    }

    public static WebApplicationException generateErrorResponse(Response.Status responseStatus, String errorMessage) {
        Response.ResponseBuilder responseBuilder = Response.status(responseStatus).entity(errorMessage);
        return new WebApplicationException(responseBuilder.build());
    }
}
