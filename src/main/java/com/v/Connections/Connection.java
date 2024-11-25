package com.v.Connections;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Connection implements IConnection {
    
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private String sourceIP; 
    private int sourcePort; 
    private String destinationIP; 
    private int destinationPort;
    
    public Connection (String sourceIP, int sourcePort, String destinationIP, int destinationPort) throws IOException {
        this.sourceIP = sourceIP;
        this.sourcePort = sourcePort;
        this.destinationIP = destinationIP;
        this.destinationPort = destinationPort;
        this.socket = new Socket(destinationIP, destinationPort);
        this.out = new DataOutputStream(socket.getOutputStream());
        this.in = new DataInputStream(socket.getInputStream());
        
        this.out.writeUTF("Connect:" + this.sourceIP + ":" + this.sourcePort);
        this.out.flush();
        
        String response = in.readUTF();
        if (response.equals("Connected")) {
            System.out.println("Connection established from " + sourceIP + ":" + sourcePort + " to " + destinationIP + ":" + destinationPort); 
        } else {
            throw new IOException("Connection failed: " + response);
        }
    }
    
    public void send(String message) throws IOException {
        out.writeUTF(message);
        out.flush();
    }
    
    public String receive() throws IOException {
        return in.readUTF();
    }
    
    public void close() throws IOException {
        out.writeUTF("Disconnect");
        out.flush();
        socket.close();
    }

    

}
