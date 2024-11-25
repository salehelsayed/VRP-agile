package com.v.Connections.P2P;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class InBoundP2PConnectionFactory {
    public static P2PConnection createIntBoundP2PConnection(String sourceIP, int sourcePort, String destinationIP,
            int destinationPort) throws IOException {

        return new P2PConnection(sourceIP, sourcePort);

    }
}
