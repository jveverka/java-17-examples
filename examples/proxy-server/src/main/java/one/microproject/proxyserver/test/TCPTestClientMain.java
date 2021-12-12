package one.microproject.proxyserver.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class TCPTestClientMain {

    private static final Logger LOG = LoggerFactory.getLogger(TCPTestClientMain.class);

    private static TCPClient tcpClient;

    public static void main(String[] args) throws IOException, InterruptedException {
        Runtime.getRuntime().addShutdownHook(new Thread(TCPTestClientMain::stopAll));
        String host = Constants.getHost(args);
        Integer port = Constants.getPort(args, 20222);
        LOG.info("Starting TEST TCP Client {}:{}", host, port);
        tcpClient = new TCPClient(host, port);
        tcpClient.connect();
        String request = "hi";
        for (int i=0; i<100; i++) {
            String response = tcpClient.sendAndWaitForResponse(request + i);
            if (response.equals(request + i)) {
                LOG.info("TEST {} OK !", i);
            } else {
                LOG.error("TEST {} FAILED !", i);
            }
        }
        LOG.info("done.");
    }

    public static void stopAll() {
        try {
            LOG.info("Stopping TEST TCP Client");
            tcpClient.close();
        } catch (Exception e) {
            LOG.error("TEST TCP Client stop ERROR:", e);
        }
    }

}
