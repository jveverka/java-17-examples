package one.microproject.tcp.server.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class ActiveConnection implements AutoCloseable, CloseListener {

    private static final Logger LOG = LoggerFactory.getLogger(ActiveConnection.class);

    private final String id;
    private final Socket socket;
    private final String targetHost;
    private final Integer targetPort;
    private final DataForwarder forwardPipe;
    private final DataForwarder reversePipe;
    private final ConnectionRegistry connectionRegistry;
    private final AtomicBoolean closed;

    public ActiveConnection(String id, Socket socket, String targetHost, Integer targetPort,
                            DataForwarder forwardPipe, DataForwarder reversePipe, ConnectionRegistry connectionRegistry) {
        this.id = id;
        this.socket = socket;
        this.targetHost = targetHost;
        this.targetPort = targetPort;
        this.forwardPipe = forwardPipe;
        this.reversePipe = reversePipe;
        this.connectionRegistry = connectionRegistry;
        this.forwardPipe.add(this);
        this.reversePipe.add(this);
        this.closed = new AtomicBoolean(Boolean.FALSE);
    }

    public String getId() {
        return id;
    }

    @Override
    public void close() throws Exception {
        LOG.info("Closing connection to {}:{}", targetHost, targetPort);
        connectionRegistry.unregister(this.id);
        forwardPipe.close();
        reversePipe.close();
        socket.close();
    }

    @Override
    public synchronized void onClose() {
        try {
            boolean isClosed = closed.getAndSet(Boolean.TRUE);
            if (Boolean.FALSE.equals(isClosed)) {
                close();
            }
        } catch (Exception e) {
            LOG.error("On Connection ERROR: ", e);
        }
    }

}
