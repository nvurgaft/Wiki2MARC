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
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Nick
 */
//@Ignore
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
        System.out.println(results);
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
