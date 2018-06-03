package nl.avans.pelicoonmessenger.base.net.packets;

import java.io.Serializable;

public abstract class Packet implements Serializable {
    public enum Type { SERVER, CLIENT, CROSS }

    public abstract Type getPacketType();
}
