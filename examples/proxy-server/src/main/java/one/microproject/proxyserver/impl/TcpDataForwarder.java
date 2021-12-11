package one.microproject.proxyserver.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class TcpDataForwarder implements Runnable, AutoCloseable {

    private static final Logger LOG = LoggerFactory.getLogger(TcpDataForwarder.class);

    private final String name;
    private final InputStream inputStream;
    private final OutputStream outputStream;
    private CloseListener closeListener;

    public TcpDataForwarder(String name, InputStream inputStream, OutputStream outputStream) {
        this.name = name;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    public void add(CloseListener closeListener) {
        this.closeListener = closeListener;
    }

    @Override
    public void run() {
        LOG.info("Forwarding data {} ...", name);
        try {
            int dataByte;
            while ((dataByte = inputStream.read()) != -1) {
                outputStream.write(dataByte);
            }
        } catch (IOException e) {
            LOG.info("DataForwarder IOException: close");
        } finally {
            try {
                close();
            } catch (Exception e) {
                LOG.info("DataForwarder IOException: on finally");
            }
        }
    }

    @Override
    public void close() throws Exception {
        LOG.info("Closing data forwarder {}", name);
        outputStream.flush();
        inputStream.close();
        outputStream.close();
        if (closeListener != null) {
            closeListener.onClose();
        }
    }
}
