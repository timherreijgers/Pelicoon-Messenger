package nl.avans.pelicoonmessenger.server;

import nl.avans.pelicoonmessenger.base.logging.Logger;
import nl.avans.pelicoonmessenger.base.models.Message;
import nl.avans.pelicoonmessenger.server.controllers.ServerConsole;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread implements ClientThread.ClientListener {

    private static Server instance;

    public static Server getInstance() {
        if(instance == null) instance = new Server();
        return instance;
    }

    private Logger logger = new Logger("Server");

    private boolean running = false;
    private ServerSocket serverSocket;

    private List<ClientThread> clients = new ArrayList<>();
    private List<Message> messages = new ArrayList<>();

    private Server() {
        setName("Server Thread");
    }

    @Override
    public void run() {
        logger.info("Initializing server...");

        try {
            serverSocket = new ServerSocket(1337);
            running = true;

            logger.info("Server is listening for clients at port " + serverSocket.getLocalPort());

            while (running) {
                ClientThread clientThread = new ClientThread(serverSocket.accept());
                clientThread.addClientListener(this);
                clientThread.start();
                clients.add(clientThread);
                logger.info("New client connected");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
    }

//    public void stop() {
//        if(serverSocket != null && !serverSocket.isClosed()) {
//            try {
//                running = false;
//                serverSocket.close();
//                serverSocket = null;
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    @Override
    public void onAuthenticated(ClientThread client) {
        logger.info("Client authenticated, sending message history");
        try {
            client.sendMessages(messages);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessageReceived(ClientThread client, Message message) {
        logger.debug("Received message " + message.toString());

        try {
            if (client.isAuthenticated()) {
                messages.add(message);
                for (ClientThread subClient : clients) {
                    subClient.sendMessage(message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisconnected(ClientThread client) {
        clients.remove(client);
        logger.debug("Client disconnected, total connected clients: " + clients.size());
    }
}
