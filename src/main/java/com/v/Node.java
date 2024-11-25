package com.v;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

import com.v.Connections.IConnection;
import com.v.Connections.P2P.OutBoundP2PConnectionFactory;
import com.v.Connections.adhoc.MultiHopConnection;
import com.v.Connections.adhoc.MultiHopConnectionFactory;
import com.v.Protocols.DSR;
import com.v.Protocols.IProtocol;

import java.util.HashMap;

public class Node {

    private String ip;
    private int port;
    private Map<String, IConnection> connections;

    public Node(String ip, int port) {
        // Start of Selection
        this.ip = ip;
        this.port = port;
        this.connections = new HashMap<String, IConnection>();

    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public Map<String, IConnection> getConnections() {
        return connections;
    }

    public IConnection getConnection(String destinationIP, int destinationPort) {
        // Ensure that only one connection is established per node
        String key = destinationIP + ":" + destinationPort;
        IConnection connection = connections.get(key);

        try {
            if (connection == null) {
                connection = (IConnection) OutBoundP2PConnectionFactory.createOutBoundP2PConnection(this.ip, this.port,
                        destinationIP, destinationPort);
                connections.put(key, connection);
            }
        } catch (IOException e) {
            // Attempt to create a multi-hop connection using Protocol
            try {
                IProtocol protocol = new DSR();
                connection = (IConnection) MultiHopConnectionFactory.createMultiHopConnection(this.ip, this.port,
                        destinationIP, destinationPort, protocol);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            connections.put(key, connection);
        }

        return connection;
    }

    @Override
    public String toString() {
        return "Node{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
                ", connections=" + connections.keySet() +
                '}';
    }

    public void startListeners() throws IOException {
        for (IConnection connection : connections.values()) {
            connection.close();
        }

    }

    public void stopListeners() throws IOException
     {
        for (IConnection connection : connections.values()) {
            connection.close();
        }
    }

}
