package com.v.Connections.adhoc;

import java.io.IOException;

import com.v.Connections.IConnection;
import com.v.Protocols.IProtocol;

public class MultiHopConnectionFactory {
    private String sourceIP;
    private int sourcePort;
    private String destinationIP;
    private int destinationPort;
    private IProtocol protocol;

    public static MultiHopConnection createMultiHopConnection(String sourceIP, int sourcePort, String destinationIP,
            int destinationPort, IProtocol protocol) throws IOException {
        return new MultiHopConnection(sourceIP, sourcePort, destinationIP, destinationPort, protocol);
    }

}
