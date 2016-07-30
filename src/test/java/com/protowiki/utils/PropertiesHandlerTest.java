package com.protowiki.utils;

import java.util.Properties;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kobi
 */
@Ignore
public class PropertiesHandlerTest {

    public static Logger logger = LoggerFactory.getLogger(PropertiesHandlerTest.class);

    DatabaseProperties instance = new DatabaseProperties("application.properties");

    @Rule
    public TestName testName = new TestName();

    @Before
    public void before() {
        logger.info("before: " + testName.getMethodName());
    }

    @After
    public void after() {
        logger.info("after: " + testName.getMethodName());
    }

    /**
     * Test of getProperties method, of class PropertiesHandler.
     */
    @Test
    public void testGetProperties() {

        Properties properties = instance.getProperties();

        properties.keySet().stream()
                .map(obj -> (String) obj)
                .forEach(key -> {
                    logger.info("Key: " + key + " ,Value: " + properties.getProperty(key));
                });
        Assert.assertTrue("Properties should hold more than 0 Key/Value pairs", properties.keySet().size() > 0);
    }

    /**
     * Test of getFileName method, of class PropertiesHandler.
     */
    @Test
    public void testGetFileName() {

        String name = instance.getFileName();
        Assert.assertEquals("Should get this properties name", "application.properties", name);
    }

    /**
     * Test of getProperty method, of class PropertiesHandler.
     */
    @Test
    public void testGetProperty() {

        String port_key = "port";
        int expResult = 1111;

        int result = instance.getInt(port_key, 1111);
        logger.info("Should match port 1111");
        logger.info("Fetched port value: " + result);
        Assert.assertEquals("Should read the property key of port", result, expResult);
    }

}
