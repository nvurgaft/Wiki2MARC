package com.protowiki.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 *
 * @author Nick
 */
@Path("heartbeat")
public class HeartbeatResource {
    
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
        return Response.status(Status.OK).build();
    }
}
