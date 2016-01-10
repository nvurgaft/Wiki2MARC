package com.protowiki.rest;

import com.protowiki.beans.FileDetails;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
@Path("records")
public class RecordsResource {
    
    private static Logger logger = LoggerFactory.getLogger(RecordsResource.class);
    
    @GET
    @Path("get-files")
    @Produces(MediaType.APPLICATION_JSON)
    public Response scanAndGetFiles() {
        
        File file = new File("C://files//");
        File[] files = file.listFiles();
        List<FileDetails> filesDetails = new ArrayList<>();
        for (File f : files) {
            FileDetails fd = new FileDetails();
            fd.setName(f.getName());
            int fileSize = 0;
            try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
                String line, content = "";
                while ((line = reader.readLine()) != null) {
                    fileSize += line.length();
                    content+=line;
                }
                fd.setContent(content);
                ++fileSize;
            } catch (IOException ioex) {
                logger.error("IOException while reading file " + f.getName(), ioex);
                return Response.status(Status.INTERNAL_SERVER_ERROR).build();
            }
            fd.setSize(fileSize);
            filesDetails.add(fd);
        }
        return Response.status(Status.OK).entity(filesDetails).build();
    }
}
