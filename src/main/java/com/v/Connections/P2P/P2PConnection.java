package com.v.connections.P2P;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.v.connections.Connection;
import com.v.connections.packets.Packet;

public abstract class P2PConnection extends Connection {

    protected String sourceIP;
    protected String destinationIP;
    protected int port;
    protected Socket socket;
    protected DataInputStream in;
    protected DataOutputStream out;
    protected volatile boolean running = true;
    protected ConnectionState state;

    // List of handler threads
    protected List<Thread> handlerThreads = new CopyOnWriteArrayList<>();

    // Added enum for connection states
    protected enum ConnectionState {
        WAITING,
        CONNECTED,
        CLOSED, ESTABLISHED
    }

    public P2PConnection(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());
        this.state = ConnectionState.CONNECTED; // Set initial state
        startMessageHandler();
    }

    public P2PConnection() {
        //TODO Auto-generated constructor stub
    }

    // Abstract method to generate a unique key for the connection
    protected abstract String generateKey();

    // Common method to start message handling
    protected void startMessageHandler() {
        Thread handlerThread = new Thread(() -> {
            try {
                while (running && !socket.isClosed()) {
                    String data = in.readUTF();
                    System.out.println("Received message: " + data);
                    // Process the incoming message
                    // processIncomingMessage(data);
                }
            } catch (IOException e) {
                if (running) {
                    e.printStackTrace();
                }
            } finally {
                try {
                    close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        handlerThread.start();
        handlerThreads.add(handlerThread);
    }

    @Override
    public void send(Packet packet) throws IOException {
        out.writeUTF(packet.toString());
        out.flush();
    }

    @Override
    public Packet receive(Packet expectedPacket) throws IOException {
        String data = in.readUTF();
        // Parse data to create Packet object
        return expectedPacket; // Implement parsing logic as needed
    }

    @Override
    public void close() throws IOException {
        running = false;
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
        for (Thread thread : handlerThreads) {
            if (thread != null && thread.isAlive()) {
                thread.interrupt();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        // Update state
        this.state = ConnectionState.CLOSED;
    }

    public String getKey() {
        return generateKey();
    }

    public int getPort() {
        return port;
    }

    public ConnectionState getState() {
        return this.state;
    }

    public String getSourceIP() {
        return sourceIP;
    }

    public String getDestinationIP() {
        return destinationIP;
    }

    P2PConnectionFactory p;
}
