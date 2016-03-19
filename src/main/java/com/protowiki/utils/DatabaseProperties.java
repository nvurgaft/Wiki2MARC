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
     * Constructs the PropertiesHandler using a file reference
     *
     * @param propfile
     */
    public DatabaseProperties(File propfile) {
        try {
            this.fileName = propfile.getName();
            this.properties = this.readProperties(this.fileName);
        } catch (Exception e) {
            logger.error("Could not load properties file", e);
            this.fileName = null;
            this.properties = null;
        }
    }

    /**
     * Constructs the PropertiesHandler using a file name
     *
     * @param fileName
     */
    public DatabaseProperties(String fileName) {
        this.fileName = fileName;
        this.properties = this.readProperties(this.fileName);
    }

    public void setProperties(File propfile) {
        if (propfile != null && !propfile.isDirectory()) {
            this.fileName = propfile.getName();
            this.properties = this.readProperties(this.fileName);
        }
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
     * @return key's value
     */
    public String getProperty(String key) {
        return this.properties.getProperty(key);
    }

    /**
     * Iterates over the properties and appends all of them using a
     * StringBuilder
     *
     * @return the string containing all the properties and their respective
     * values
     */
    public String printProperties() {
        StringBuilder sb = new StringBuilder();
        this.properties.keySet().stream().forEach(o -> {
            String s = (String) o;
            sb.append("key: ").append(s).append(", value: ").append(this.properties.getProperty(s)).append("\n");
        });
        return sb.toString();
    }

    @Override
    public String toString() {
        return "Properties handler for " + fileName;
    }
}
