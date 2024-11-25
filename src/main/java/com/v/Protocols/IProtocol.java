package com.v.Protocols;

import java.io.IOException;
import java.net.Socket;
public interface IProtocol {
    
    String getVersion();

    void sendMessage(String message) throws IOException;

    String receiveMessage() throws IOException;

    void closeConnection() throws IOException;

    void setSocket(Socket socket) throws IOException;

    void initializeConnection(String sourceIP, int sourcePort, String destinationIP, int destinationPort) throws IOException;
    
}
