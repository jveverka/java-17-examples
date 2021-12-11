package one.microproject.proxyserver.impl;

import one.microproject.proxyserver.ProxyServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class UDPProxyImpl implements ProxyServer {

    private static final Logger LOG = LoggerFactory.getLogger(UDPProxyImpl.class);

    private final String serverHost;
    private final Integer serverPort;
    private final String targetHost;
    private final Integer targetPort;

    private ExecutorService processors;
    private DatagramSocket serverSocket;
    private DatagramSocket clientSocket;
    private UdpDataHandlerForward handlerForward;
    private UdpDataHandlerReverse handlerReverse;

    public UDPProxyImpl(ProxyConfiguration configuration) {
        this.serverHost = configuration.serverHost();
        this.serverPort = configuration.serverPort();
        this.targetHost = configuration.targetHost();
        this.targetPort = configuration.targetPort();
    }

    @Override
    public void start() throws IOException {
        LOG.info("Starting UDP proxy ...");
        this.processors = Executors.newFixedThreadPool(2);
        this.serverSocket = new DatagramSocket(serverPort, InetAddress.getByName(serverHost));
        this.clientSocket = new DatagramSocket();
        handlerForward = new UdpDataHandlerForward(serverSocket, clientSocket, targetHost, targetPort);
        handlerReverse = new UdpDataHandlerReverse(serverSocket, clientSocket, serverHost, serverPort);
        processors.submit(handlerForward);
        processors.submit(handlerReverse);
        LOG.info("UDP proxy started.");
    }

    @Override
    public void close() throws Exception {
        LOG.info("Closing UDP proxy ...");
        handlerForward.close();
        handlerReverse.close();
        serverSocket.close();
        clientSocket.close();
        processors.shutdown();
        processors.awaitTermination(10, TimeUnit.SECONDS);
        LOG.info("UDP proxy stopped.");
    }

}
