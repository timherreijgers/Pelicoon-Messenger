package nl.avans.pelicoonmessenger.server.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.text.TextFlow;
import nl.avans.pelicoonmessenger.base.logging.Logger;
import nl.avans.pelicoonmessenger.base.logging.LoggerAppender;
import nl.avans.pelicoonmessenger.server.Server;
import nl.avans.pelicoonmessenger.server.net.ClientConnection;

import java.net.URL;
import java.util.ResourceBundle;

public class ServerConsole implements Initializable, Server.EventListener {

    @FXML
    private TextArea clients;

    @FXML
    private TextArea console;

    @FXML
    private Button startStopButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Logger.setAppender(new LoggerAppender.BasicLoggerAppender(console));
        Server.getInstance().addEventListener(this);
    }

    @FXML
    private void handleButtonClick(ActionEvent event) {
        Server server = Server.getInstance();

        if(server.isRunning()) {
            server.stop();
        } else {
            server.start();
        }
    }

    @Override
    public void onStarted(Server server) {
        Platform.runLater(() -> startStopButton.setText("Stop"));
    }

    @Override
    public void onStopped(Server server) {
        Platform.runLater(() -> startStopButton.setText("Start"));
    }

    @Override
    public void onClientConnected(ClientConnection client) {
        clients.appendText("Test");
    }

    @Override
    public void onClientDisconnected(ClientConnection client) {

    }
}
