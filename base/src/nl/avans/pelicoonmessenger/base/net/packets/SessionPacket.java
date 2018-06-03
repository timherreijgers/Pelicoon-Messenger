package nl.avans.pelicoonmessenger.base.net.packets;

import nl.avans.pelicoonmessenger.base.models.Session;

public class SessionPacket extends Packet {

    private final Session session;

    public SessionPacket(Session session) {
        this.session = session;
    }

    public Session getSession() {
        return session;
    }

    @Override
    public Type getPacketType() {
        return Type.SERVER;
    }
}
