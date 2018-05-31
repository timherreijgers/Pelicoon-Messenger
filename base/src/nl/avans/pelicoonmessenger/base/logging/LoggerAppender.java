package nl.avans.pelicoonmessenger.base.logging;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.text.SimpleDateFormat;
import java.util.Date;


public abstract class LoggerAppender {
    private TextArea console;

    public LoggerAppender(TextArea console) {
        this.console = console;
    }

    public void append(LoggerMessage message) {
        if(this.console == null) return;

        Platform.runLater(() -> {
            this.console.appendText(format(message));
        });
    }

    public abstract String format(LoggerMessage message);

    public static class BasicLoggerAppender extends LoggerAppender {

        private SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm:ss");

        public BasicLoggerAppender(TextArea console) {
            super(console);
        }

        @Override
        public String format(LoggerMessage message) {
            return dateFormatter.format(message.getTimestamp())
                    + " [" + message.getThread().getName() + "/" + message.getLevel() + "] [" + message.getPrefix() + "]: "
                    + message.getMessage() + "\n";
        }
    }
}
