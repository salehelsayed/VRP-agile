package com.v.Connections.P2P;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class P2PConnection {
    private String sourceIP;
    private int sourcePort;
    private String destinationIP;
    private int destinationPort;
    private Socket socket;
    private ServerSocket serverSocket;
    private DataOutputStream out;
    private DataInputStream in;
    private Thread listenerThread;

    public P2PConnection(String sourceIP, int sourcePort) throws IOException {
        setListenerThread(sourcePort);

        //todo:check hwhere ip goes
    }

    public P2PConnection(String sourceIP, int sourcePort, String destinationIP, int destinationPort)
            throws IOException {
        this.socket = new Socket(destinationIP, destinationPort);

        registerConnection(sourceIP, sourcePort, destinationIP, destinationPort);
    }

    private void registerConnection(String sourceIP, int sourcePort, String destinationIP, int destinationPort
            )
            throws IOException {
        this.sourceIP = sourceIP;
        this.sourcePort = sourcePort;
        this.destinationIP = destinationIP;
        this.destinationPort = destinationPort;
       this.out = new DataOutputStream(socket.getOutputStream());
        this.in = new DataInputStream(socket.getInputStream());

        this.out.writeUTF("Connect:" + this.sourceIP + ":" + this.sourcePort);
        this.out.flush();

        String response = in.readUTF();
        if (response.equals("Connected")) {
            System.out.println("Connection established from " + sourceIP + ":" + sourcePort + " to " + destinationIP
                    + ":" + destinationPort);
        } else {
            throw new IOException("Connection failed: " + response);
        }
    }
    private Socket setListenerThread(int port) {
        this.listenerThread = new Thread(() -> {
            try (ServerSocket server = new ServerSocket(port)) {
                this.serverSocket = server;
                while (!server.isClosed()) {
                    this.socket = server.accept();
                    handleClientSocket(this.socket);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        listenerThread.start();
        return this.socket;
    }

    private synchronized void handleClientSocket(Socket clientSocket) {
        // Additional handling of the clientSocket can be done here
    }

    public Object getServerSocket() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getServerSocket'");
    }

    public Object getListenerThread() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getListenerThread'");
    }

}
