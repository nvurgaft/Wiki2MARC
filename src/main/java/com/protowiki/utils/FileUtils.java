package com.protowiki.utils;

import static com.protowiki.utils.Validators.isBlank;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Nick
 */
public class FileUtils {
    
    public static Logger logger = LoggerFactory.getLogger(FileUtils.class);

    /**
     * Checks the file exists in the specified path
     *
     * @param path string absolute path to file
     * @return true if file exists
     */
    public static boolean exists(String path) {
        if (isBlank(path)) {
            return false;
        }
        return new File(path).exists();
    }

    /**
     * Creates a file in the specified path
     *
     * @param path string absolute path to file
     * @return true if file was successfully created
     */
    public static boolean createFile(String path) {
        if (isBlank(path)) {
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

    /**
     * Creates a directory in the specified path
     *
     * @param path string absolute path to file
     * @return true if directory was successfully created
     */
    public static boolean createDir(String path) {
        if (isBlank(path)) {
            return false;
        }
        try {
            File dir = new File(path);
            return dir.mkdir();
        } catch (Throwable e) {
            logger.error("Exception while creating directory", e);
            return false;
        }
    }

    /**
     * Reads an input stream and writes it's data into a file on the specified
     * fileLocation
     *
     * @param inputStream input stream
     * @param fileLocation string absolute path to file
     * @return true is stream was successfully read and the file was created
     */
    public static boolean saveFile(InputStream inputStream, String fileLocation) {
        boolean result;
        try (OutputStream outputStream = new FileOutputStream(new File(fileLocation))) {
            int read;
            byte[] bytes = new byte[1024];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
            outputStream.flush();
            logger.debug("File saved to location {}", fileLocation);
            result = true;
        } catch (IOException ioex) {
            logger.error("IOException while saving file to disk", ioex);
            result = false;
        }
        return result;
    }
    
    public static boolean saveFile1(InputStream inputStream, String fileLocation) {
        boolean result;
        try (OutputStream outputStream = new FileOutputStream(new File(fileLocation))) {
            int read;
            byte[] bytes = new byte[1024];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
            outputStream.flush();
            logger.debug("File saved to location {}", fileLocation);
            result = true;
        } catch (IOException ioex) {
            logger.error("IOException while saving file to disk", ioex);
            result = false;
        }
        return result;
    }

    /**
     * Reads a file from the provided file path and returns it's content
     *
     * @param file the file to read
     * @return the file's content as a string
     * @throws IOException
     */
    public static String fileReader(File file) throws IOException {
        if (file == null) {
            return null;
        }
        
        StringBuilder content = new StringBuilder();
        String line;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        }
        return content.toString();
    }
}
