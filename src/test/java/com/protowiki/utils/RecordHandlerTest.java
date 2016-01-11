package com.protowiki.utils;

import com.protowiki.beans.Record;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Ignore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kobi
 */
@Ignore
public class RecordHandlerTest {

    private static Logger logger = LoggerFactory.getLogger(RecordHandlerTest.class);

    public RecordHandlerTest() {
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
     * Test "testParseRecord()"
     *
     * Should: Take a single XML record and parse it into a record object. Pass:
     * If and only if the newly generated object is equal to the expected object
     * provided. Fail: Otherwise.
     */
    @Test
    public void testParseRecord() {

        String fileName = "C:\\files\\authbzi.xml";
        RecordSAXParser parser = new RecordSAXParser();
        List<Record> records = parser.parseXMLFileForRecords(fileName);
        logger.info("Records: ");
        for (Record record : records) {
            System.out.println(record.toString());
        }
        logger.info("Total records amount: " + records.size());
    }
}
