package com.protowiki.app;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 *
 * @author Nick
 */
public class JobExecutor {

    private static final JobExecutor jobExecutor = new JobExecutor();
    private ExecutorService executor;

    private JobExecutor() {
        if (jobExecutor != null) {
            throw new IllegalStateException("Already instantiated");
        }

        executor = Executors.newFixedThreadPool(4);
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
