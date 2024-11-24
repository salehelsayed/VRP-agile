package com.v;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.HashMap;

public class Node {

    private String ip;
    private int port;
    private Map<String, Connection> connections;
    private ServerSocket serverSocket;
    private Thread listenerThread;

    public Node(String ip, int port) {
        this.ip = ip;
        this.port = port;
        this.connections = new HashMap<>();
    }

    public void startListener() {
        listenerThread = new Thread(() -> {
            try {
                serverSocket = new ServerSocket(port);
                while (!serverSocket.isClosed()) {
                    Socket clientSocket = serverSocket.accept();
                    DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                    DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

                    String message = in.readUTF();
                    if (message.startsWith("Connect:")) {
                        out.writeUTF("Connected");
                        out.flush();
                        System.out.println("Accepted connection on " + ip + ":" + port);
                    } else if (message.equals("Disconnect")) {
                        clientSocket.close();
                    }
                    // You can add more message handling here
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        listenerThread.start();
    }

    public void stopListener() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            if (listenerThread != null && listenerThread.isAlive()) {
                listenerThread.join();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public Map<String, Connection> getConnections() {
        return connections;
    }

    public Connection getConnection(String destinationIP, int destinationPort) {
        // Ensure that only one connection is established per node
        String key = destinationIP + ":" + destinationPort;
        Connection connection = connections.get(key);

        try {
            if (connection == null) {
                connection = new Connection(this.ip, this.port, destinationIP, destinationPort);
                connections.put(key, connection);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to establish connection to " + destinationIP + ":" + destinationPort, e);
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
}
