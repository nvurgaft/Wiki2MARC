package com.protowiki.model;

import com.protowiki.entities.RDFStatement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kobi
 */
@Ignore
public class AuthorModelTest {
    
    public static Logger logger = LoggerFactory.getLogger(AuthorModelTest.class);

    public AuthorModelTest() {
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
    
    @Test
    public void testClerGraph() {
        logger.info("insertAuthorsViafAndAbstracts");
        AuthorModel instance = new AuthorModel();
        instance.clearGraph();
        // after the graph is cleared, try fetching data from it
        List<RDFStatement> results = instance.getAuthorsViafAndAbstracts();
        if (results != null) {
            if (results.isEmpty()) {
                System.out.println("results list is empty");
            }
            results.stream().forEach(r -> {
                System.out.println(r);
            });
        } else {
            System.out.println("results is null");
        }
    }
    
    @Test
    public void testPurgeGraph() {
        AuthorModel instance = new AuthorModel();
        
    }

    /**
     * Test of insertAuthorsViafAndAbstracts method, of class AuthorModel.
     */
    @Test
    public void testInsertAuthorsViafAndAbstracts() {
        logger.info("insertAuthorsViafAndAbstracts");
        Map<String, String> abstractsMap = new HashMap<>();
        abstractsMap.put("100001", "This is a dummy text");
        abstractsMap.put("100002", "This is also a dummy text");
        abstractsMap.put("100003", "This is the last dummy text");
        AuthorModel instance = new AuthorModel();
        boolean result = instance.insertAuthorsViafAndAbstracts(abstractsMap);
        assertEquals("Should be equals if the insertion was successful", result, true);
    }

    /**
     * Test of getAuthorsViafAndAbstracts method, of class AuthorModel.
     */
    @Test
    public void testGetAuthorsViafAndAbstracts() {
        logger.info("getAuthorsViafAndAbstracts");
        AuthorModel instance = new AuthorModel();
        List<RDFStatement> results = instance.getAuthorsViafAndAbstracts();
        if (results != null) {
            if (results.isEmpty()) {
                System.out.println("results list is empty");
            }
            results.stream().forEach(r -> {
                System.out.println(r);
            });
        } else {
            System.out.println("results is null");
        }
    }

}
