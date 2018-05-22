package nl.avans.pelicoonmessenger.client;

import nl.avans.pelicoonmessenger.base.models.Message;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        Client client = new Client("localhost", 1337);
        //new Thread(client).start();
        client.start();

        try {
            client.sendMessage(new Message.Builder()
                .id(0)
                .timestamp()
                .message("Hello")
                .build());
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Client started!");
    }

}
