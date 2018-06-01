package nl.avans.pelicoonmessenger.client.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import nl.avans.pelicoonmessenger.base.models.Message;
import nl.avans.pelicoonmessenger.base.models.Session;
import nl.avans.pelicoonmessenger.base.models.User;
import nl.avans.pelicoonmessenger.client.net.Connection;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class ClientChat implements Controller, Initializable, Connection.MessageReceivedListener {

    @FXML
    private TextField mainMessageArea;

    @FXML
    private TextArea mainTextArea;

    @FXML
    private Label connectToLabel;

    private Connection connection;
    private User user;

    public ClientChat(String ip, String username) {
        try {
            user = new User.Builder()
                    .username(username)
                    .build();
            connection = new Connection(ip, user);
            connection.setMessageReceivedListener(this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void handleMessageSendEvent(ActionEvent event) {
        String message = mainMessageArea.getText();
        connection.sendMessage(new Message.Builder()
                .message(message)
                .timestamp(LocalDateTime.now())
                .user(user)
                .build());
        mainMessageArea.setText("");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mainMessageArea.setOnAction(event -> handleMessageSendEvent(event));
        connectToLabel.setText("Connected to: " + connection.getIp());

    }

    @FXML
    private void handleCloseEvent(ActionEvent event){

    }

    @Override
    public void onMessageReceived(Message message) {
        System.out.println("Message received!");
        mainTextArea.appendText(message.getUser().getUsername() + ": " +message.getMessage() + "\n");
    }

    @Override
    public void onClose() {
        connection.stopConnection();
        Platform.exit();
    }
}
