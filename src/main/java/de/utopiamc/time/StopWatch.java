package de.utopiamc.time;

import org.apache.log4j.Logger;

public final class StopWatch {

    public static final String ALREADY_STARTED_MSG = "Tried to start stopwatch, but it was already started!";
    public static final String NOT_STARTED_MSG = "Tried to stop stopwatch, it it wasn't started!";
    private Long start;
    private final Logger logger;

    public StopWatch(Logger logger) {
        this.logger = logger;
    }

    public void start() {
        if (start != null) {
            throw new StopWatchException(ALREADY_STARTED_MSG);
        }
        start = System.currentTimeMillis();
    }

    public void stopAndLog(String msg) {
        if (start == null) {
            throw new StopWatchException(NOT_STARTED_MSG);
        }
        long ms = System.currentTimeMillis() - start;
        this.start = null;

        logger.info(String.format(msg + " %sms", ms));
    }

}
