package com.v.connections.adhoc;

import java.io.IOException;

import com.v.Protocols.Protocol;
import com.v.connections.Connection;
import com.v.connections.packets.Packet;

public class MultiHopConnection extends Connection {
    private Protocol protocol;

    public MultiHopConnection(String sourceIP, int sourcePort, String destinationIP, int destinationPort,
            Protocol protocol) throws IOException {
        this.sourceIP = sourceIP;
        this.sourcePort = sourcePort;
        this.destinationIP = destinationIP;
        this.destinationPort = destinationPort;
        this.protocol = protocol;
        this.protocol.initializeConnection(sourceIP, sourcePort, destinationIP, destinationPort);
    }

    // todo: implememnt packet class
    @Override
    public void send(Packet message) throws IOException {
        protocol.sendMessage(message);
    }

    @Override
    public Packet receive(Packet expectedPacket) throws IOException {
        return protocol.receiveMessage(expectedPacket);
    }

    @Override
    public void close() throws IOException {
        protocol.closeConnection();
    }
}