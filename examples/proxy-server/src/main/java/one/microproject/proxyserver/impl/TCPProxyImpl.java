package one.microproject.proxyserver.impl;

import one.microproject.proxyserver.ProxyServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TCPProxyImpl implements ProxyServer, ConnectionRegistry {

    private static final Logger LOG = LoggerFactory.getLogger(TCPProxyImpl.class);

    private final String serverHost;
    private final Integer serverPort;
    private final String targetHost;
    private final Integer targetPort;
    private final Integer maxConnections;
    private final Map<String, TcpActiveConnection> activeConnections;

    private TcpMain tcpMain;
    private ExecutorService processors;

    public TCPProxyImpl(ProxyConfiguration configuration) {
        this.serverHost = configuration.serverHost();
        this.serverPort = configuration.serverPort();
        this.targetHost = configuration.targetHost();
        this.targetPort = configuration.targetPort();
        this.maxConnections = configuration.maxConnections();
        this.activeConnections = new ConcurrentHashMap<>();
    }

    @Override
    public void start() throws IOException {
        LOG.info("Starting TCP proxy ...");
        int threadPoolSize = 1 + (maxConnections*2);
        LOG.info("Starting internal threadpool size={}", threadPoolSize);
        this.processors = Executors.newFixedThreadPool(threadPoolSize);
        this.tcpMain = new TcpMain(this, serverHost, serverPort, processors, targetHost, targetPort, maxConnections);
        processors.submit(tcpMain);
        LOG.info("TCP proxy started.");
    }

    @Override
    public void close() throws Exception {
        LOG.info("Closing TCP proxy ...");
        activeConnections.forEach((k,v) -> {
            try {
                v.close();
            } catch (Exception e) {
                LOG.error("On close Exception: ", e);
            }
        });
        tcpMain.close();
        processors.shutdown();
        processors.awaitTermination(10, TimeUnit.SECONDS);
        LOG.info("TCP proxy stopped.");
    }

    @Override
    public synchronized void register(TcpActiveConnection activeConnection) {
        activeConnections.put(activeConnection.getId(), activeConnection);
        LOG.info("Active connections: {}", activeConnections.size());
    }

    @Override
    public synchronized int getActiveConnections() {
        return activeConnections.size();
    }

    @Override
    public synchronized void unregister(String id) {
        activeConnections.remove(id);
        LOG.info("Active connections: {}", activeConnections.size());
    }

}
