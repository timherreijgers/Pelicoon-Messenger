package nl.avans.pelicoonmessenger.client.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import nl.avans.pelicoonmessenger.client.net.Connection;
import nl.avans.pelicoonmessenger.client.view.ClientApplication;

import java.io.IOException;
import java.net.URL;
import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.ResourceBundle;

public class ClientLogin implements Initializable, Controller, Connection.ConnectionListener {

    @FXML
    private TextField ipTextField;

    @FXML
    private TextField usernameTextField;

    @FXML
    private void handleLoginAction(ActionEvent event) {
        boolean isEmpty = false;

        if(ipTextField.getText().equals("")) {
            System.out.println("ipTextField is empty!");
            isEmpty = true;
        }if (usernameTextField.getText().equals("")) {
            System.out.println("usernameTextField is empty");
            isEmpty = true;
        }

        if(!isEmpty) {
            System.out.println("ip: " + ipTextField.getText() + ", username: " + usernameTextField.getText());

            try {
                Connection.getInstance().connect(ipTextField.getText(), usernameTextField.getText());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Connection.getInstance().addConnectionListener(this);
    }

    @Override
    public void onClose() {

    }

    @Override
    public void onConnected() {
        try {
            ClientApplication.getInstance().loadLayout("layouts/client_chat.fxml", "Chat", 1280,
                    720, new ClientChat());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisconnected() {
        // YEA CRAP IDK WHAT TO DO HERE...
    }
}
