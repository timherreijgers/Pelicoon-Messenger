package nl.avans.pelicoonmessenger.client.net;

import nl.avans.pelicoonmessenger.base.models.Message;
import nl.avans.pelicoonmessenger.base.net.IPacketHandler;
import nl.avans.pelicoonmessenger.base.net.packets.*;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class PacketHandler extends Thread implements IPacketHandler {

    private Connection connection;
    private String username; // Temp store username here


    private Queue<Packet> packetQueue = new LinkedList<>();

    private MessageReceivedListener listener;

    public PacketHandler(Connection connection, String username) {
        this.connection = connection;
        this.username = username;
        //setName("ClientHandler-"+getId());
    }

    public void setMessageReceivedListener(MessageReceivedListener listener) {
        this.listener = listener;
    }

    public void sendMessage(String message) {
        queuePacket(new MessagePacket(message));
    }

    @Override
    public void queuePacket(Packet packet) {
        packetQueue.offer(packet);
    }

    @Override
    public boolean validatePacket(Packet packet) {
        return packet.getPacketType() == Packet.Type.SERVER;
    }

    @Override
    public void processPacket(Packet packet) {
        // Check if we are already authenticated
        if (!connection.isAuthenticated()) {
            if (packet instanceof TokenPacket) {
                queuePacket(new AuthenticatePacket(((TokenPacket) packet).getToken(), username));
            }

            if (packet instanceof SessionPacket) {
                connection.session = ((SessionPacket) packet).getSession();
            }
        }

        if (packet instanceof MessageHistoryPacket) {
            System.out.println("Received new messages: " + ((MessageHistoryPacket) packet).getHistory());
            if(listener != null) {
                for (Message message : ((MessageHistoryPacket) packet).getHistory()) {
                    listener.onMessageReceived(message);
                }
            }
        }
    }

    @Override
    public void run() {
        while (true) {
            if(packetQueue.size() > 0) {
                Packet packet = packetQueue.poll();
                try {
                    connection.outputStream.writeObject(packet);
                    connection.outputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public interface MessageReceivedListener {
        void onMessageReceived(Message message);
    }
}
