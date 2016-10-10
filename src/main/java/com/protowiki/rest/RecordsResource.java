package com.protowiki.rest;

import com.protowiki.beans.FileDetails;
import com.protowiki.beans.ResponseMessage;
import com.protowiki.core.MARCFileFactory;
import com.protowiki.utils.ApplicationProperties;
import com.protowiki.utils.FileUtils;
import com.protowiki.values.Values;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
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

    public static Logger logger = LoggerFactory.getLogger(RecordsResource.class);

    /**
     *
     * @return
     */
    @GET
    @Path("get-files")
    @Produces(MediaType.APPLICATION_JSON)
    public Response scanAndGetFiles() {

        File[] files = new File(Values.FILE_PATH).listFiles();

        List<FileDetails> filesDetails = new ArrayList<>();
        for (File file : files) {
            FileDetails fd = new FileDetails();
            fd.setName(file.getName());

            java.nio.file.Path filePath = Paths.get(Values.FILE_PATH + file.getName());
            BasicFileAttributes attr;
            
            try {
                attr = Files.readAttributes(filePath, BasicFileAttributes.class);
                fd.setCreationTime(attr.creationTime().toString());
                fd.setLastModified(attr.lastModifiedTime().toString());              
            } catch (IOException ioex) {
                logger.warn("IOException while reading file properties", ioex);
            }

            try {
                String content = FileUtils.fileReader(file);
                fd.setSize(content.length());
            } catch (IOException ioex) {
                logger.error("IOException while reading file " + file.getName(), ioex);
                return Response.status(Status.INTERNAL_SERVER_ERROR).build();
            }
            filesDetails.add(fd);
        }
        return Response.status(Status.OK).entity(filesDetails).build();
    }

    @GET
    @Path("get-file-detail")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getFileDetails(@QueryParam("fileName") String fileName) {

        File file = new File(Values.FILE_PATH + fileName);
        String content;
        try {
            content = FileUtils.fileReader(file);
        } catch (IOException ioex) {
            logger.error("IOException while reading file " + fileName, ioex);
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Could not get file").build();
        }
        return Response.status(Status.OK).entity(content).build();
    }

    @GET
    @Path("download-file")
    @Produces(MediaType.APPLICATION_JSON)
    public void downloadFile(@Suspended AsyncResponse asyncResponse,
            @QueryParam("file") String fileName) {

        try {
            java.nio.file.Path path = Paths.get(Values.FILE_PATH + fileName);

            byte[] data = Files.readAllBytes(path);
            asyncResponse.resume(Response
                    .status(Status.OK)
                    .type(MediaType.APPLICATION_OCTET_STREAM)
                    .entity(data)
                    .header("Content-Disposition", "attachment; filename=" + path.getFileName() + "")
                    .build());

        } catch (Throwable ex) {
            logger.error("Error reading file", ex);
            asyncResponse.resume(Response.status(Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity("Error reading file").build());
        }

    }

    /**
     *
     * @param fileName
     * @return
     */
    @GET
    @Path("xml-parse-file")
    @Produces(MediaType.APPLICATION_JSON)
    public Response xmlParseFile(@QueryParam("file") String fileName) {

        String path = Values.FILE_PATH;
        String propsFileName = Values.APP_PROPS_NAME;
        
        ApplicationProperties dbprop = new ApplicationProperties(propsFileName); //use_database
        boolean useDatabase = dbprop.getBoolean("use_database", false);

        MARCFileFactory proc = new MARCFileFactory(useDatabase);
        int result = proc.runProcess(path + fileName);
        ResponseMessage rm = new ResponseMessage();
        if (result == 0) {
            rm.setStatus(0).setData("File generation was successful");
            return Response.status(Status.OK).entity(rm).build();
        } else {
            rm.setStatus(-1).setData("File generation was interrupted");
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(rm).build();
        }
    }

    /**
     *
     * @param fileName
     * @return
     */
    @DELETE
    @Path("remove-file")
    @Produces(MediaType.TEXT_PLAIN)
    public Response removeFile(@QueryParam("fileName") String fileName) {

        File file = new File(Values.FILE_PATH + fileName);
        try {
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            logger.error("An exception occured while attempting to delete the file: " + file.toString(), e);
            return Response
                    .status(Status.INTERNAL_SERVER_ERROR)
                    .entity("An exception occured while attempting to delete the file")
                    .build();
        }

        return Response.ok("File deleted").build();
    }
}
