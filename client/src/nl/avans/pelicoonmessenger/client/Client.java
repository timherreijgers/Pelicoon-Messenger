package nl.avans.pelicoonmessenger.client;

import nl.avans.pelicoonmessenger.base.models.Message;
import nl.avans.pelicoonmessenger.base.models.Session;
import nl.avans.pelicoonmessenger.base.models.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client extends Thread {

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public Client(String host, int port) {
        try {
            socket = new Socket(host, port);

            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            out.writeObject(new User.Builder()
                .username("TestClient")
                .build());

            Session session = (Session) in.readObject();
            System.out.println("Session: " + session.toString());

            while (socket.isConnected()) {
                try {
                    Object object = in.readObject();

                    if(object instanceof Message) {
                        System.out.println(object.toString());
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(Message message) throws IOException {
        out.writeObject(message);
    }
}
