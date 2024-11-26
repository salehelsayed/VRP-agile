package com.v.connections;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.v.connections.packets.Packet;

public abstract class Connection {
    protected String sourceIP;
    protected int sourcePort;
    protected String destinationIP;
    protected int destinationPort;
    protected Socket socket;
    protected DataOutputStream out;
    protected DataInputStream in;

    public void setSourceIP(String sourceIP) {
        this.sourceIP = sourceIP;
    }

    public String getSourceIP() {
        return sourceIP;
    }

    public int getSourcePort() {
        return sourcePort;
    }

    public String getDestinationIP() {
        return destinationIP;
    }

    public int getDestinationPort() {
        return destinationPort;
    }

    public Socket getSocket() {
        return socket;
    }

    public DataOutputStream getOut() {
        return out;
    }

    public DataInputStream getIn() {
        return in;
    }

    public void setSourcePort(int sourcePort) {
        this.sourcePort = sourcePort;
    }

        public void setDestinationIP(String destinationIP) {
        this.destinationIP = destinationIP;
    }

    public void setDestinationPort(int destinationPort) {
        this.destinationPort = destinationPort;
    }

    public abstract void send(Packet packet) throws IOException;

    public abstract Packet receive(Packet expectedPacket) throws IOException;

    public abstract void close() throws IOException;
}