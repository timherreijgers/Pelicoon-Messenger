package nl.avans.pelicoonmessenger.base.net.packets;

public class MessagePacket extends Packet {

    private final String message;

    public MessagePacket(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public Type getPacketType() {
        return Type.CLIENT;
    }
}
