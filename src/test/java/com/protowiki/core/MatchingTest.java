package com.protowiki.core;

import java.util.Locale;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Nick
 */
@Ignore
public class MatchingTest {

    public static Logger logger = LoggerFactory.getLogger(MatchingTest.class);

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
    public void testLevenshteinDistance() {

        int ld1 = StringUtils.getLevenshteinDistance("The quick brown fox", "The quick green fox");
        int ld2 = StringUtils.getLevenshteinDistance("The quick brown fox", "The quick fox green");

        System.out.println(ld1);
        System.out.println(ld2);
    }

    @Test
    public void testJaroWinklerDistance() {

        double ld1 = StringUtils.getJaroWinklerDistance("The quick brown fox", "The quick green fox");
        double ld2 = StringUtils.getJaroWinklerDistance("The quick brown fox", "The quick brown fox");
        double ld3 = StringUtils.getJaroWinklerDistance("The quick brown fox", "The  quick brown  fox");

        System.out.println(ld1);
        System.out.println(ld2);
        System.out.println(ld3);
    }

    @Test
    public void testJaroWinkerStrain() {
        double ld1 = StringUtils.getJaroWinklerDistance("Michael Joseph Jackson", "Michael Joseph Jackson");
        double ld2 = StringUtils.getJaroWinklerDistance("Michael Joseph Jackson", "Michael Jackson");
        double ld3 = StringUtils.getJaroWinklerDistance("Michael Joseph Jackson", "Jackson Michael ");
        double ld4 = StringUtils.getJaroWinklerDistance("Michael Joseph Jackson", "Jackson, Michael ");
        double ld5 = StringUtils.getJaroWinklerDistance("Michael Joseph Jackson", "Michael J. Jackson");
        double ld6 = StringUtils.getJaroWinklerDistance("Michael Joseph Jackson", "Jackson J. Michael");
        double ld7 = StringUtils.getJaroWinklerDistance("Michael Joseph Jackson", "M. J. Jackson");
        double ld8 = StringUtils.getJaroWinklerDistance("Michael Joseph Jackson", "M. Jackson");

        System.out.println(ld1);
        System.out.println(ld2);
        System.out.println(ld3);
        System.out.println(ld4);
        System.out.println(ld5);
        System.out.println(ld6);
        System.out.println(ld7);
        System.out.println(ld8);
    }
    
    @Test
    public void testStrain() {
        String name1 = "Jackson, Michael";
        String name2 = "Michael Jackson";
        
        double ld1 = StringUtils.getJaroWinklerDistance(name1, name2);
        
        System.out.println(ld1);
    }

    @Test
    public void testFuzzy() {
        int ld1 = StringUtils.getFuzzyDistance("The quick brown fox", "The quick green fox", Locale.ENGLISH);
        int ld2 = StringUtils.getFuzzyDistance("The quick brown fox", "The quick brown fox", Locale.ENGLISH);
        int ld3 = StringUtils.getFuzzyDistance("The quick brown fox", "The  quick brown  fox", Locale.ENGLISH);

        System.out.println(ld1);
        System.out.println(ld2);
        System.out.println(ld3);
    }

}
