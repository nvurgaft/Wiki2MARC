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
 *  This servlet provides indications whether the server and the database are 
 *  active, note that because this servlet is a part of the server, if the 
 *  server is down then the user also won't get a response from the database.
 * 
 * @author Nick
 */
@Path("heartbeat")
public class HeartbeatResource {

    public static Logger logger = LoggerFactory.getLogger(HeartbeatResource.class);
    
    /**
     * Indication for the server
     * @return OK, otherwise an exception message will appear
     */
    @GET
    @Path("server")
    @Produces(MediaType.APPLICATION_JSON)

    public Response getServerHeartbeat() {
        return Response.status(Status.OK).build();
    }
    
    /**
     * Indication for the database
     * @return 200 OK, otherwise, 
     *         500 INTERNAL_SERVER_ERROR if an error occurred, 
     *         503 SERVICE_UNAVAILABLE if unavailable  
     */
    @GET
    @Path("database")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDatabaseHeartbeat() {
        
        String queryResult;
        try {
            queryResult = new WikidataRemoteAPIModel().runTestQuery("SELECT 1", "SPARQL");
            if (!queryResult.isEmpty()) {
                return Response.status(Status.OK).build();
            }
        } catch (Exception e) {
            logger.error("Exception while check database status", e);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.status(Status.SERVICE_UNAVAILABLE).build();
    }
}
