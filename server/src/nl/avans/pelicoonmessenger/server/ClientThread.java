package nl.avans.pelicoonmessenger.server;

import nl.avans.pelicoonmessenger.base.models.Message;
import nl.avans.pelicoonmessenger.base.models.Session;
import nl.avans.pelicoonmessenger.base.models.User;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class ClientThread extends Thread {

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private List<OnMessageReceivedListener> messageListeners = new ArrayList<>();

    public ClientThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            System.out.println("[CLIENTTHREAD/" + getId() + "]: Initializing client thread.");

            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            System.out.println("[CLIENTTHREAD/" + getId() + "]: Waiting for user information");
            Object user = in.readObject();
            if (!(user instanceof User)) {
                System.out.println("[CLIENTTHREAD/" + getId() + "]: Client did not authenticate correctly!");
                socket.close();
            }

            System.out.println("[CLIENTTHREAD/" + getId() + "]: Welcoming " + ((User) user).getUsername() + " by sending the session");
            Session session = new Session.Builder()
                    .user((User) user)
                    .ip(socket.getInetAddress().getHostAddress())
                    .build();
            out.writeObject(session);

            System.out.println("[CLIENTTHREAD/" + getId() + "]: Client thread initialized, waiting for messages...");

            System.out.println(socket.getRemoteSocketAddress());

            while (!socket.isClosed()) {
                try {

                    // TODO: Fix this... it's not checking correctly!
                    if(in.read() == -1) {
                        System.err.println("[CLIENTTHREAD/" + getId() + "]: Read -1!! Are we still connected???");
                    }

                    Object object = in.readObject();
                    if (object instanceof Message) {
                        for (OnMessageReceivedListener listener : messageListeners) {
                            listener.onMessageReceived((Message) object);
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void addOnMessageReceivedListener(OnMessageReceivedListener listener) {
        messageListeners.add(listener);
    }

    public void sendMessage(Message message) throws IOException {
        out.writeObject(message);
    }

    public interface OnMessageReceivedListener {
        void onMessageReceived(Message message);
    }
}
