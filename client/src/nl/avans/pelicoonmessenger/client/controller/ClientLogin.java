package nl.avans.pelicoonmessenger.client.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import nl.avans.pelicoonmessenger.client.view.ClientApplication;

public class ClientLogin {

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
                ClientApplication.getInstance().loadLayout("layouts/client_login.fxml", "login2", 1000, 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
