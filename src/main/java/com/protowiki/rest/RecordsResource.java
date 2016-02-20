package com.protowiki.rest;

import com.protowiki.beans.FileDetails;
import com.protowiki.beans.ResponseMessage;
import com.protowiki.core.MainProcess;
import com.protowiki.utils.FileUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
    
    private static final String FILE_PATH = "C://files//";

    @GET
    @Path("get-files")
    @Produces(MediaType.APPLICATION_JSON)
    public Response scanAndGetFiles() {

        File file = new File(FILE_PATH);
        File[] files = file.listFiles();

        List<FileDetails> filesDetails = new ArrayList<>();
        for (File f : files) {
            FileDetails fd = new FileDetails();
            fd.setName(f.getName());
            int fileSize = 0;
            try {
                String content = FileUtils.fileReader(f);
                fileSize = content.length();
            } catch (IOException ioex) {
                logger.error("IOException while reading file " + f.getName(), ioex);
                return Response.status(Status.INTERNAL_SERVER_ERROR).build();
            }
            fd.setSize(fileSize);
            filesDetails.add(fd);
        }
        return Response.status(Status.OK).entity(filesDetails).build();
    }

    @GET
    @Path("xml-parse-file")
    @Produces(MediaType.APPLICATION_JSON)
    public Response xmlParseFile(@QueryParam("file") String fileName) {

        MainProcess proc = new MainProcess();
        System.out.println("File path is : " + FILE_PATH + fileName);
        int result = proc.runProcess(FILE_PATH + fileName);
        ResponseMessage rm = new ResponseMessage();
        if (result==0) {
            rm.setStatus(0).setData("File generation was successful");
            return Response.status(Status.OK).entity(rm).build();
        } else {
            rm.setStatus(-1).setData("File generation was interrupted");
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(rm).build();
        }
    }

}
