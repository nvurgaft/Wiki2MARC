package com.protowiki.utils;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kobi
 */
@Ignore
public class RDFUtilsTest {

    public static Logger logger = LoggerFactory.getLogger(RDFUtilsTest.class);

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
     * Test of spliceLiteralValue method, of class RDFUtils.
     */
    @Test
    public void testSpliceLiteralValueWithURI() {

        String integerUri = "113230702^^http://www.w3.org/2001/XMLSchema#integer";
        String expResult = "113230702";
        String result = RDFUtils.spliceLiteralType(integerUri);
        assertEquals("Should be equal after uri is spliced", expResult, result);
    }

    /**
     * Test of spliceLiteralValue method, of class RDFUtils.
     */
    @Test
    public void testSpliceLiteralValueWithLiteral() {

        String integerUri = " 113230702  ";
        String expResult = "113230702";
        String result = RDFUtils.spliceLiteralType(integerUri);
        assertEquals("Should be equal if uri is literal", expResult, result);
    }

    /**
     * Test of spliceLiteralValue method, of class RDFUtils.
     */
    @Test
    public void testSpliceLiteralLaguageTag() {

        String abstractText = "He was lauded as the \"greatest American humorist of his age\", and William Faulkner called Twain \"the father of American literature\".\"@en";
        String expResult = "He was lauded as the \"greatest American humorist of his age\", and William Faulkner called Twain \"the father of American literature\".";
        String result = RDFUtils.spliceLiteralLaguageTag(abstractText);
        assertEquals("Should be equal after stripping the @lang tag", expResult, result);
    }

    @Test
    public void testNormalizeMARCName() {

        String name = RDFUtils.normalizeMARCName("Doe, John");
        String expResult = "John Doe";
        assertEquals("Should be equal", name, expResult);
    }

    @Test
    public void testSliceNameFromUrl() {

        String url = "http://dbpedia.org/resource/Douglas_Adams";
        String name = RDFUtils.sliceNameFromUrl(url);
        String expResult = "Douglas_Adams";
        assertEquals("Should be equal", name, expResult);
    }

}
