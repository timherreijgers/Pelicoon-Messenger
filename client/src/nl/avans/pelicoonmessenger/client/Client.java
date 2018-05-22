package nl.avans.pelicoonmessenger.client;

import nl.avans.pelicoonmessenger.base.models.Message;
import nl.avans.pelicoonmessenger.base.models.Session;
import nl.avans.pelicoonmessenger.base.models.User;
import nl.avans.pelicoonmessenger.base.utils.ArrayUtilities;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Client extends Thread {

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private List<Message> messages = new ArrayList<>();

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
                        messages.add((Message) object);
                    }

                    if(object instanceof Message[]) {
                        messages.addAll(Arrays.asList((Message[])object));
                        System.out.println("Received a message history with, our history now contains " + messages.size() + " messages.");
                        System.out.println(Arrays.asList((Message[])object));
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
