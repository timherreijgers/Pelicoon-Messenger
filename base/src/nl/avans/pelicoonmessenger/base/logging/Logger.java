package nl.avans.pelicoonmessenger.base.logging;

public final class Logger {
    private static LoggerAppender appender;

    public static void setAppender(LoggerAppender appender) {
        Logger.appender = appender;
    }

    private String prefix;

    public Logger(String prefix) {
        this.prefix = prefix;
    }

    public enum Level { INFO, WARN, DEBUG, ERROR }

    public void log(Level level, String message) {
        if(Logger.appender == null) return;

        Logger.appender.append(new LoggerMessage.Builder()
            .level(level)
            .prefix(prefix)
            .message(message)
            .thread()
            .timestamp()
            .build());
    }

    public void info(String message) {
        log(Level.INFO, message);
    }

    public void warn(String message) {
        log(Level.WARN, message);
    }

    public void debug(String message) {
        log(Level.DEBUG, message);
    }

    public void error(String message) {
        log(Level.ERROR, message);
    }
}
