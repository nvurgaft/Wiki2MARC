package com.protowiki.model;

import com.protowiki.beans.Author;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
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
 * @author Kobi
 */
@Ignore
public class WikipediaRemoteAPIModelTest {
    
    public static Logger logger = LoggerFactory.getLogger(WikipediaRemoteAPIModelTest.class);
    
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
     * Test of getAbstractByArticleName method, of class WikipediaRemoteAPIModel.
     */
    @Test
    public void testGetAbstractByArticleName() {
        
        String articleName = "Potato";
        String language = "en";
        WikipediaRemoteAPIModel instance = new WikipediaRemoteAPIModel();
        String result = instance.getAbstractByArticleName(articleName, language);   
        logger.info("Result: " + result);
    }

    /**
     * Test of getAbstractsByArticleNames method, of class WikipediaRemoteAPIModel.
     */
    @Test
    public void testGetAbstractsByArticleNames() {

        List<Author> authors = null;
        String language = "he";
        WikipediaRemoteAPIModel instance = new WikipediaRemoteAPIModel();
        Map<String, String> result = instance.getAbstractsByArticleNames(authors, language);
        result.keySet().forEach(key -> {
            logger.info("Key: " + key + ", Value: " + result.get(key));
        });
    }
    
}
