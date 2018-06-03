package nl.avans.pelicoonmessenger.base.net.packets;

import java.util.UUID;

public class AuthenticatePacket extends Packet {

    private final UUID token;
    private final String username;

    public AuthenticatePacket(UUID token, String username) {
        this.token = token;
        this.username = username;
    }

    public UUID getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public Type getPacketType() {
        return Type.CLIENT;
    }
}
