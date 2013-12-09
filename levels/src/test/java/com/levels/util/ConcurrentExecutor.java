package com.levels.util;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

/**
 * Concurrent executor for timing and testing concurrent thread executions over
 * a given action
 * 
 * @author adarrivi
 * 
 */
public class ConcurrentExecutor {

    private ExecutorService executor;
    private int concurrency;
    private Runnable action;

    public ConcurrentExecutor(ExecutorService executor, int concurrency, Runnable action) {
        this.executor = executor;
        this.concurrency = concurrency;
        this.action = action;
    }

    public long verifyConcurrentExecution() {
        try {
            return executeActionConcurrently();
        } catch (InterruptedException ex) {
            throw new AssertionError(ex);
        }
    }

    private long executeActionConcurrently() throws InterruptedException {
        final CountDownLatch ready = new CountDownLatch(concurrency);
        final CountDownLatch start = new CountDownLatch(1);
        final CountDownLatch done = new CountDownLatch(concurrency);
        for (int i = 0; i < concurrency; i++) {
            // TODO How to get the Exception thrown? Using submit doesn't look
            // like executing them at the same time?
            // http://stackoverflow.com/questions/1687977/how-to-properly-catch-runtimeexceptions-from-executors
            executor.execute(new Runnable() {

                @Override
                public void run() {
                    ready.countDown();
                    try {
                        start.await();
                        action.run();
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    } finally {
                        done.countDown();
                    }

                }
            });
        }
        ready.await();
        long startNanos = System.nanoTime();
        start.countDown();
        done.await();
        return System.nanoTime() - startNanos;
    }

}
