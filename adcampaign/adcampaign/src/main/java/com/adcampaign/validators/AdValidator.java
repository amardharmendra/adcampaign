package com.adcampaign.validators;

import com.adcampaign.api.Ad;
import com.adcampaign.util.ServiceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

public class AdValidator {

    private static Logger LOG = LoggerFactory.getLogger(AdValidator.class);

    public static void validateForInsert(Ad ad) {
        List<Error> errors = new ArrayList<>();

        if(ad.getAdContent() == null || ad.getAdContent().length() == 0) {
            errors.add(new Error("Ad content is needed."));
        }

        if(ad.getDuration() == null || ad.getDuration() <= 0) {
            errors.add(new Error("Duration is needed and should be positive."));
        }

        if (errors.size() > 0) {
            throw new WebApplicationException(ServiceUtils.buildErrorResponse(Response.Status.BAD_REQUEST, errors));
        }
    }

}
