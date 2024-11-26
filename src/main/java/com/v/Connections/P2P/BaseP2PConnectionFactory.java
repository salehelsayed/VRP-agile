package com.v.connections.P2P;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.ArrayList;

public abstract class BaseP2PConnectionFactory {
    protected int portStart;
    protected int portEnd;

    public int getPortStart() {
        return portStart;
    }

    public int getPortEnd() {
        return portEnd;
    }

    protected void setPortStart(int portStart) {
        this.portStart = portStart;
    }   

    protected void setPortEnd(int portEnd) {
        this.portEnd = portEnd;
    }

    // Map to store connections with key as "srcIP:destinationIP"
    protected Map<String, P2PConnection> connections = new ConcurrentHashMap<>();

    // Method to create the key
    protected String generateConnectionKey(String srcIP, String destinationIP) {
        return srcIP + ":" + destinationIP;
    }

    // Common method to get a connection
    public P2PConnection getConnection(String srcIP, String destinationIP) {
        String key = generateConnectionKey(srcIP, destinationIP);
        return connections.get(key);
    }

    // Common method to remove a connection
    public void removeConnection(String srcIP, String destinationIP) {
        String key = generateConnectionKey(srcIP, destinationIP);
        connections.remove(key);
    }

    // Getter for all connections
    public List<P2PConnection> getAllConnections() {
        return new ArrayList<>(connections.values());
    }

    // Setter for all connections - use with caution
    public void setAllConnections(Map<String, P2PConnection> newConnections) {
        connections.clear();
        connections.putAll(newConnections);
    }
} 