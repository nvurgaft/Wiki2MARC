package com.protowiki.app;

import com.protowiki.utils.DatabaseProperties;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Nick
 */
public class JobExecutor {

    public static Logger logger = LoggerFactory.getLogger(JobExecutor.class);

    private static final JobExecutor jobExecutor = new JobExecutor();
    private ExecutorService executor;

    protected DatabaseProperties dbProperties;

    private JobExecutor() {
        if (jobExecutor != null) {
            throw new IllegalStateException("Already instantiated");
        }
        
        int threadPoolSize = dbProperties.getInt("executor_threadpool_size", 10);

        executor = Executors.newFixedThreadPool(threadPoolSize);
    }

    public static JobExecutor getInstance() {
        return jobExecutor;
    }

    public Future<?> submitJob(Callable<?> task) {
        return executor.submit(task);
    }

    public List<Future<?>> submitMultipleJobs(List<Callable<?>> tasks) {

        if (tasks != null) {
            return tasks.stream()
                    .filter(task -> {
                        return task != null;
                    })
                    .map(task -> {
                        return executor.submit(task);
                    })
                    .collect(Collectors.toList());

        } else {
            return null;
        }
    }

}
