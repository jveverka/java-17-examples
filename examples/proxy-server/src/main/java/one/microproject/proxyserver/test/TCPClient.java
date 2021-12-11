package one.microproject.proxyserver.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static one.microproject.proxyserver.test.Constants.MESSAGE_END;
import static one.microproject.proxyserver.test.Constants.MESSAGE_EOF;

public class TCPClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(TCPClient.class);

    private final String host;
    private final Integer port;
    private Socket socket;
    private ExecutorService executor;

    public TCPClient(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    public void connect() throws IOException {
        LOGGER.info("Starting TEST TCP Client: {}:{}", host, port);
        socket = new Socket(host, port);
        executor = Executors.newSingleThreadExecutor();
    }

    public String sendAndWaitForResponse(String message) throws IOException, InterruptedException {
        LOGGER.info("TEST TCP Client: {}:{} - sending data", host, port);
        SocketListener socketListener = new SocketListener(socket.getInputStream());
        executor.submit(socketListener);
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(message.getBytes(StandardCharsets.UTF_8));
        outputStream.write(MESSAGE_END);
        outputStream.flush();
        return new String(socketListener.waitAndGetResponse(), StandardCharsets.UTF_8);
    }

    public void close() throws IOException {
        socket.close();
        executor.shutdown();
    }

    private static class SocketListener implements Runnable {
        private final InputStream in;
        private final CountDownLatch cl;
        private byte[] response;
        private SocketListener(InputStream in) {
            this.in = in;
            this.cl = new CountDownLatch(1);
        }
        private byte[] waitAndGetResponse() throws InterruptedException {
            cl.await();
            return response;
        }
        @Override
        public void run() {
            try {
                int nextByte = 0;
                ByteArrayOutputStream messageBuffer = new ByteArrayOutputStream(1024);
                while (nextByte != MESSAGE_EOF) {
                    nextByte = in.read();
                    switch (nextByte) {
                        case MESSAGE_END:
                            messageBuffer.flush();
                            response = messageBuffer.toByteArray();
                            this.cl.countDown();
                            return;
                        default:
                            messageBuffer.write(nextByte);
                    }
                }
            } catch (Exception e) {
                throw new UnsupportedOperationException(e);
            }
        }
    }
}
