package one.microproject.tcp.server.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

public class TcpMain implements Runnable, AutoCloseable {

    private static final Logger LOG = LoggerFactory.getLogger(TcpMain.class);

    private final ConnectionRegistry connectionRegistry;
    private final ExecutorService processors;
    private final String serverHost;
    private final Integer serverPort;
    private final String targetHost;
    private final Integer targetPort;
    private final Integer maxConnections;

    private boolean active;
    private ServerSocket serverSocket;

    public TcpMain(ConnectionRegistry connectionRegistry, String serverHost, Integer serverPort,
                   ExecutorService processors, String targetHost, Integer targetPort, Integer maxConnections) {
        this.connectionRegistry = connectionRegistry;
        this.serverHost = serverHost;
        this.serverPort = serverPort;
        this.processors = processors;
        this.targetHost = targetHost;
        this.targetPort = targetPort;
        this.active = true;
        this.maxConnections = maxConnections;
    }

    @Override
    public void run() {
        Socket clientSocket = null;
        try {
            LOG.info("Creating TCP socket");
            serverSocket = new ServerSocket(serverPort, maxConnections*2, InetAddress.getByName(serverHost));
            while (active) {
                LOG.info("Waiting for incoming TCP connections on {} ...", serverSocket.getLocalPort());
                clientSocket = serverSocket.accept();
                LOG.info("TCP connection accepted !");
                if (connectionRegistry.getActiveConnections() >= maxConnections) {
                    LOG.info("Max connections {} per server exceeded, closing connection {}:{} !", maxConnections, clientSocket.getRemoteSocketAddress(), clientSocket.getPort());
                    clientSocket.close();
                    continue;
                }
                String id = UUID.randomUUID().toString();
                LOG.info("Connection id={} from {}:{} to {}:{} in progress ...", id, clientSocket.getRemoteSocketAddress(), clientSocket.getPort(), targetHost, targetPort);
                Socket socket = new Socket(targetHost, targetPort);
                LOG.info("Connection id={} from {}:{} to {}:{} established.", id, clientSocket.getRemoteSocketAddress(), clientSocket.getPort(), targetHost, targetPort);
                DataForwarder forwardPipe = new DataForwarder(" -> ", clientSocket.getInputStream(), socket.getOutputStream());
                DataForwarder reversePipe = new DataForwarder(" <- ", socket.getInputStream(), clientSocket.getOutputStream());
                ActiveConnection activeConnection = new ActiveConnection(id, socket, targetHost, targetPort, forwardPipe, reversePipe, connectionRegistry);
                processors.submit(forwardPipe);
                processors.submit(reversePipe);
                connectionRegistry.register(activeConnection);
                LOG.info("Connection id={} data forwarders created", id);
            }
        } catch (IOException e) {
            try {
                LOG.error("TcpMain IOException: ", e);
                if (clientSocket != null) {
                    clientSocket.close();
                }
                close();
            } catch (Exception ex) {
                LOG.error("TcpMain Exception: ", e);
            }
        }
    }

    @Override
    public void close() throws Exception {
        LOG.info("Closing TCP Server ...");
        this.active = false;
        this.serverSocket.close();
        this.processors.shutdown();
    }

}
