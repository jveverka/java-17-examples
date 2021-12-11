package one.microproject.proxyserver.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpDataHandlerForward implements Runnable, AutoCloseable {

    private static final Logger LOG = LoggerFactory.getLogger(UdpDataHandlerForward.class);

    private final DatagramSocket serverSocket;
    private final DatagramSocket clientSocket;
    private final String targetHost;
    private final Integer targetPort;

    private boolean active;

    public UdpDataHandlerForward(DatagramSocket serverSocket, DatagramSocket clientSocket, String targetHost, Integer targetPort) {
        this.serverSocket = serverSocket;
        this.clientSocket = clientSocket;
        this.active = true;
        this.targetHost = targetHost;
        this.targetPort = targetPort;
    }

    @Override
    public void close() throws Exception {
        this.active = false;
    }

    @Override
    public void run() {
        try {
            InetAddress clientAddress = InetAddress.getByName(targetHost);
            byte[] serverBuffer = new byte[2048];
            while (active) {
                DatagramPacket serverPacket = new DatagramPacket(serverBuffer, serverBuffer.length);
                serverSocket.receive(serverPacket);
                byte[] payload = serverPacket.getData();
                DatagramPacket clientPacket = new DatagramPacket(payload, payload.length, clientAddress, targetPort);
                clientSocket.send(clientPacket);
            }
        } catch (IOException e) {
            LOG.error("UdpDataHandlerForward ERROR:", e);
        }
    }

}
