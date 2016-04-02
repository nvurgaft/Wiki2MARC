package com.protowiki.model;

import com.protowiki.beans.Author;
import com.protowiki.values.Prefixes;
import com.protowiki.values.Providers;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
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
public class WikidataRemoteAPIModelTest {

    public static Logger logger = LoggerFactory.getLogger(WikidataRemoteAPIModelTest.class);

    @Rule
    public TestName testName = new TestName();

    @Before
    public void before() {
        logger.info("before: " + testName.getMethodName());
    }

    @After
    public void after() {
        logger.info("after: " + testName.getMethodName());
    }

    /**
     * Test of getWikipediaAbstract method, of class WikidataRemoteAPIModel.
     */
    @Test
    public void testRunQueryOnWikidata() {
        String minimumViableQuery = StringUtils.join(
                Prefixes.WDT,
                Prefixes.WIKIBASE,
                Prefixes.BD,
                Prefixes.WD,
                "SELECT ?item ?itemLabel",
                "WHERE",
                "{",
                "?item wdt:P31 wd:Q146 . ",
                "SERVICE wikibase:label { bd:serviceParam wikibase:language 'en' }",
                "}", "\n");
        WikidataRemoteAPIModel instance = new WikidataRemoteAPIModel();
        String results = instance.runRemoteQuery(minimumViableQuery, Providers.WIKIDATA);
        System.out.println(results);
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
     * Test of testGetWikipediaAbstractByViafId method, of class
     * WikidataRemoteAPIModel.
     */
    @Test
    public void testGetWikipediaAbstractByViafId() {

        String viafId = "113230702";
        String language = "en";
        WikidataRemoteAPIModel instance = new WikidataRemoteAPIModel();
        String articleAbstract = instance.getWikipediaAbstractByViafId(viafId, language);
        
        logger.info("abstract: " + articleAbstract);   
        assertNotNull("Should not be null (meaning the resultset should be at least 1 row)", articleAbstract);
        assertTrue("Should contain the abstract for Douglas Noel Adams", articleAbstract.length() > 0);
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
