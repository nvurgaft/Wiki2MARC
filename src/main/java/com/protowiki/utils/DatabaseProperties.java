package com.protowiki.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Nick
 */
public class DatabaseProperties {

    public static Logger logger = LoggerFactory.getLogger(DatabaseProperties.class);

    private Properties properties;
    private String fileName;
    
    /**
     * Constructs the PropertiesHandler using the default application.properties
     * path
     */
    public DatabaseProperties() {
        this.properties = this.readProperties("application.properties");
        this.fileName = "application.properties";
    }

    /**
     * Constructs the PropertiesHandler using a file reference
     *
     * @param propfile
     */
    public DatabaseProperties(File file) {
        this.properties = this.readProperties(file.getPath());
        this.fileName = file.getPath();
    }

    /**
     * Constructs the PropertiesHandler using a file name
     *
     * @param file
     */
    public DatabaseProperties(String file) {
        this.properties = this.readProperties(file);
        this.fileName = file;
    }
    
    public void setProperty(String key, String value) {
        this.properties.setProperty(key, value);
    }

    public void setProperties(File file) {
        this.fileName = file.getName();
        this.properties = this.readProperties(file.getName());
    }

    public void setProperties(String fileName) {
        if (fileName != null && !fileName.isEmpty()) {
            this.fileName = fileName;
            this.properties = this.readProperties(this.fileName);
        }
    }

    private Properties readProperties(String filename) {
        properties = new Properties();
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream stream = loader.getResourceAsStream(filename);
            properties.load(stream);
        } catch (IOException ioex) {
            logger.error("IOException in readProperties", ioex);
        }
        return properties;
    }

    /**
     * Return the Properties file
     *
     * @return the Properties file
     */
    public Properties getProperties() {
        return properties;
    }

    /**
     * Return the file name
     *
     * @return the file name string
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Retrieves a property value using a property key
     *
     * @param key property key
     * @return value assigned to the key
     */
    public String getProperty(String key) {
        return this.properties.getProperty(key);
    }
}
