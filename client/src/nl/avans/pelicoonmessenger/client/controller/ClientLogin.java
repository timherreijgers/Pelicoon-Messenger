package nl.avans.pelicoonmessenger.client.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import nl.avans.pelicoonmessenger.client.view.ClientApplication;

import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.ResourceBundle;

public class ClientLogin implements Controller{

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
                ClientApplication.getInstance().loadLayout("layouts/client_chat.fxml", "Chat", 1280,
                        720, new ClientChat(ipTextField.getText(), usernameTextField.getText()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClose() {

    }
}
