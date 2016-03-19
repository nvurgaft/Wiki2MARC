package com.protowiki.rest;

import com.protowiki.utils.FileUtils;
import com.protowiki.values.Values;
import java.io.InputStream;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  Servlet class for the Data Import UI, allows loading files in the 
 *  working directory 
 * 
 * @author Nick
 */
@Path("upload")
public class DataImportResource {

    public static Logger logger = LoggerFactory.getLogger(DataImportResource.class);

    @POST
    @Path("xml_file")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response postXmlFile(
            @FormDataParam("file") InputStream fileInputStream,
            @FormDataParam("file") FormDataContentDisposition contentDispositionHeader
    ) {
        String filePath = Values.FILE_PATH + contentDispositionHeader.getFileName();
        boolean result = FileUtils.saveFile(fileInputStream, filePath);
        if (result) {
            return Response
                    .status(Status.OK)
                    .entity("File saved successfuly at " + filePath)
                    .build();
        } else {
            return Response
                    .status(Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed saving file at " + filePath)
                    .build();
        }
    }
}
