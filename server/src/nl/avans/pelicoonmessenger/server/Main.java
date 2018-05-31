package nl.avans.pelicoonmessenger.server;

import nl.avans.pelicoonmessenger.base.models.Message;
import nl.avans.pelicoonmessenger.base.models.Session;
import nl.avans.pelicoonmessenger.base.models.User;

public class Main {

    public static void main(String[] args) {
        //System.out.println("Server started!");
        //System.out.println("Server stopped!");

        User user = new User.Builder()
                .username("Pelicoon")
                .build();

        Session session = new Session.Builder()
                .ip("127.0.0.1")
                .user(user)
                .build();

        Message message = new Message.Builder()
                .id(0)
                .user(user)
                .message("A message")
                .timestamp()
                .build();

        Server server = Server.getInstance();
        server.start();
    }

}
