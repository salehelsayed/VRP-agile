package com.v.connections.adhoc;

import java.io.IOException;

import com.v.Protocols.Protocol;


public class MultiHopConnectionFactory {

    public static MultiHopConnection createMultiHopConnection(String sourceIP, int sourcePort, String destinationIP,
            int destinationPort, Protocol protocol) throws IOException {
        return new MultiHopConnection(sourceIP, sourcePort, destinationIP, destinationPort, protocol);
    }

}
