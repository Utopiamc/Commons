package de.utopiamc.time;

import org.apache.log4j.Logger;
import org.apiguardian.api.API;

/**
 * <h1>StopWatch</h1>
 * <p>
 * Should be used to log tasks with their used time. Tasks should be chained together.
 * <p>
 * E.g:
 * <pre>
 *     StopWatch watch = new StopWatch(logger);
 *     watch.start();
 *
 *     // Task
 *
 *     watch.stopAndLog("Finished doing task.") // -> Console: Finished doing task. 20ms
 *     watch.start();
 *
 *     // Next Task
 *
 *     watch.stopAndLog("Finished doing next task."); // -> Console: Finished doing next task. 34ms
 *
 * </pre>
 */
@API(status = API.Status.STABLE, since = "2")
public final class StopWatch {

    public static final String ALREADY_STARTED_MSG = "Tried to start stopwatch, but it was already started!";
    public static final String NOT_STARTED_MSG = "Tried to stop stopwatch, it it wasn't started!";
    private Long start;
    private final Logger logger;

    public StopWatch(Logger logger) {
        this.logger = logger;
    }

    /**
     * Start the watch. This saves the {@link System#currentTimeMillis()}.
     * @throws StopWatchException When the stopwatch is already started.
     */
    public void start() {
        if (start != null) {
            throw new StopWatchException(ALREADY_STARTED_MSG);
        }
        start = System.currentTimeMillis();
    }

    /**
     * Stops the watch and logs the message with the performed time in {@code ms}.
     * @param msg Message that should be logged.
     * @throws StopWatchException When the stopwatch is not started.
     */
    public void stopAndLog(String msg) {
        if (start == null) {
            throw new StopWatchException(NOT_STARTED_MSG);
        }
        long ms = System.currentTimeMillis() - start;
        this.start = null;

        logger.info(String.format(msg + " %sms", ms));
    }

}
