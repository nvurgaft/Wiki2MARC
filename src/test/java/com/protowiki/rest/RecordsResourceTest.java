package com.protowiki.rest;

import com.protowiki.values.Values;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Nick
 */
@Ignore
public class RecordsResourceTest {

    public static Logger logger = LoggerFactory.getLogger(RecordsResourceTest.class);

    @Rule
    private final TestName testName = new TestName();

    @Before
    public void before() {
        logger.info("before: {}", testName.getMethodName());
    }

    @After
    public void after() {
        logger.info("after: {}", testName.getMethodName());
    }

    /**
     * Test of getFiles method, of class RecordsResource.
     */
    @Test
    public void testGetFiles() {
        File file = new File(Values.FILE_PATH);
        logger.debug("Scanning for files");
        File[] files = file.listFiles();
        for (File f : files) {
            logger.info("Found: {}", f.getName());
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
            logger.info("Size: {}", fileSize);
        }
    }

}
