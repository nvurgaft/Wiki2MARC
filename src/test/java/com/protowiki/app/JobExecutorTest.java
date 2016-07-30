package com.protowiki.app;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Nick
 */
//@Ignore
public class JobExecutorTest {

    public static Logger logger = LoggerFactory.getLogger(JobExecutorTest.class);

    @Rule
    public TestName testName = new TestName();

    public JobExecutorTest() {
    }

    @Before
    public void setUp() {
        logger.debug("before: " + testName.getMethodName());
    }

    @After
    public void tearDown() {
        logger.debug("after: " + testName.getMethodName());
    }

    /**
     * Test of getInstance method, of class JobExecutor.
     */
    @Test
    public void testGetInstance() {

        JobExecutor instance = JobExecutor.getInstance();
        Assert.assertNotNull("Instance should not be null", instance);
    }

    /**
     * Test of submitJob method, of class JobExecutor.
     */
    @Test
    public void testSubmitJob() {

        Callable<String> task = (Callable<String>) () -> {
            return "test callback";
        };

        JobExecutor instance = JobExecutor.getInstance();

        Future<?> future = instance.submitJob(task);
        String result = null;
        try {
            result = (String) future.get();
        } catch (InterruptedException | ExecutionException ex) {
            logger.error("Exception", ex);
        }
        logger.info("result: " + result);
    }

    /**
     * Test of submitMultipleJobs method, of class JobExecutor.
     */
    @Test
    public void testSubmitMultipleJobs() {

        List<Callable<?>> tasks = batchJobs();
        JobExecutor instance = JobExecutor.getInstance();

        List<Future<?>> result = instance.submitMultipleJobs(tasks);
        result.stream().forEach(future -> {
            try {
                String str = (String) future.get();
                logger.info("output: " + str);
            } catch (InterruptedException | ExecutionException ex) {
                logger.error("Exception", ex);
            }
        });

    }

    public List<Callable<?>> batchJobs() {

        return IntStream.range(0, 50).boxed().map(i -> {
            return (Callable<String>) () -> {
                TimeUnit.SECONDS.sleep(3);
                return "job " + i + "  has finished";
            };
        }).collect(Collectors.toList());
    }

}
