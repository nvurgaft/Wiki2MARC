package com.protowiki.model;

import java.util.HashMap;
import java.util.Map;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kobi
 */
@Ignore
public class AuthorModelTest {
    
    public static Logger logger = LoggerFactory.getLogger(AuthorModelTest.class);
    
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

    @Test
    public void testClerGraph() {

        AuthorModel instance = new AuthorModel();
        instance.clearGraph();
        // after the graph is cleared, try fetching data from it
        Map<String, String> results = instance.getAuthorsViafAndAbstracts();
        if (results != null) {
            if (results.isEmpty()) {
                logger.info("results list is empty");
            }
            results.keySet().stream().forEach(k -> {
                logger.info(k + " : " + results.get(k));
            });
        } else {
            logger.info("results is null");
        }
    }
    
    @Test
    public void testPurgeGraph() {
        AuthorModel instance = new AuthorModel();
        instance.clearGraph();
    }

    /**
     * Test of insertAuthorsViafAndAbstracts method, of class AuthorModel.
     */
    @Test
    public void testInsertAuthorsViafAndAbstracts() {

        Map<String, String> abstractsMap = new HashMap<>();
        abstractsMap.put("100001", "This is a dummy text");
        abstractsMap.put("100002", "This is also a dummy text");
        abstractsMap.put("100003", "This is the last dummy text");
        AuthorModel instance = new AuthorModel();
        boolean result = instance.insertAuthorsViafAndAbstracts(abstractsMap);
        assertEquals("Should return true if the insertion was successful", result, true);
    }

    /**
     * Test of getAuthorsViafAndAbstracts method, of class AuthorModel.
     */
    @Test
    public void testGetAuthorsViafAndAbstracts() {

        AuthorModel instance = new AuthorModel();
        Map<String, String> results = instance.getAuthorsViafAndAbstracts();
        if (results != null) {
            if (results.isEmpty()) {
                logger.info("results list is empty");
            }
            results.keySet().stream().forEach(k -> {
                logger.info(k + " : " + results.get(k));
            });
        } else {
            logger.info("results is null");
        }
    }

}
