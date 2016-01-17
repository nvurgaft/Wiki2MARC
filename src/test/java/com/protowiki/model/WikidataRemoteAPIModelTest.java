package com.protowiki.model;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Nick
 */
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
    public void testGetWikipediaAbstract() {
        logger.info("getWikipediaAbstract");
        String author = "Mark Twain";
        String language = "en";
        WikidataRemoteAPIModel instance = new WikidataRemoteAPIModel();
        instance.getWikipediaAbstract(author, language);
        
        
    }

    /**
     * Test of getViafFromAuthors method, of class WikidataRemoteAPIModel.
     */
    @Test
    public void testGetAuthorsWithVIAF() {
        logger.info("getViafFromAuthors");
        WikidataRemoteAPIModel instance = new WikidataRemoteAPIModel();
        instance.getAuthorsWithVIAF();
       
    }
    /**
     * Test of getViafFromAuthors method, of class WikidataRemoteAPIModel.
     */
    @Test
    public void testGetAuthorsWithVIAFRemote() {
        logger.info("getViafFromAuthorsRemote");
        WikidataRemoteAPIModel instance = new WikidataRemoteAPIModel();
        instance.getAuthorsWithVIAFRemote();       
    }
    
}
