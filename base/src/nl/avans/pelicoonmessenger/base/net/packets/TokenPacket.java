package nl.avans.pelicoonmessenger.base.net.packets;

import java.util.UUID;

public class TokenPacket extends Packet {

    private final UUID token;

    public TokenPacket(UUID token) {
        this.token = token;
    }

    public UUID getToken() {
        return token;
    }

    @Override
    public Type getPacketType() {
        return Type.SERVER;
    }
}
