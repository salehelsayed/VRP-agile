package com.v.connections;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.v.connections.P2P.InBoundP2PConnectionFactory;
import com.v.connections.P2P.InboundP2PConnection;
import com.v.connections.P2P.OutBoundP2PConnectionFactory;
import com.v.connections.P2P.OutboundP2PConnection;
import com.v.connections.P2P.BaseP2PConnectionFactory;


public class ConnectionFactory {
    private static ConnectionFactory instance;
    private static int portEnd;
    private static int portStart;
    private static Map<String, Connection> connections;

    // Instances of specific factories
    private final BaseP2PConnectionFactory inboundFactory;
    private final BaseP2PConnectionFactory outboundFactory;

    private ConnectionFactory(int portStart, int portEnd) {
        this.inboundFactory = InBoundP2PConnectionFactory.getInstance();
        this.outboundFactory = OutBoundP2PConnectionFactory.getInstance();
    }

    public static synchronized ConnectionFactory getInstance(int portStart, int portEnd) {
        if (instance == null) {
            connections = new ConcurrentHashMap<String, Connection>();
            instance = new ConnectionFactory(portStart, portEnd);
        }
        return instance;
    }

    // Method to create an inbound P2P connection
    public InboundP2PConnection createInboundConnection(String sourceIP) throws IOException {
        InboundP2PConnection connection = ((InBoundP2PConnectionFactory) inboundFactory).createInBoundP2PConnection(sourceIP, inboundFactory.getPortStart(), inboundFactory.getPortEnd());
        connections.put(sourceIP + ":::", connection);
        return connection;
    }

    // Method to create an outbound P2P connection
    public OutboundP2PConnection createOutboundConnection(String sourceIP, int sourcePort, String destinationIP) throws IOException {
        OutboundP2PConnection connection = ((OutBoundP2PConnectionFactory) outboundFactory).createOutBoundP2PConnection(sourceIP, sourcePort, destinationIP, outboundFactory.getPortStart(), outboundFactory.getPortEnd()); 
        connections.put(sourceIP + "::" + destinationIP + ":" + sourcePort, connection);
        return connection;
    }

    public Map<String, Connection> getConnections() {
        return connections;
    }   


    public int getPortStart() {
        return portStart;
    }

    public int getPortEnd() {
        return portEnd;
    }
} 
