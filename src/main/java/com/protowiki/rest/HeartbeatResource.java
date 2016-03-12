package com.protowiki.rest;

import com.protowiki.model.WikidataRemoteAPIModel;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Nick
 */
@Path("heartbeat")
public class HeartbeatResource {

    public static Logger logger = LoggerFactory.getLogger(HeartbeatResource.class);

    @GET
    @Path("server")
    @Produces(MediaType.APPLICATION_JSON)

    public Response getServerHeartbeat() {
        return Response.status(Status.OK).build();
    }

    @GET
    @Path("database")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDatabaseHeartbeat() {
        
        String queryResult;
        try {
            queryResult = new WikidataRemoteAPIModel().runQueryOnWikidata("SELECT 1", "SPARQL");
            if (!queryResult.isEmpty()) {
                return Response.status(Status.OK).build();
            }
        } catch (Exception e) {
            logger.error("Exception while check database status", e);
        }
        return Response.status(Status.SERVICE_UNAVAILABLE).build();
    }
}
