package com.v.connections.P2P;

import java.io.IOException;
import java.net.ServerSocket;

public class InBoundP2PConnectionFactory extends BaseP2PConnectionFactory {
    private static InBoundP2PConnectionFactory instance;

    private InBoundP2PConnectionFactory() {
        super();
    }

    public static InBoundP2PConnectionFactory getInstance() {
        if (instance == null) {
            instance = new InBoundP2PConnectionFactory();
        }
        return instance;
    }

    public InboundP2PConnection createInBoundP2PConnection(String sourceIP, int portStart, int portEnd) throws IOException {
        // For inbound connections, destinationIP can be left empty or set to a default value
        setPortStart(portStart);
        setPortEnd(portEnd);    
        String destinationIP = "";
        String key = generateConnectionKey(sourceIP, destinationIP);

        // Check if connection already exists
        if (connections.containsKey(key)) {
            return (InboundP2PConnection) connections.get(key);
        }

        // Iterate through the port range to find a free port
        for (int port = portStart; port <= portEnd; port++) {
            if (isPortAvailable(port)) {
                try {
                    InboundP2PConnection connection = new InboundP2PConnection(sourceIP, port);
                    connections.put(key, connection);
                    System.out.println("Inbound connection initialized on port " + port);
                    return connection;
                } catch (IOException e) {
                    System.err.println("Failed to initialize inbound connection on port " + port + ": " + e.getMessage());
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
