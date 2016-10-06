package com.protowiki.utils;

import com.protowiki.values.Values;
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
public class ApplicationProperties {

    public static Logger logger = LoggerFactory.getLogger(ApplicationProperties.class);

    private Properties properties;
    private final String fileName;

    /**
     * Constructs the PropertiesHandler using the default application.properties
     * path
     */
    public ApplicationProperties() {
        this.properties = this.readProperties(Values.APP_PROPS_NAME);
        this.fileName = Values.APP_PROPS_NAME;
    }

    /**
     * Constructs the PropertiesHandler using a file reference
     *
     * @param file
     */
    public ApplicationProperties(File file) {
        this.properties = this.readProperties(file.getPath());
        this.fileName = file.getPath();
    }

    /**
     * Constructs the PropertiesHandler using a file name
     *
     * @param file
     */
    public ApplicationProperties(String file) {
        this.properties = this.readProperties(file);
        this.fileName = file;
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
     * Retrieves a property value as a string
     *
     * @param key property key
     * @param defaultValue value to return in case the keyed value is missing
     * @return string value assigned to the key
     */
    public String getString(String key, String defaultValue) {
        if (this.properties.getProperty(key) != null) {
            return this.properties.getProperty(key);
        } else {
            return defaultValue;
        }
    }

    /**
     * Retrieves a property value as a integer
     *
     * @param key property key
     * @param defaultValue value to return in case the keyed value is missing
     * @return integer value assigned to the key
     */
    public int getInt(String key, int defaultValue) {
        if (this.properties.getProperty(key) != null) {
            int value = -1;
            try {
                value = Integer.valueOf(this.properties.getProperty(key));
            } catch (NumberFormatException nfe) {
                logger.error("NumberFormatException occured parsing property value for key: " + key, nfe);
            }
            return value;
        } else {
            return defaultValue;
        }
    }

    /**
     * Retrieves a property value as a boolean
     *
     * @param key property key
     * @param defaultValue value to return in case the keyed value is missing
     * @return boolean value assigned to the key
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        if (this.properties.getProperty(key) != null) {
            return Boolean.valueOf(this.properties.getProperty(key));
        } else {
            return defaultValue;
        }
    }
}
