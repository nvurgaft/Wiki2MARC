package com.protowiki.core;


import com.protowiki.beans.Author;
import com.protowiki.beans.Record;
import com.protowiki.utils.RecordSAXParser;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
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
public class DataTransformerTest {

    public Logger logger = LoggerFactory.getLogger(DataTransformerTest.class);

    public DataTransformerTest() {
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
     * Test of transformRecordsListToAuthors method, of class DataTransformer.
     */
    @Test
    public void testTransformRecordsListToAuthors() {
        logger.info("transformRecordsListToAuthors");
        List<Record> recordsList = null;
        DataTransformer instance = new DataTransformer();
        List<Author> expResult = null;
        List<Author> result = instance.transformRecordsListToAuthors(recordsList);

    }

    /**
     * Test of insertAuthorIntoDB method, of class DataTransformer.
     */
    @Test
    public void testInsertAuthorIntoDB() {
        logger.info("insertAuthorIntoDB");
        String s = "";
        String p = "";
        String o = "";
        DataTransformer instance = new DataTransformer();

    }

    /**
     * Test of batchInsertAuthorIntoDB method, of class DataTransformer.
     */
    @Test
    public void testBatchInsertAuthorIntoDB() {
        logger.info("batchInsertAuthorIntoDB");
        List<Author> authors = null;
        DataTransformer instance = new DataTransformer();

    }

    /**
     * Test of generateMARCXMLFile method, of class DataTransformer.
     */
    @Test
    public void testGenerateMARCXMLFile() {
        logger.info("generateMARCXMLFile");
        String filePath = "c://files//authbzi.xml";
        
        DataTransformer instance = new DataTransformer();
        //Map<String, String> articleAbstracts = instance.generateMARCXMLFile(filePath, articleAbstracts);
    }

    /**
     * Test of generateMARCXMLFile method, of class DataTransformer.
     */
    @Test
    public void testDynamicallyGenerateMARCXMLFile() {
        logger.info("generateMARCXMLFile");
        String filePath = "c://files//authbzi.xml";
        DataTransformer instance = new DataTransformer();
        boolean result = instance.dynamicallyGenerateMARCXMLFile(filePath);
        assertTrue("Should return true if method was successful", result);
    }

    @Test
    public void testProcess() {
        logger.info("testing process");
        List<Author> authors = null;
        List<Author> filteredAuthors = null;
        try {
            File file = new File("c://files//authbzi.xml");
            RecordSAXParser parse = new RecordSAXParser();
            List<Record> records = parse.parseXMLFileForRecords(file);
            DataTransformer transformer = new DataTransformer();
            authors = transformer.transformRecordsListToAuthors(records);
            filteredAuthors = authors.stream().filter(a -> {
                return ((a.getNames().get("heb") != null || a.getNames().get("lat") != null) && a.getViafId() != null);
            }).collect(Collectors.toList());
        } catch (Exception ex) {
            logger.error("Exception in testProcess", ex);
        }

        assertNotNull("Objects should have been initialized", authors);
        assertNotNull("Objects should have been initialized", filteredAuthors);
        assertTrue("Filtered authors size should not exceed original authors size", filteredAuthors.size() <= authors.size());
    }

}
