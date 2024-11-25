package com.v.Protocols;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.Socket;

import static org.mockito.Mockito.*;

public class IProtocolTest {
    private IProtocol mockProtocol;
    private Socket mockSocket;

    @BeforeEach
    public void setUp() throws IOException {
        mockSocket = Mockito.mock(Socket.class);
        mockProtocol = new DSR();
        mockProtocol.initializeConnection("127.0.0.1", 8080, "192.168.1.1", 9090);
        // Assume DSR has a method to set the socket for testing purposes
        if (mockProtocol instanceof DSR) {
            ((DSR) mockProtocol).setSocket(mockSocket);
        }
    }

    @Test
    public void testSendMessage() throws IOException {
        String message = "Test Message";
        mockProtocol.sendMessage(message);
        // Verify that the mock socket's output stream was used
        verify(mockSocket.getOutputStream(), times(1)).write(any(byte[].class));
    }

    // @Test
    // public void testReceiveMessage() throws IOException {
    //     when(mockSocket.getInputStream()).thenReturn(/* your InputStream mock here */);
    //     String received = mockProtocol.receiveMessage();
    //     // Add assertions based on your mock setup
    // }

    // ... additional tests ...
} 