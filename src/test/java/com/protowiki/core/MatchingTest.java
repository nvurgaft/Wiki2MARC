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
        logger.info("before: {}", testName.getMethodName());
    }

    @After
    public void after() {
        logger.info("after: {}", testName.getMethodName());
    }

    @Test
    public void testLevenshteinDistance() {
        logger.info("{}", StringUtils.getLevenshteinDistance("The quick brown fox", "The quick green fox"));
        logger.info("{}", StringUtils.getLevenshteinDistance("The quick brown fox", "The quick fox green"));
    }

    @Test
    public void testJaroWinklerDistance() {

        logger.info("{}", StringUtils.getJaroWinklerDistance("The quick brown fox", "The quick green fox"));
        logger.info("{}", StringUtils.getJaroWinklerDistance("The quick brown fox", "The quick brown fox"));
        logger.info("{}", StringUtils.getJaroWinklerDistance("The quick brown fox", "The  quick brown  fox"));
    }

    @Test
    public void testJaroWinkerStrain() {
        logger.info("{}", StringUtils.getJaroWinklerDistance("Michael Joseph Jackson", "Michael Joseph Jackson"));
        logger.info("{}", StringUtils.getJaroWinklerDistance("Michael Joseph Jackson", "Michael Jackson"));
        logger.info("{}", StringUtils.getJaroWinklerDistance("Michael Joseph Jackson", "Jackson Michael "));
        logger.info("{}", StringUtils.getJaroWinklerDistance("Michael Joseph Jackson", "Jackson, Michael "));
        logger.info("{}", StringUtils.getJaroWinklerDistance("Michael Joseph Jackson", "Michael J. Jackson"));
        logger.info("{}", StringUtils.getJaroWinklerDistance("Michael Joseph Jackson", "Jackson J. Michael"));
        logger.info("{}", StringUtils.getJaroWinklerDistance("Michael Joseph Jackson", "M. J. Jackson"));
        logger.info("{}", StringUtils.getJaroWinklerDistance("Michael Joseph Jackson", "M. Jackson"));
    }

    @Test
    public void testStrain() {
        String name1 = "Jackson, Michael";
        String name2 = "Michael Jackson";

        logger.info("{}", StringUtils.getJaroWinklerDistance(name1, name2));
    }

    @Test
    public void testFuzzy() {
        logger.info("{}", StringUtils.getFuzzyDistance("The quick brown fox", "The quick green fox", Locale.ENGLISH));
        logger.info("{}", StringUtils.getFuzzyDistance("The quick brown fox", "The quick brown fox", Locale.ENGLISH));
        logger.info("{}", StringUtils.getFuzzyDistance("The quick brown fox", "The  quick brown  fox", Locale.ENGLISH));
    }

}
