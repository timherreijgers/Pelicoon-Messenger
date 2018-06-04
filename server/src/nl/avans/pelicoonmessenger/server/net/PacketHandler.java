package nl.avans.pelicoonmessenger.server.net;

import nl.avans.pelicoonmessenger.base.logging.Logger;
import nl.avans.pelicoonmessenger.base.models.Message;
import nl.avans.pelicoonmessenger.base.models.Session;
import nl.avans.pelicoonmessenger.base.models.User;
import nl.avans.pelicoonmessenger.base.net.IPacketHandler;
import nl.avans.pelicoonmessenger.base.net.packets.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class PacketHandler extends Thread implements IPacketHandler {

    private Logger logger = new Logger(getClass().getSimpleName());

    private ClientConnection client;

    private Queue<Packet> packetQueue = new LinkedList<>();
    private List<PacketListener> listeners = new ArrayList<>();

    public PacketHandler(ClientConnection client) {
        this.client = client;
        setName("ClientHandler-"+client.getId());
    }

    public void sendMessage(Message message) {
        queuePacket(new MessageHistoryPacket(message));
    }

    public void sendMessages(List<Message> messages) {
        queuePacket(new MessageHistoryPacket(messages));
    }

    @Override
    public void queuePacket(Packet packet) {
        packetQueue.offer(packet);
    }

    @Override
    public boolean validatePacket(Packet packet) {
        return packet.getPacketType() == Packet.Type.CLIENT;
    }

    @Override
    public void processPacket(Packet packet) {
        if(!client.isAuthenticated() && packet instanceof AuthenticatePacket) {
            if(!((AuthenticatePacket) packet).getToken().equals(client.getToken())) {
                queuePacket(new AuthenticationFailedPacket(AuthenticationFailedPacket.ErrorType.TOKEN,
                        "Token did not match with the server token"));
                logger.warn("Client did not use our given token for authentication, authentication failed!");
                client.disconnect();
                return;
            }

            User user = new User.Builder()
                    .username(((AuthenticatePacket) packet).getUsername())
                    .build();

            Session session = new Session.Builder()
                    .user(user)
                    .ip(client.getHostAddress())
                    .build();

            logger.info("Welcoming " + user.getUsername() + " by sending the session");

            queuePacket(new SessionPacket(session));

            client.session = session;

            for(PacketListener listener : listeners) {
                listener.onAuthenticated(client, session);
            }
        }

        if (packet instanceof MessagePacket) {
            if(((MessagePacket) packet).getMessage() != null && ((MessagePacket) packet).getMessage() != "") {
                Message message = new Message.Builder()
                        .user(client.getAuthenticatedSession().getUser())
                        .message(((MessagePacket) packet).getMessage())
                        .timestamp()
                        .build();

                for (PacketListener listener : listeners) {
                    listener.onMessageReceived(message);
                }
            }
        }
    }

    public void addPacketListener(PacketListener listener) {
        listeners.add(listener);
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (packetQueue.size() > 0) {
                    Packet packet = packetQueue.poll();
                    logger.debug("Sending queued packet: " + packet.getClass().getSimpleName());
                    client.out.writeObject(packet);
                    client.out.flush();
                } else {
                    Thread.sleep(100);
                }
            } catch (IOException | InterruptedException e) {
                logger.printStackTrace(e);
            }
        }
    }

    public interface PacketListener {
        void onAuthenticated(ClientConnection client, Session session);
        void onMessageReceived(Message message);
    }
}
