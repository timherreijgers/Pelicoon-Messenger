package nl.avans.pelicoonmessenger.base.net.packets;

import nl.avans.pelicoonmessenger.base.models.Message;

import java.util.Arrays;
import java.util.List;

public class MessageHistoryPacket extends Packet {

    private final List<Message> history;

    public MessageHistoryPacket(Message... message) {
        history = Arrays.asList(message);
    }

    public MessageHistoryPacket(List<Message> messages) {
        history = messages;
    }

    public List<Message> getHistory() {
        return history;
    }

    @Override
    public Type getPacketType() {
        return Type.SERVER;
    }
}
