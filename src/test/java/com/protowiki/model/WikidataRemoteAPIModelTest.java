package com.protowiki.model;

import com.protowiki.model.WikidataRemoteAPIModel;
import com.protowiki.beans.Author;
import com.protowiki.beans.Record;
import com.protowiki.core.DataTransformer;
import com.protowiki.utils.RecordSAXParser;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
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
public class WikidataRemoteAPIModelTest {

    public static Logger logger = LoggerFactory.getLogger(WikidataRemoteAPIModelTest.class);

    public WikidataRemoteAPIModelTest() {
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
     * Test of getWikipediaAbstract method, of class WikidataRemoteAPIModel.
     */
    @Test
    public void testRunQueryOnWikidata() {
        String minimumViableQuery = StringUtils.join(
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>",
                "select ?resource where {",
                "?resource rdfs:label \"London\"",
                "}"
        , "\n");
        WikidataRemoteAPIModel instance = new WikidataRemoteAPIModel();
        String results = instance.runQueryOnWikidata(minimumViableQuery, null);
        assertNotNull(results);
    }

    /**
     * Test of getWikipediaAbstract method, of class WikidataRemoteAPIModel.
     */
    @Test
    public void testGetWikipediaAbstractByName() {
        logger.info("getWikipediaAbstract");
        String author = "Mark Twain";
        String language = "en";
        WikidataRemoteAPIModel instance = new WikidataRemoteAPIModel();
        String articleAbstract = instance.getWikipediaAbstractByName(author, language);
        assertNotNull(articleAbstract);
        assertTrue(articleAbstract.length() > 0);
    }

    /**
     * Test of testGetWikipediaAbstractByViafId method, of class WikidataRemoteAPIModel.
     */
    @Test
    public void testGetWikipediaAbstractByViafId() {
        logger.info("getWikipediaAbstract");
        String viafId = "50566653";
        String language = "en";
        WikidataRemoteAPIModel instance = new WikidataRemoteAPIModel();
        String articleAbstract = instance.getWikipediaAbstractByViafId(viafId, language);
        assertNotNull(articleAbstract);
        assertTrue(articleAbstract.length() > 0);
    }
    
//    /**
//     * Test of testGetWikipediaAbstractByViafId method, of class WikidataRemoteAPIModel.
//     */
//    @Test
//    public void testGetMultipleWikipediaAbstractByViafIds() {
//        logger.info("getMultipleWikipediaAbstractByViafIds");
//        List<String> vids = Arrays.asList("50566653", "113230702");
//        String language = "en";
//        WikidataRemoteAPIModel instance = new WikidataRemoteAPIModel();
//        Map<String, String> absMap = instance.getMultipleWikipediaAbstractByViafIds(vids, language);
//        
//        for (String key : absMap.keySet()) {
//            System.out.println("Key: " + key + ", value: " + absMap.get(key));
//        }
//        
//        assertNotNull("Should not be null", absMap);
//        assertEquals("Should returns 2 objects", absMap.keySet().size(), 2);
//    }
//    
//    /**
//     * Test of testGetWikipediaAbstractByViafId method, of class WikidataRemoteAPIModel.
//     */
//    @Test
//    public void testGetMultipleWikipediaAbstractByViafs() {
//        logger.info("getMultipleWikipediaAbstractByViafs");
//        
//        RecordSAXParser parser = new RecordSAXParser();
//        DataTransformer optimus = new DataTransformer();
//        List<Record> records = parser.parseXMLFileForRecords("C://files//authbzi.xml");
//        List<Author> authorsList = optimus.transformRecordsListToAuthors(records);
//        
//        List<String> viafs = authorsList.stream().map(a -> {
//            return a.getViafId().trim();
//        }).collect(Collectors.toList());
//        
//        WikidataRemoteAPIModel instance = new WikidataRemoteAPIModel();
//        Map<String, String> absMap = instance.getMultipleWikipediaAbstractByViafIds(viafs, "en");
//        
//        assertNotNull("Should not be null", absMap);
//        assertTrue("Should returns multiple objects", absMap.keySet().size()>0);
//    }

    /**
     * Test of getViafFromAuthors method, of class WikidataRemoteAPIModel.
     */
    @Test
    @Ignore
    public void testGetAuthorsWithVIAF() {
        logger.info("getViafFromAuthors");
        WikidataRemoteAPIModel instance = new WikidataRemoteAPIModel();
        List<Author> authors = instance.getAuthorsWithVIAF();
        assertNotNull(authors);
        assertTrue(authors.size() > 0);
    }

    /**
     * Test of getViafFromAuthors method, of class WikidataRemoteAPIModel.
     */
    @Test
    public void testGetAuthorsWithVIAFRemote() {
        logger.info("getViafFromAuthorsRemote");
        WikidataRemoteAPIModel instance = new WikidataRemoteAPIModel();
        List<Author> authors = instance.getAuthorsWithVIAFRemote();
        assertNotNull(authors);
        assertTrue(authors.size() > 0);
    }

}
