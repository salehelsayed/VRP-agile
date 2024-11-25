package com.v.Connections;

import java.io.IOException;

import com.v.Protocols.IProtocol;

public class MultiHopConnection implements IConnection {
    private String sourceIP;
    private int sourcePort;
    private String destinationIP;
    private int destinationPort;
    private IProtocol protocol;

    public MultiHopConnection(String sourceIP, int sourcePort, String destinationIP, int destinationPort, IProtocol protocol) throws IOException {
        this.sourceIP = sourceIP;
        this.sourcePort = sourcePort;
        this.destinationIP = destinationIP;
        this.destinationPort = destinationPort;
        this.protocol = protocol;
        this.protocol.initializeConnection(sourceIP, sourcePort, destinationIP, destinationPort);
    }

    @Override
    public void send(String message) throws IOException {
        protocol.sendMessage(message);
    }

    @Override
    public String receive() throws IOException {
        return protocol.receiveMessage();
    }

    @Override
    public void close() throws IOException {
        protocol.closeConnection();
    }
} 