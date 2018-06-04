package nl.avans.pelicoonmessenger.server.net;

import nl.avans.pelicoonmessenger.base.logging.Logger;
import nl.avans.pelicoonmessenger.base.models.Session;
import nl.avans.pelicoonmessenger.base.net.packets.*;

import java.io.*;
import java.net.Socket;
import java.util.Comparator;
import java.util.UUID;

public class ClientConnection extends Thread {

    private Logger logger = new Logger(getClass().getSimpleName());

    private Socket socket;
    private PacketHandler handler = new PacketHandler(this);
    private UUID token = UUID.randomUUID();

    private ConnectionListener listener;

    Session session;
    ObjectOutputStream out;
    ObjectInputStream in;

    public ClientConnection(Socket socket, ConnectionListener listener) {
        this.socket = socket;
        this.listener = listener;
        setName("Client Thread-" + getId());
    }

    @Override
    public synchronized void start() {
        super.start();
        handler.start();
    }

    @Override
    public void run() {
        try {
            logger.info("Initializing client thread.");

            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            logger.debug("Sending token packet");
            handler.queuePacket(new TokenPacket(token));

            logger.info("Client thread initialized, waiting for messages...");
            listener.onConnected(this);

            try {
                while (socket.isConnected()) {
                    Object packet = in.readObject();
                    if (packet instanceof Packet && handler.validatePacket((Packet) packet)) {
                        handler.processPacket((Packet) packet);
                    } else {
                        logger.warn("Client send an invalid packet, ignoring the packet");
                    }
                }
            } catch(EOFException e) {
                logger.printStackTrace(e);
            } finally {
                out.close();
                in.close();
                socket.close();
                handler.interrupt();
                listener.onDisconnected(this);
            }
        } catch (IOException | ClassNotFoundException e) {
            logger.printStackTrace(e);
        }
    }

    public void disconnect() {
        try {
            socket.close();
        } catch (IOException e) {
            logger.printStackTrace(e);
        }
    }


    public boolean isAuthenticated() {
        return session != null;
    }

    public Session getAuthenticatedSession() {
        return session;
    }

    public UUID getToken() {
        return token;
    }

    public String getHostAddress() {
        return socket.getInetAddress().getHostAddress();
    }

    public PacketHandler getHandler() {
        return handler;
    }

    public static Comparator<ClientConnection> compareSessions() {
        return Comparator.comparing(o -> o.session);
    }

    public interface ConnectionListener {
        void onConnected(ClientConnection client);
        void onDisconnected(ClientConnection client);
    }
}
