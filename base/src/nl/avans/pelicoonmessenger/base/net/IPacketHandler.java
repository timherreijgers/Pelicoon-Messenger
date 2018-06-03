package nl.avans.pelicoonmessenger.base.net;

import nl.avans.pelicoonmessenger.base.net.packets.Packet;

import java.io.IOException;

public interface IPacketHandler {
    void queuePacket(Packet packet) throws IOException;
    boolean validatePacket(Packet packet);
    void processPacket(Packet packet);
}
