package com.protowiki.rest;

import com.protowiki.utils.FileUtils;
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
 *
 * @author Nick
 */
@Path("upload")
public class DataImportResource {

    private static Logger logger = LoggerFactory.getLogger(DataImportResource.class);
    private static final String FILE_SAVE_PATH = "C://files//";

    @POST
    @Path("xml_file")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response postXmlFile(
            @FormDataParam("file") InputStream fileInputStream,
            @FormDataParam("file") FormDataContentDisposition contentDispositionHeader
    ) {
        String filePath = FILE_SAVE_PATH + contentDispositionHeader.getFileName();
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
