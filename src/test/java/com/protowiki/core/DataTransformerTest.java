package com.protowiki.core;

import com.protowiki.beans.Author;
import com.protowiki.beans.Record;
import com.protowiki.utils.RecordSAXParser;
import com.protowiki.values.Values;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.After;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
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
public class DataTransformerTest {

    public Logger logger = LoggerFactory.getLogger(DataTransformerTest.class);
    
    DataTransformer transformer = new DataTransformer();
    
    @Rule
    public TestName testName = new TestName();

    @Before
    public void before() {
        logger.info("before: {}", testName.getMethodName());
    }

    @After
    public void after() {
        logger.info("after: {}", testName.getMethodName());
    }

    /**
     * Test of transformRecordsListToAuthors method, of class DataTransformer.
     */
    @Test
    public void testTransformRecordsListToAuthors() {
        
        List<Record> recordsList = null;
        DataTransformer instance = new DataTransformer();
        List<Author> expResult = null;
        List<Author> result = instance.transformRecordsListToAuthors(recordsList);

    }

    /**
     * Test of generateMARCXMLFile method, of class DataTransformer.
     */
    @Test
    public void testGenerateMARCXMLFile() {

        String filePath = Values.FILE_PATH + "authbzi.xml";
        
        DataTransformer instance = new DataTransformer();
        //Map<String, String> articleAbstracts = instance.generateMARCXMLFile(filePath, articleAbstracts);
    }

    /**
     * Test of generateMARCXMLFile method, of class DataTransformer.
     */
    @Test
    public void testDynamicallyGenerateMARCXMLFile() {

        String filePath = Values.FILE_PATH + "authbzi.xml";
        DataTransformer instance = new DataTransformer();
        boolean result = instance.dynamicallyGenerateMARCXMLFile(filePath, null); // will break;
        assertTrue("Should return true if method was successful", result);
    }

    @Test
    public void testProcess() {
        
        logger.info("testing process");
        List<Author> authors = null;
        List<Author> filteredAuthors = null;
        try {
            File file = new File(Values.FILE_PATH + "authbzi.xml");
            RecordSAXParser parse = new RecordSAXParser();
            List<Record> records = parse.parseXMLFileForRecords(file);
            authors = transformer.transformRecordsListToAuthors(records);
            filteredAuthors = authors.stream().filter(a -> {
                return ((a.getNames().get("heb") != null || a.getNames().get("lat") != null) && a.getViafId() != null);
            }).collect(Collectors.toList());
        } catch (Exception ex) {
            logger.error("Exception in testProcess", ex);
        }

        assertNotNull("Authors should have been initialized", authors);
        assertNotNull("Filtered authors should have been initialized", filteredAuthors);
        assertTrue("Filtered authors size should not exceed original authors size", filteredAuthors.size() <= authors.size());
    }

}
