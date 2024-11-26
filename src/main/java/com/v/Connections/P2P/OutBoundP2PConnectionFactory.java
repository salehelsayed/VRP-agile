package com.v.connections.P2P;

import java.io.IOException;
import java.net.ServerSocket;

public class OutBoundP2PConnectionFactory extends BaseP2PConnectionFactory {
    private static OutBoundP2PConnectionFactory instance;

    private OutBoundP2PConnectionFactory() {
        // Private constructor to prevent instantiation
        super();
    }

    public static synchronized OutBoundP2PConnectionFactory getInstance() {
        if (instance == null) {
            instance = new OutBoundP2PConnectionFactory();
        }
        return instance;
    }

    public OutboundP2PConnection createOutBoundP2PConnection(String sourceIP, int sourcePort, String destinationIP, int portStart, int portEnd) throws IOException {
        String key = generateConnectionKey(sourceIP, destinationIP);
        // Check if connection already exists
        if (connections.containsKey(key)) {
            return (OutboundP2PConnection) connections.get(key);
        }

        // Iterate through the port range to find a free destination port
        for (int port = portStart; port <= portEnd; port++) {
            if (isPortAvailable(port)) {
                try {
                    OutboundP2PConnection connection = new OutboundP2PConnection(sourceIP, sourcePort, destinationIP, port);
                    connections.put(key, connection);
                    System.out.println("Outbound connection established from " + sourceIP + " to " + destinationIP + " on port " + port);
                    return connection;
                } catch (IOException e) {
                    System.err.println("Failed to establish outbound connection on port " + port + ": " + e.getMessage());
                }
            } else {
                System.out.println("Port " + port + " is already in use.");
            }
        }
        throw new IOException("No available ports found in the range " + portStart + "-" + portEnd);
    }

    // Helper method to check if a port is available
    private boolean isPortAvailable(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            serverSocket.setReuseAddress(true);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
