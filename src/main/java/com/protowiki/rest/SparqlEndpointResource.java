
package com.protowiki.rest;

import com.protowiki.model.WikidataRemoteAPIModel;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.glassfish.jersey.server.ManagedAsync;

/**
 *
 * @author Koby
 */

@Path("sparql")
public class SparqlEndpointResource {
    
    @GET
    @ManagedAsync
    @Produces(MediaType.TEXT_PLAIN)
    public void postQuery(
            @Suspended AsyncResponse asyncResponse,
            @QueryParam("query") String query,
            @QueryParam("endpoint") String endpoint) {
        
        String queryResult = new WikidataRemoteAPIModel().runQueryOnWikidata(query, endpoint);
        
        asyncResponse.resume(Response.status(Status.OK).entity(queryResult).build());
    }
}
