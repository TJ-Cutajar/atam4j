package me.atam.atam4j;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import me.atam.atam4j.health.AcceptanceTestsState;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AcceptanceTestsRunnerTaskScheduler {

    private final Class[] testClasses;
    private final long initialDelay;
    private final long period;
    private final TimeUnit unit;
    private final boolean runOnce;
    private final ScheduledExecutorService scheduler;
    private final TestRunListener testRunListener;

    public AcceptanceTestsRunnerTaskScheduler(final Class[] testClasses,
                                              final long initialDelay,
                                              final long period,
                                              final TimeUnit unit,
                                              final boolean runOnce,
                                              final TestRunListener testRunListener) {
        this.testClasses = testClasses;
        this.initialDelay = initialDelay;
        this.period = period;
        this.unit = unit;
        this.runOnce = runOnce;
        this.testRunListener = testRunListener;
        this.scheduler = Executors.newSingleThreadScheduledExecutor(
                new ThreadFactoryBuilder()
                        .setNameFormat("acceptance-tests-runner")
                        .setDaemon(false)
                        .build());
    }

    public void scheduleAcceptanceTestsRunnerTask(final AcceptanceTestsState acceptanceTestsState) {
        final AcceptanceTestsRunnerTask task = new AcceptanceTestsRunnerTask(acceptanceTestsState, testRunListener, testClasses);
        if (runOnce) {
            scheduler.schedule(task, initialDelay, unit);
        } else {
            scheduler.scheduleAtFixedRate(task, initialDelay, period, unit);
        }
    }
}