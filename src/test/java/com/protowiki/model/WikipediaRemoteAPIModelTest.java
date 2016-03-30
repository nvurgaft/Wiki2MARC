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
 * @author Nick
 */
@Ignore
public class WikipediaRemoteAPIModelTest {
    
    public static Logger logger = LoggerFactory.getLogger(WikipediaRemoteAPIModelTest.class);
    
    @Rule
    public TestName testName = new TestName();
    
    @Before
    public void setUp() {
        logger.info("Before method: " + testName.getMethodName());
    }
    
    @After
    public void tearDown() {
        logger.info("After method: " + testName.getMethodName());
    }

    /**
     * Test of getAbstractByArticleName method, of class WikipediaRemoteAPIModel.
     */
    @Test
    public void testGetAbstractByArticleNameEn() {
        
        String articleName = "Potato";
        String language = "en";
        WikipediaRemoteAPIModel instance = new WikipediaRemoteAPIModel();
        String result = instance.getAbstractByArticleName(articleName, language);   
        logger.info("Result: " + result);
    }
    
    /**
     * Test of getAbstractByArticleName method, of class WikipediaRemoteAPIModel.
     */
    @Test
    public void testGetAbstractByArticleNameHe() {
        
        String articleName = "תפוח אדמה";
        String language = "he";
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
        Map<String, String> result = instance.getMultipleAbstractsByAuthors(authors, language);
        result.keySet().forEach(key -> {
            logger.info("Key: " + key + ", Value: " + result.get(key));
        });
    }
    
}
