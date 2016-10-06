package com.protowiki.rest;

import com.protowiki.utils.ApplicationProperties;
import com.protowiki.values.Values;
import java.util.Map;
import java.util.stream.Collectors;
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
@Path("manage")
public class ManageResource {

    public static Logger logger = LoggerFactory.getLogger(ManageResource.class);

    private final String propertiesFile = Values.APP_PROPS_NAME;

    @GET
    @Path("get-all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {

        try {
            ApplicationProperties properties = new ApplicationProperties(propertiesFile);

            Map<String, String> props = properties.getProperties()
                    .keySet()
                    .stream()
                    .map(key -> (String) key)
                    .collect(Collectors.toMap(key -> key, key -> properties.getString(key, "")));
            return Response.ok(props).build();
        } catch (Exception e) {
            logger.error("An Exception has occured while marshalling database properties", e);
        }
        return Response.status(Status.INTERNAL_SERVER_ERROR).build();
    }
}
