package nl.avans.pelicoonmessenger.client.controller;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientChat implements Controller, Initializable {

    public ClientChat(String ip, String username) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Initialize called");
    }
}
