package com.v.Connections;

import java.io.IOException;
import java.net.Socket;

public interface IConnection {

    void registerConnection(String sourceIP, int sourcePort, String destinationIP, int destinationPort,
            Socket socket)
            throws IOException;

    void send(String message) throws IOException;

    String receive() throws IOException;

    void close() throws IOException;
}