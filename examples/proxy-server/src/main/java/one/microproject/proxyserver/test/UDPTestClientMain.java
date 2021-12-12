package one.microproject.proxyserver.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class UDPTestClientMain {

    private static final Logger LOG = LoggerFactory.getLogger(UDPTestClientMain.class);

    private static UDPClient udpClient;

    public static void main(String[] args) throws IOException {
        Runtime.getRuntime().addShutdownHook(new Thread(UDPTestClientMain::stopAll));
        String host = Constants.getHost(args);
        Integer port = Constants.getPort(args, 30333);
        LOG.info("Starting TEST UDP Client {}:{}", host, port);
        udpClient = new UDPClient(host, port);
        udpClient.connect();
        String request = "hi";
        for (int i=0; i<100; i++) {
            String response = udpClient.sendAndWaitForResponse(request + i);
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
            LOG.info("Stopping TEST UDP Client");
            udpClient.close();
        } catch (Exception e) {
            LOG.error("TEST UDP Client stop ERROR:", e);
        }
    }

}
