package com.protowiki.app;

import com.protowiki.utils.ApplicationProperties;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The JobExecutorServiceis a global (static class) that provides an async
 * executor for the application, it has basic configuration support using a
 * properties file and has basic job validation.
 *
 * @author Nick
 */
public class JobExecutorService {

    public static Logger logger = LoggerFactory.getLogger(JobExecutorService.class);

    private static ExecutorService executor = null;

    protected static ApplicationProperties dbProperties;

    private static void init() {
        int threadPoolSize = 10;
        try {
            threadPoolSize = dbProperties.getInt("executor_threadpool_size", 10);
        } catch (NullPointerException npe) {
            logger.error("Could not read properties file", npe.getMessage());
        }
        executor = Executors.newFixedThreadPool(threadPoolSize);
    }

    /**
     * Submit a single job to the executor
     *
     * @param task
     * @return a Future for that task
     */
    public static Future<?> submitJob(Callable<?> task) {
        if (task == null) {
            return null;
        }

        if (executor == null) {
            synchronized (JobExecutorService.class) {
                if (executor == null) {
                    init();
                }
            }
        }
        return executor.submit(task);
    }

    /**
     * Submit multiple jobs to the executor
     *
     * @param tasks
     * @return a list of Futures for that task
     */
    public static List<Future<?>> submitMultipleJobs(List<Callable<?>> tasks) {

        if (tasks == null) {
            return null;
        }
        if (executor == null) {
            synchronized (JobExecutorService.class) {
                if (executor == null) {
                    init();
                }
            }
        }

        return tasks.stream()
                .filter(task -> {
                    return task != null;
                })
                .map(task -> {
                    return executor.submit(task);
                })
                .collect(Collectors.toList());
    }

    /**
     * Waits for all jobs to finish and shuts down the executor
     */
    public static void shutdownExecutor() {
        if (executor != null) {
            executor.shutdown();
        }
    }

    /**
     * Attempts to interrupt all executing jobs and shut down the executor as
     * soon as possible.
     */
    public static void killExecutor() {
        if (executor != null) {
            executor.shutdownNow();
        }
    }

}
