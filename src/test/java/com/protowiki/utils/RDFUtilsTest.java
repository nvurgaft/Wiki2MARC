package com.protowiki.utils;

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
public class RDFUtilsTest {
    
    public static Logger logger = LoggerFactory.getLogger(RDFUtilsTest.class);
    /**
     * Test of spliceLiteralValue method, of class RDFUtils.
     */
    @Test
    public void testSpliceLiteralValueWithURI() {
        logger.info("spliceLiteralValue");
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
        logger.info("spliceLiteralValue");
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
        logger.info("spliceLiteralValue");
        String abstractText = "He was lauded as the \"greatest American humorist of his age\", and William Faulkner called Twain \"the father of American literature\".\"@en";
        String expResult = "He was lauded as the \"greatest American humorist of his age\", and William Faulkner called Twain \"the father of American literature\".";
        String result = RDFUtils.spliceLiteralLaguageTag(abstractText);
        assertEquals("Should be equal after stripping the @lang tag", expResult, result);
    }
    
    @Test
    public void testNormalizeMARCName() {
        logger.info("normalizeMARCName");
        String name = RDFUtils.normalizeMARCName("Doe, John");
        String expResult = "John Doe";
        assertEquals("Should be equal", name, expResult);
    }
    
}
