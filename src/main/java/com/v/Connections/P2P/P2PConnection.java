package com.v.Connections.P2P;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class P2PConnection {
    private String sourceIP;
    private int sourcePort;
    private String destinationIP;
    private int destinationPort;
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;

    public P2PConnection(String sourceIP, int sourcePort, String destinationIP, int destinationPort, Socket socket)
            throws IOException {
        this.sourceIP = sourceIP;
        this.sourcePort = sourcePort;
        this.destinationIP = destinationIP;
        this.destinationPort = destinationPort;
        this.socket = socket;
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

}
