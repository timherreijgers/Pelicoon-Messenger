package nl.avans.pelicoonmessenger.base.logging;

import java.time.LocalDateTime;

class LoggerMessage {
    private Thread thread;
    private Logger.Level level;
    private LocalDateTime timestamp;
    private String prefix;
    private String message;

    private LoggerMessage(Builder builder) {
        thread = builder.thread;
        level = builder.level;
        timestamp = builder.timestamp;
        prefix = builder.prefix;
        message = builder.message;
    }

    public Thread getThread() {
        return thread;
    }

    public Logger.Level getLevel() {
        return level;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getMessage() {
        return message;
    }

    static class Builder {
        Thread thread;
        Logger.Level level;
        LocalDateTime timestamp;
        String prefix;
        String message;

        Builder() {

        }

        public Builder thread() {
            return thread(Thread.currentThread());
        }

        public Builder thread(Thread thread) {
            this.thread = thread;

            return this;
        }

        public Builder timestamp() {
            return timestamp(LocalDateTime.now());
        }

        public Builder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;

            return this;
        }

        public Builder level(Logger.Level level) {
            this.level = level;

            return this;
        }

        public Builder prefix(String prefix) {
            this.prefix = prefix;

            return this;
        }

        public Builder message(String message) {
            this.message = message;

            return this;
        }

        public LoggerMessage build() {
            return new LoggerMessage(this);
        }
    }
}
