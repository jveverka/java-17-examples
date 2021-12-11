package one.microproject.proxyserver.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpDataHandlerReverse implements Runnable, AutoCloseable {

    private static final Logger LOG = LoggerFactory.getLogger(UdpDataHandlerReverse.class);

    private final DatagramSocket serverSocket;
    private final DatagramSocket clientSocket;
    private final String serverHost;
    private final Integer serverPort;

    private boolean active;

    public UdpDataHandlerReverse(DatagramSocket serverSocket, DatagramSocket clientSocket, String serverHost, Integer serverPort) {
        this.serverSocket = serverSocket;
        this.clientSocket = clientSocket;
        this.active = true;
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    @Override
    public void close() throws Exception {
        this.active = false;
    }

    @Override
    public void run() {
        try {
            InetAddress serverAddress = InetAddress.getByName(serverHost);
            byte[] clientBuffer = new byte[2048];
            while (active) {
                DatagramPacket clientPacket = new DatagramPacket(clientBuffer, clientBuffer.length);
                clientSocket.receive(clientPacket);
                byte[] payload = clientPacket.getData();
                DatagramPacket serverPacket = new DatagramPacket(payload, payload.length, serverAddress, serverPort);
                serverSocket.send(serverPacket);
            }
        } catch (IOException e) {
            LOG.error("UdpDataHandlerReverse ERROR:", e);
        }
    }

}
