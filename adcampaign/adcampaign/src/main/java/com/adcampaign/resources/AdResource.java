package com.adcampaign.resources;

import com.adcampaign.api.Ad;
import com.adcampaign.converter.AdConverter;
import com.adcampaign.db.AdDao;
import com.adcampaign.db.model.DbAd;
import com.adcampaign.service.AdManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/ad")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "AdResource", description = "End point for ads.")
public class AdResource {

    private static Logger LOG = LoggerFactory.getLogger(AdResource.class);

    private AdManager adManager;

    public AdResource(AdManager adManager) {
        this.adManager = adManager;
    }

    @POST
    @ApiOperation("Creates an ad(with multiple ads per partner).")
    public Response createAd(@NotNull Ad ad) {
        return Response.ok(adManager.createAd(ad)).build();
    }

    @GET
    @Path("{partnerId}")
    @ApiOperation("Get all active ads by partner id.")
    public Response getActiveAds(@PathParam("partnerId") @NotNull String partnerId) {
        return Response.ok(adManager.getActiveAdsByPartnerId(partnerId)).build();
    }

    @GET
    @Path("activeads")
    @ApiOperation("Get all active ads.")
    public Response getAllActiveAds() {
        return Response.ok(adManager.getAllActiveAds()).build();
    }


}
