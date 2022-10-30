package de.utopiamc.time;

public class StopWatchException extends RuntimeException {

    public StopWatchException() {
    }

    public StopWatchException(String message) {
        super(message);
    }

    public StopWatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public StopWatchException(Throwable cause) {
        super(cause);
    }

    public StopWatchException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
