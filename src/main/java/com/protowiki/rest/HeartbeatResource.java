
package com.protowiki.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 *
 * @author Kobi
 */
@Path("heartbeat")
public class HeartbeatResource {
    
    @GET
    @Produces("application/json")
    public Response getServerHeartbeat() {
        return Response.status(Status.OK).entity("ok").build();
    }
}
