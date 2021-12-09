package one.microproject.tcp.server.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

public class TcpMain implements Runnable, AutoCloseable {

    private static final Logger LOG = LoggerFactory.getLogger(TcpMain.class);

    private final ConnectionRegistry connectionRegistry;
    private final ServerSocket serverSocket;
    private final ExecutorService processors;
    private final String targetHost;
    private final Integer targetPort;

    private boolean active;

    public TcpMain(ConnectionRegistry connectionRegistry, ServerSocket serverSocket,
                   ExecutorService processors, String targetHost, Integer targetPort) {
        this.connectionRegistry = connectionRegistry;
        this.processors = processors;
        this.serverSocket = serverSocket;
        this.targetHost = targetHost;
        this.targetPort = targetPort;
        this.active = true;
    }

    @Override
    public void run() {
        try {
            while (active) {
                LOG.info("Waiting for incoming TCP connections ...");
                Socket clientSocket = serverSocket.accept();
                String id = UUID.randomUUID().toString();
                LOG.info("Connection id={} from {}:{} to {}:{} in progress ...", id, clientSocket.getRemoteSocketAddress(), clientSocket.getPort(), targetHost, targetPort);
                try (Socket socket = new Socket(targetHost, targetPort)) {
                    DataForwarder forwardPipe = new DataForwarder(" -> ", clientSocket.getInputStream(), socket.getOutputStream());
                    DataForwarder reversePipe = new DataForwarder(" <- ", socket.getInputStream(), clientSocket.getOutputStream());
                    ActiveConnection activeConnection = new ActiveConnection(id, socket, targetHost, targetPort, forwardPipe, reversePipe, connectionRegistry);
                    processors.submit(forwardPipe);
                    processors.submit(reversePipe);
                    connectionRegistry.register(activeConnection);
                    LOG.info("Connection id={} from {}:{} to {}:{} established.", id, clientSocket.getRemoteSocketAddress(), clientSocket.getPort(), targetHost, targetPort);
                }
            }
        } catch (IOException e) {
            LOG.info("TcpMain IOException");
            this.active = false;
        }
    }

    @Override
    public void close() throws Exception {
        LOG.info("Closing TCP Server ...");
        this.active = false;
        this.serverSocket.close();
    }

}
