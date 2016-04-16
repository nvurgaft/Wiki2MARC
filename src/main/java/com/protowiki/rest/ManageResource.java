package com.protowiki.rest;

import com.protowiki.entities.DatabasePropertiesEntity;
import com.protowiki.utils.DatabaseProperties;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
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

    private final String propertiesFile = "application.properties";

    @GET
    @Path("get-all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {

        Map props = new HashMap();
        try {
            DatabaseProperties properties = new DatabaseProperties(propertiesFile);
            properties.getProperties().keySet().stream().forEach(key -> {
                props.put(key, properties.getProperty((String) key));
            });
        } catch (Exception e) {
            logger.error("An Exception has occured while marshalling database properties", e);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.ok(props).build();
    }

    @PUT
    @Path("update")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePreferences(Map<String, String> jsonProperties) {

        DatabaseProperties dbProps = new DatabaseProperties(propertiesFile);
        try {
            jsonProperties.keySet().stream().forEach(key -> {
                dbProps.setProperty(key, jsonProperties.get((String) key));
            });
        } catch (Exception e) {
            logger.error("An Exception has occured while updating database properties", e);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.ok().build();
    }
}
