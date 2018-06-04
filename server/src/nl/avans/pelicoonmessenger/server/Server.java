package nl.avans.pelicoonmessenger.server;

import nl.avans.pelicoonmessenger.base.logging.Logger;
import nl.avans.pelicoonmessenger.base.models.Lobby;
import nl.avans.pelicoonmessenger.base.models.Message;
import nl.avans.pelicoonmessenger.base.models.Session;
import nl.avans.pelicoonmessenger.server.net.ClientConnection;
import nl.avans.pelicoonmessenger.server.net.PacketHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server implements ClientConnection.ConnectionListener, PacketHandler.PacketListener {

    private static Server instance;

    public static Server getInstance() {
        if(instance == null) instance = new Server();
        return instance;
    }

    private Logger logger = new Logger(getClass().getSimpleName());

    private boolean running = false;
    private Thread serverThread;
    private ServerSocket serverSocket;

    private List<ClientConnection> clients = new ArrayList<>();

    private Map<String, Lobby> lobbies = new HashMap<>();


    private List<EventListener> listeners = new ArrayList<>();

    private List<Message> messages = new ArrayList<>();

    private Server() {
    }

    public void start() {
        running = true;

        serverThread = new Thread(() -> {
            logger.info("Initializing server...");

            try {
                serverSocket = new ServerSocket(1337);
                running = true;

                logger.info("Server is listening for clients at port " + serverSocket.getLocalPort());

                for(EventListener listener : listeners) {
                    listener.onStarted(this);
                }

                while (running) {
                    ClientConnection client = new ClientConnection(serverSocket.accept(), this);
                    client.getHandler().addPacketListener(this);
                    client.start();
                    logger.info("New client connected");
                }
            } catch (IOException e) {
                logger.printStackTrace(e);
                running = false;
            }
        });

        serverThread.setName("Server Thread");
        serverThread.start();
    }

    public void stop() {
        //if(running) {
            try {
                running = false;
                serverSocket.close();
                serverThread.interrupt();
                logger.info("Server closed");

                for(EventListener listener : listeners) {
                    listener.onStopped(this);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        //}
    }

    public boolean isRunning() {
        return running;
    }

    public List<ClientConnection> getClients() {
        return clients;
    }

    public void addEventListener(EventListener listener) {
        listeners.add(listener);
    }

    public void removeEventListener(EventListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void onConnected(ClientConnection client) {
        clients.add(client);
        for(EventListener listener : listeners) {
            listener.onClientConnected(client);
        }
    }

    @Override
    public void onDisconnected(ClientConnection client) {
        clients.remove(client);
        for(EventListener listener : listeners) {
            listener.onClientDisconnected(client);
        }
        logger.debug("Client disconnected, total connected clients: " + clients.size());
    }

    @Override
    public void onAuthenticated(ClientConnection client, Session session) {
        for(EventListener listener : listeners) {
            listener.onClientAuthenticated(client);
        }
        client.getHandler().sendMessages(messages);
    }

    @Override
    public void onMessageReceived(Message message) {
        for(ClientConnection client : clients) {
            client.getHandler().sendMessage(message);
        }
        messages.add(message);
    }


    public interface EventListener {
        void onStarted(Server server);
        void onStopped(Server server);

        void onClientConnected(ClientConnection client);
        void onClientAuthenticated(ClientConnection client);
        void onClientDisconnected(ClientConnection client);
    }
}
