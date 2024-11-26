package com.v.connections.P2P;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.v.connections.packets.P2PPacket;
import com.v.connections.packets.Packet.MessageType;


public class InboundP2PConnection extends P2PConnection {
    private ServerSocket serverSocket;

    public InboundP2PConnection(String sourceIP, int port) throws IOException {
        super();
        this.sourceIP = sourceIP;
        this.port = port;
        this.state = ConnectionState.WAITING;
        this.serverSocket = new ServerSocket(port);
        this.serverSocket.setReuseAddress(true);
        startListenerThread();
    }

    private void startListenerThread() {
        Thread listenerThread = new Thread(() -> {
            try {
                while (!serverSocket.isClosed()) {
                    Socket clientSocket = serverSocket.accept();
                    this.destinationIP = clientSocket.getInetAddress().getHostAddress();
                    System.out.println("Accepted connection from " + clientSocket.getRemoteSocketAddress());
                    // Handle the accepted connection
                    handleClientSocket(clientSocket);
                }
            } catch (IOException e) {
                if (!serverSocket.isClosed()) {
                    e.printStackTrace();
                }
            }
        });
        listenerThread.start();
    }

    private void handleClientSocket(Socket clientSocket) throws IOException {
        this.socket = clientSocket; 
        this.state = ConnectionState.ESTABLISHED;
        this.destinationIP = clientSocket.getInetAddress().getHostAddress();
        this.destinationPort = clientSocket.getPort();
        send(new P2PPacket( null, MessageType.CONNECTION_ESTABLISHED, sourceIP, port, destinationIP, destinationPort));
    
    }

    @Override
    protected String generateKey() {
        // For inbound connections, key can be sourceIP:destinationIP (after a client connects)
        return sourceIP + ":" + destinationIP;
    }

    @Override
    public void close() throws IOException {
        super.close();
        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
        }
        this.state = ConnectionState.CLOSED;
    }
} 