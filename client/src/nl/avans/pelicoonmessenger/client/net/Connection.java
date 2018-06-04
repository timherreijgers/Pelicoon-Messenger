package nl.avans.pelicoonmessenger.client.net;

import nl.avans.pelicoonmessenger.base.models.Message;
import nl.avans.pelicoonmessenger.base.models.Session;
import nl.avans.pelicoonmessenger.base.net.packets.MessagePacket;
import nl.avans.pelicoonmessenger.base.net.packets.Packet;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class Connection extends Thread {

    private static Connection instance;

    public static Connection getInstance() {
        if(instance == null) instance = new Connection();
        return instance;
    }

    private Socket socket;
    ObjectOutputStream outputStream;
    ObjectInputStream inputStream;
    private PacketHandler handler;

    private MessageReceivedListener listener;
    private boolean running = true;

    Session session;

    private List<ConnectionListener> connectionListeners = new ArrayList<>();

    private Connection() { }

    public void connect(String ip, String username) throws IOException {
        socket = new Socket(ip, 1337);
        handler = new PacketHandler(this, username);
        handler.start();

        for(ConnectionListener listener : connectionListeners) {
            listener.onConnected();
        }

        start();
    }

    public void setMessageReceivedListener(MessageReceivedListener listener) {
        this.listener = listener;
    }

    @Override
    public void run() {
        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());

            try {
                while (running) {
                    Object packet = inputStream.readObject();

                    if(packet instanceof Packet) {
                        handler.processPacket((Packet) packet);
                    }

//                    if (object instanceof Message[])
//                        if (listener != null)
//                            for (Message message : (Message[]) object) {
//                                listener.onMessageReceived(message);
//                            }
//                    if (object instanceof Message) {
//                        if (listener != null)
//                            listener.onMessageReceived((Message) object);
//                    }
                }
            } catch (EOFException e) {
                e.printStackTrace();
                outputStream.close();
                inputStream.close();
                socket.close();
                running = false;

                for(ConnectionListener listener : connectionListeners) {
                    listener.onDisconnected();
                }
            } catch (SocketException e) {
                interrupt();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void stopConnection(){
        try {
            running = false;
            socket.close();
            handler.interrupt();
            interrupt();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public String getHostAddress(){
        return socket.getInetAddress().getHostAddress();
    }

    public PacketHandler getHandler() {
        return handler;
    }

    public boolean isAuthenticated() {
        return session != null;
    }

    public void addConnectionListener(ConnectionListener listener) {
        connectionListeners.add(listener);
    }

    public interface MessageReceivedListener{
        void onMessageReceived(Message message);
    }

    public interface ConnectionListener {
        void onConnected();
        void onDisconnected();
    }
}
