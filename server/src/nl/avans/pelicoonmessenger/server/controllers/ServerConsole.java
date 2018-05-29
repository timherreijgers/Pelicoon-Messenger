package nl.avans.pelicoonmessenger.server.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import nl.avans.pelicoonmessenger.base.logging.Logger;
import nl.avans.pelicoonmessenger.base.logging.LoggerAppender;

public class ServerConsole {

    @FXML
    private TextArea console;

    public void initialize() {
        Logger.setAppender(new LoggerAppender.BasicLoggerAppender(console));
    }
}
