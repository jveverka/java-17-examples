package one.microproject.proxyserver.tests.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import static one.microproject.proxyserver.tests.tools.Constants.MESSAGE_END;

public class TCPServer implements Runnable, AutoCloseable {

    private static final Logger LOG = LoggerFactory.getLogger(TCPServer.class);

    private final String host;
    private final Integer port;

    private boolean active;
    private ServerSocket serverSocket;
    private Socket clientSocket;

    public TCPServer(String host, Integer port) {
        this.host = host;
        this.port = port;
        this.active = true;
    }

    @Override
    public void run() {
        try {
            LOG.info("Starting TEST TCP Server: {}:{}", host, port);
            serverSocket = new ServerSocket(port, 2, InetAddress.getByName(host));
            clientSocket = serverSocket.accept();
            LOG.info("TEST TCP Server: {}:{} - connection accepted !", host, port);
            while(active) {
                InputStream is = clientSocket.getInputStream();
                int dataByte;
                ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
                LOG.info("TEST TCP Server: {}:{} - reading request.", host, port);
                while((dataByte = is.read()) != MESSAGE_END) {
                    baos.write(dataByte);
                }
                baos.flush();
                LOG.info("TEST TCP Server: {}:{} - writing response.", host, port);
                OutputStream os = clientSocket.getOutputStream();
                os.write(baos.toByteArray());
                os.write(MESSAGE_END);
                os.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void close() throws Exception {
        active = false;
        if (clientSocket != null) {
            clientSocket.close();
        }
        if (serverSocket != null) {
            serverSocket.close();
        }
    }

}
