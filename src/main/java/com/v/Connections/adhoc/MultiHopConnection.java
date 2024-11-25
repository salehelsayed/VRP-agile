package com.v.Connections.adhoc;

import java.io.IOException;
import java.net.Socket;

import com.v.Connections.IConnection;
import com.v.Protocols.IProtocol;

public class MultiHopConnection implements IConnection {
    private int sourcePort;
    private String destinationIP;
    private int destinationPort;
    private IProtocol protocol;
    private String sourceIP;

    public MultiHopConnection(String sourceIP, int sourcePort, String destinationIP, int destinationPort,
            IProtocol protocol) throws IOException {
        this.sourceIP = sourceIP;
        this.sourcePort = sourcePort;
        this.destinationIP = destinationIP;
        this.destinationPort = destinationPort;
        this.protocol = protocol;
        this.protocol.initializeConnection(sourceIP, sourcePort, destinationIP, destinationPort);
    }

    // todo: implememnt packet class
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

    @Override
    public void registerConnection(String sourceIP, int sourcePort, String destinationIP, int destinationPort,
            Socket socket) throws IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'registerConnection'");
    }
}