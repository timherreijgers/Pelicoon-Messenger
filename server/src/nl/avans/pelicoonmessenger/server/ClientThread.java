package nl.avans.pelicoonmessenger.server;

import nl.avans.pelicoonmessenger.base.models.Message;
import nl.avans.pelicoonmessenger.base.models.Session;
import nl.avans.pelicoonmessenger.base.models.User;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientThread extends Thread {

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private List<ClientListener> listeners = new ArrayList<>();

    private Session session;

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
            session = new Session.Builder()
                    .user((User) user)
                    .ip(socket.getInetAddress().getHostAddress())
                    .build();
            out.writeObject(session);
            for(ClientListener listener : listeners) {
                listener.onAuthenticated(this);
            }

            System.out.println("[CLIENTTHREAD/" + getId() + "]: Client thread initialized, waiting for messages...");

            try {
                while (socket.isConnected()) {
                    Object object = in.readObject();
                    if (object instanceof Message) {
                        for (ClientListener listener : listeners) {
                            listener.onMessageReceived(this, (Message) object);
                        }
                    }
                }
            } catch(EOFException e) {
                e.printStackTrace();
            } finally {
                out.close();
                in.close();
                socket.close();
                for(ClientListener listener : listeners) {
                    listener.onDisconnected(this);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void addClientListener(ClientListener listener) {
        listeners.add(listener);
    }

    public void sendMessage(Message message) throws IOException {
        out.writeObject(message);
    }

    public void sendMessages(List<Message> messages) throws IOException {
        out.writeObject(messages.toArray(new Message[0]));
    }

    public boolean isAuthenticated() {
        return session != null;
    }

    public Session getAuthenticatedSession() {
        return session;
    }

    public interface ClientListener {
        void onAuthenticated(ClientThread client);

        void onMessageReceived(ClientThread client, Message message);

        void onDisconnected(ClientThread client);
    }
}
