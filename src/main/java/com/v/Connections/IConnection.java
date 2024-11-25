package com.v.Connections;

import java.io.IOException;

public interface IConnection {
    void send(String message) throws IOException;
    String receive() throws IOException;
    void close() throws IOException;
} 