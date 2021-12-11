package one.microproject.proxyserver.tests.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;


public class UDPClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(UDPClient.class);

    private final String host;
    private final Integer port;

    private DatagramSocket socket;

    public UDPClient(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    public void connect() throws IOException {
        LOGGER.info("Starting TEST UDP Client: {}:{}", host, port);
        socket = new DatagramSocket(port, InetAddress.getByName(host));
    }

    public String sendAndWaitForResponse(String message) throws IOException {
        LOGGER.info("TEST UDP Client: {}:{}  - sending data", host, port);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(message.getBytes(StandardCharsets.UTF_8));
        baos.flush();
        byte[] data = baos.toByteArray();
        DatagramPacket packet = new DatagramPacket(data, data.length, InetAddress.getByName(host), port);
        socket.send(packet);
        data = new byte[2048];
        packet = new DatagramPacket(data, data.length);
        socket.receive(packet);
        return new String(map(packet.getData()));
    }

    public void close() throws IOException {
        socket.close();
    }

    private static String map(byte[] bytes) {
        StringBuffer buffer = new StringBuffer();
        for (byte by: bytes) {
            if (by != 0) {
                buffer.append((char)by);
            }
        }
        return buffer.toString();
    }

}
