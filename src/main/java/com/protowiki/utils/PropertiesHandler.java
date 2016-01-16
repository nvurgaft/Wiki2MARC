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
public class PropertiesHandler {

    public static Logger logger = LoggerFactory.getLogger(PropertiesHandler.class);

    private Properties props;
    private String fileName;

    /**
     *  Constructs the PropertiesHandler using a file reference
     * @param propfile
     */
    public PropertiesHandler(File propfile) {
        this.fileName = propfile.getName();
        this.props = this.readProperties(this.fileName);
    }

    /**
     *  Constructs the PropertiesHandler using a file name
     * @param fileName
     */
    public PropertiesHandler(String fileName) {
        this.fileName = fileName;
        this.props = this.readProperties(this.fileName);
    }

    private Properties readProperties(String filename) {
        props = new Properties();
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream stream = loader.getResourceAsStream(filename);
            props.load(stream);
        } catch (IOException ioex) {
            logger.error("IOException in readProperties", ioex);
        }
        return props;
    }

    /**
     *  Return the Properties file
     * @return the Properties file
     */
    public Properties getProperties() {
        return props;
    }

    /**
     *  Return the file name
     * @return the file name string 
     */
    public String getFileName() {
        return fileName;
    }

    /**
     *  Retrieves a property value using a property key
     * @param key property key
     * @return key's value
     */
    public String getProperty(String key) {
        return this.props.getProperty(key);
    }

    /**
     *  Iterates over the properties and appends all of them using a StringBuilder
     * @return the string containing all the properties and their respective values
     */
    public String printProperties() {
        StringBuilder sb = new StringBuilder();
        this.props.keySet().stream().forEach(o -> {
            String s = (String) o;
            sb.append("key: ").append(s).append(", value: ").append(this.props.getProperty(s)).append("\n");
        });
        return sb.toString();
    }

    @Override
    public String toString() {
        return "Properties handler for " + fileName;
    }
}
