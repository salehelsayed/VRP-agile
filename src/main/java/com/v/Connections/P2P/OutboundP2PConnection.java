package com.v.connections.P2P;

import java.io.*;
import java.net.Socket;

public class OutboundP2PConnection extends P2PConnection {
    public OutboundP2PConnection(String sourceIP, int sourcePort, String destinationIP, int destinationPort)
            throws IOException {
        super();
        this.sourceIP = sourceIP;
        this.destinationIP = destinationIP;
        this.port = destinationPort;

        this.socket = new Socket(destinationIP, destinationPort);
        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());

        // Send connection request
        out.writeUTF("Connect:" + sourceIP + ":" + sourcePort);
        out.flush();

        String response = in.readUTF();
        if (response.equals("Connected")) {
            System.out.println("Connection established from " + sourceIP + " to " + destinationIP);
            // Start handling incoming messages
            startMessageHandler();
        } else {
            throw new IOException("Connection failed: " + response);
        }

        // Set state to CONNECTED
        this.state = ConnectionState.CONNECTED;
    }

    @Override
    protected String generateKey() {
        return sourceIP + ":" + destinationIP;
    }

    @Override
    public void close() throws IOException {
        super.close();
        // Update state
        this.state = ConnectionState.CLOSED;
    }
} 