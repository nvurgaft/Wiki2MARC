
package com.protowiki.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 *
 * @author Koby
 */

@Path("sparql")
public class SparqlEndpointResource {
    
    @GET
    @Produces("application/json")
    public Response postQuery(@QueryParam("query") String query) {
        
        return Response.status(Status.OK).entity("query response").build();
    }
}
