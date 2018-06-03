package nl.avans.pelicoonmessenger.base.logging;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

public abstract class LoggerAppender {
    private TextArea console;

    public LoggerAppender(TextArea console) {
        this.console = console;
    }

    public void append(LoggerMessage message) {
        if(console == null) return;

        Platform.runLater(() -> {
            console.appendText(format(message));
            console.setScrollTop(Double.MAX_VALUE);
        });
    }

    public abstract String format(LoggerMessage message);

    public static class BasicLoggerAppender extends LoggerAppender {

        private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        public BasicLoggerAppender(TextArea console) {
            super(console);
        }

        @Override
        public String format(LoggerMessage message) {
            return "[" + message.getTimestamp().format(formatter)
                    + "] [" + message.getThread().getName() + "/" + message.getLevel() + "] [" + message.getPrefix() + "]: "
                    + message.getMessage() + "\n";
        }
    }
}
