package com.protowiki.model;

import com.protowiki.beans.Author;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
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
public class WikidataRemoteAPIModelTest {

    public static Logger logger = LoggerFactory.getLogger(WikidataRemoteAPIModelTest.class);

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
    
    @Test
    public void testGetAuthorLabelByViaf() {
        
        String viafId = "113230702";
        String language = "en";
        WikidataRemoteAPIModel instance = new WikidataRemoteAPIModel();
        String result = instance.getAuthorLabelByViaf(viafId, language);
        
        logger.info("result: " + result);
    }

    /**
     * Test of getWikipediaAbstract method, of class WikidataRemoteAPIModel.
     */
    @Test
    public void testGetWikipediaAbstractByName() {
        
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
//        List<Record> records = parser.parseXMLFileForRecords("//content//authbzi.xml");
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

        WikidataRemoteAPIModel instance = new WikidataRemoteAPIModel();
        List<Author> authors = instance.getAuthorsWithVIAFRemote();
        assertNotNull(authors);
        assertTrue(authors.size() > 0);
    }

}
