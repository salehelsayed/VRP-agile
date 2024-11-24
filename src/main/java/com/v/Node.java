package com.v;

import java.io.IOException;

public class Node {

    private String ip;
    private int port;

    public Node(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public Connection getConnection(String destinationIP, int destinationPort) {
        // singleton pattern to ensure that only one connection is established per node
        Connection connection = Connection.getInstance(this.ip, this.port, destinationIP, destinationPort);
        
            try {
                if (connection == null || connection.isConnected()) {
                connection = new Connection(this.ip, this.port, destinationIP, destinationPort);
                Connection.setInstance(this.ip, this.port, destinationIP, destinationPort, connection);
            }} catch (IOException e) {
                
                e.printStackTrace();
                throw new RuntimeException("Failed to establish connection to " + destinationIP + ":" + destinationPort, e);    
            }
        


        return connection;
    }

}
