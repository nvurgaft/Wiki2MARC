package com.protowiki.utils;

import com.protowiki.beans.Author;
import com.protowiki.beans.Record;
import com.protowiki.core.DataTransformer;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
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
public class RecordHandlerTest {

    public static Logger logger = LoggerFactory.getLogger(RecordHandlerTest.class);
    
    private final String testXMLFileName = "C:\\files\\authbzi.xml";

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
     * testParseRecord
     *
     * Should: Take a single XML record and parse it into a record object.
     */
    @Test
    public void testParseRecord() {      
        RecordSAXParser parser = new RecordSAXParser();
        List<Record> records = parser.parseXMLFileForRecords(testXMLFileName);
        assertNotNull("Should hold an object and not be null", records);
        assertTrue("Should hold multiple values", records.size()>0);
    }
    
    /**
     *  testRecordToAuthorTransformation
     *  
     * Should: Take a single XML record and parse it into a record object.
     */
    @Test
    public void testRecordToAuthorTransformation() {
        RecordSAXParser parser = new RecordSAXParser();
        DataTransformer optimus = new DataTransformer();
        List<Record> records = parser.parseXMLFileForRecords(testXMLFileName);
        List<Author> authorsList = optimus.transformRecordsListToAuthors(records);
        authorsList.stream().forEach(author -> {
            logger.info(author.getViafId());
        });
        assertNotNull("Should hold an object and not be null", records);
        assertTrue("Should hold multiple values", records.size()>0);
    }
}
