package nl.avans.pelicoonmessenger.client.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import nl.avans.pelicoonmessenger.base.models.Message;
import nl.avans.pelicoonmessenger.client.net.Connection;
import nl.avans.pelicoonmessenger.client.net.PacketHandler;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ClientChat implements Controller, Initializable, PacketHandler.MessageReceivedListener {

    @FXML
    private TextField mainMessageArea;

    @FXML
    private TextArea mainTextArea;

    @FXML
    private Label connectToLabel;

    private Connection connection = Connection.getInstance();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @FXML
    private void handleMessageSendEvent(ActionEvent event) {
        try {
            connection.getHandler().sendMessage(mainMessageArea.getText());
            mainMessageArea.setText("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        connection.getHandler().setMessageReceivedListener(this);

        mainMessageArea.setOnAction(event -> handleMessageSendEvent(event));
        connectToLabel.setText("Connected to: " + connection.getHostAddress());
    }

    @FXML
    private void handleCloseEvent(ActionEvent event){

    }

    @Override
    public void onMessageReceived(Message message) {
        System.out.println("Message received: " + message);
        mainTextArea.appendText(message.getTimestamp().format(formatter) + "  " + message.getUser().getUsername() + ": " +message.getMessage() + "\n");
    }

    @Override
    public void onClose() {
        connection.stopConnection();
        Platform.exit();
    }
}
