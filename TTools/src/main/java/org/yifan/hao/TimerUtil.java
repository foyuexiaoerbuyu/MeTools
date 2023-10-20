package org.yifan.hao;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TimerUtil {

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    /**
     * Schedules a task to run after the specified delay.
     *
     * @param task  The task to be executed.
     * @param delay The delay in milliseconds before the task is executed.
     */
    public static void schedule(Runnable task, long delay) {
        scheduler.schedule(task, delay, TimeUnit.MILLISECONDS);
    }

    /**
     * Schedules a task to run periodically with the specified interval.
     *
     * @param task         The task to be executed.
     * @param initialDelay The delay in milliseconds before the first execution of the task.
     * @param interval     The interval in milliseconds between subsequent executions of the task.
     */
    public static void scheduleAtFixedRate(Runnable task, long initialDelay, long interval) {
        scheduler.scheduleAtFixedRate(task, initialDelay, interval, TimeUnit.MILLISECONDS);
    }

    /**
     * Shuts down the scheduler, stopping all scheduled tasks.
     */
    public static void shutdown() {
        scheduler.shutdown();
    }
}