package com.protowiki.rest;

import com.protowiki.beans.Author;
import com.protowiki.beans.FileDetails;
import com.protowiki.beans.Record;
import com.protowiki.core.DataTransformer;
import com.protowiki.utils.FileUtils;
import com.protowiki.utils.RecordSAXParser;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
            try {
                String content = FileUtils.fileReader(f);
                fd.setContent(content);
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

    @POST
    @Path("xml-parse-file")
    @Produces(MediaType.APPLICATION_JSON)
    public Response xmlParseFile(@QueryParam("file") String fileName) {

        RecordSAXParser parser = new RecordSAXParser();
        DataTransformer prime = new DataTransformer();
        List<Record> recordsList = null;
        List<Author> authorsList = null;
        try {
            recordsList = parser.parseXMLFileForRecords(fileName);
            authorsList = prime.transformRecordsListToAuthors(recordsList);
        } catch (Exception ex) {
            logger.error("Exception in servlet while parsing MARC XML file", ex);
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex).build();
        }

        return Response.status(Status.OK).entity(authorsList).build();
    }

}
