package com.v.Connections.P2P;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.v.Connections.IConnection;

public class OutBoundP2PConnectionFactory {

    public static P2PConnection createOutBoundP2PConnection(String sourceIP, int sourcePort, String destinationIP,
            int destinationPort) throws IOException {

        return new P2PConnection(sourceIP, sourcePort, destinationIP, destinationPort);

    }

}
