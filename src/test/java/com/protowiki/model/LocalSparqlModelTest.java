
package com.protowiki.model;

import com.protowiki.beans.Author;
import com.protowiki.utils.DatabaseProperties;
import java.util.List;
import org.junit.After;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Nick
 */
@Ignore
public class LocalSparqlModelTest {
    
    public static Logger logger = LoggerFactory.getLogger(LocalSparqlModelTest.class);
    
    @Rule
    TestName testName = new TestName();
    
    @Before
    public void before() {
        logger.info("Before: " + testName.getMethodName());
    }
    
    @After
    public void after() {
        logger.info("After: " + testName.getMethodName());
    }

    /**
     * Test of getAuthorsWithVIAF method, of class LocalSparqlModel.
     */
    @Test
    public void testGetAuthorsWithVIAF() {
       
        LocalSparqlModel instance = new LocalSparqlModel(new DatabaseProperties("application.properties"));
        List<Author> authors = instance.getAuthorsWithVIAF();
        assertNotNull(authors);
        assertTrue(authors.size() > 0);
    }
    
}
