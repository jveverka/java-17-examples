package one.microproject.proxyserver.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPServer implements Runnable, AutoCloseable {

    private static final Logger LOG = LoggerFactory.getLogger(UDPServer.class);

    private final String host;
    private final Integer port;

    private boolean active;
    private DatagramSocket serverSocket;

    public UDPServer(String host, Integer port) {
        this.host = host;
        this.port = port;
        this.active = true;
    }

    @Override
    public void run() {
        try {
            LOG.info("Starting TEST UDP Server: {}:{}", host, port);
            byte[] buf = new byte[2046];
            while (active) {
                serverSocket = new DatagramSocket(port, InetAddress.getByName(host));
                DatagramPacket requestPacket = new DatagramPacket(buf, buf.length);
                LOG.info("TEST UDP Server: {}:{} - waiting for message.", host, port);
                serverSocket.receive(requestPacket);
                InetAddress responseAddress = requestPacket.getAddress();
                int responsePort = requestPacket.getPort();
                byte[] payload = requestPacket.getData();
                DatagramPacket responsePacket = new DatagramPacket(payload, payload.length, responseAddress, responsePort);
                LOG.info("TEST UDP Server: {}:{} - sending response.", host, port);
                serverSocket.send(responsePacket);
            }
        } catch (IOException e) {
            try {
                close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void close() throws Exception {
        this.active = false;
        if (serverSocket != null) {
            serverSocket.close();
        }
    }

}
