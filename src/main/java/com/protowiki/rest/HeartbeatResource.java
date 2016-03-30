package com.protowiki.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import virtuoso.jena.driver.VirtGraph;

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
    @Produces(MediaType.TEXT_PLAIN)
    public Response getDatabaseHeartbeat() {
        
        try {
            VirtGraph g = new VirtGraph("jdbc:virtuoso://localhost:1111/CHARSET=UTF-8/log_enable=2", "dba", "dba");
            String graphName = g.getGraphName();
            g.close();
            if (!graphName.isEmpty()) {
                return Response.status(Status.OK).build();
            }
        } catch (Exception e) {
            logger.warn("Exception while check database status");
            return Response.status(Status.SERVICE_UNAVAILABLE).entity("Cannot connect to database").build();
        }
        return Response.status(Status.SERVICE_UNAVAILABLE).entity("Database found").build();
    }
}
