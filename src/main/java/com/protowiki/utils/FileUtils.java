package com.protowiki.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kobi
 */
public class FileUtils {

    private static Logger logger = LoggerFactory.getLogger(FileUtils.class);

    public static boolean exists(String path) {
        if (path == null || path.isEmpty()) {
            return false;
        }
        return new File(path).exists();
    }

    public static boolean createFile(String path) {
        if (path == null || path.isEmpty()) {
            return false;
        }
        try {
            File file = new File(path);
            file.createNewFile();
        } catch (IOException ioex) {
            logger.error("Could not create file from given path", ioex);
            return false;
        }
        return true;
    }

    public static boolean createDir(String path) {
        if (path == null || path.isEmpty()) {
            return false;
        }
        File dir = new File(path);
        dir.mkdir();
        return true;
    }

    public static boolean saveFile(InputStream inputStream, String fileLocation) {
        boolean result;
        try (OutputStream outputStream = new FileOutputStream(new File(fileLocation))) {
            int read;
            byte[] bytes = new byte[1024];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
            outputStream.flush();
            logger.debug("File saved to location " + fileLocation);
            result = true;
        } catch (IOException ioex) {
            logger.error("IOException while saving file to disk", ioex);
            result = false;
        }
        return result;
    }
}
