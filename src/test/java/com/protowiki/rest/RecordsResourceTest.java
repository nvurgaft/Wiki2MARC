package com.protowiki.rest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Nick
 */
@Ignore
public class RecordsResourceTest {

    public static Logger logger = LoggerFactory.getLogger(RecordsResourceTest.class);

    public RecordsResourceTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getFiles method, of class RecordsResource.
     */
    @Test
    public void testGetFiles() {
        File file = new File("C://files//");
        logger.debug("Scanning for files");
        File[] files = file.listFiles();
        for (File f : files) {
            logger.info("Found: " + f.getName());
            int fileSize = 0;
            try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    fileSize += line.length();
                    System.out.println(line);
                }
                ++fileSize;
            } catch (IOException ioex) {
                logger.error("IOException while reading file " + f.getName(), ioex);
            }
            logger.info("Size: " + fileSize);
        }
        logger.debug("Done.");
    }

}
