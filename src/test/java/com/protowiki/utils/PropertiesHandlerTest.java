package com.protowiki.utils;

import java.util.Properties;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
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
    
    DatabaseProperties instance = null;
    
    public PropertiesHandlerTest() {
        instance = new DatabaseProperties("application.properties");
    }
    
    @Rule
    public TestName testName = new TestName();
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        logger.info(testName.getMethodName());
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getProperties method, of class PropertiesHandler.
     */
    @Test
    public void testGetProperties() {

        Properties properties = instance.getProperties();
        
        properties.keySet().forEach(key -> {
            logger.info("Key: " + key + " ,Value: " + properties.getProperty((String) key));
        });
        assertTrue("Properties should hold more than 0 Key/Value pairs", properties.keySet().size()>0);
    }

    /**
     * Test of getFileName method, of class PropertiesHandler.
     */
    @Test
    public void testGetFileName() {

        String name = instance.getFileName();
        assertEquals("Should get this properties name", name, "application.properties");
    }

    /**
     * Test of getProperty method, of class PropertiesHandler.
     */
    @Test
    public void testGetProperty() {

        String port_key = "port";
        String expResult = "1111";
        
        String result = instance.getProperty(port_key);
        assertEquals("Should read the property key of port", result, expResult);
    }

    /**
     * Test of printProperties method, of class PropertiesHandler.
     */
    @Test
    public void testPrintProperties() {

        logger.info(instance.printProperties());
    }
    
}
