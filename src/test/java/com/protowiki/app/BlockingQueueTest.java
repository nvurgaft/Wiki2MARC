/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.protowiki.app;

import java.util.concurrent.Callable;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Nick
 */
public class BlockingQueueTest {

    public static Logger logger = LoggerFactory.getLogger(BlockingQueueTest.class);

    @Rule
    public TestName testName = new TestName();

    public BlockingQueueTest() {
    }

    @Before
    public void setUp() {
        logger.debug("before: {}", testName.getMethodName());
    }

    @After
    public void tearDown() {
        logger.debug("after: {}", testName.getMethodName());
    }

    protected Callable<String> doJob() {
        return () -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
                logger.error("InterruptedException", ex);
            }
            return String.valueOf(System.currentTimeMillis());
        };
    }
}
