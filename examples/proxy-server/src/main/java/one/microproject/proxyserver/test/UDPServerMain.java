package one.microproject.proxyserver.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UDPServerMain {

    private static final Logger LOG = LoggerFactory.getLogger(UDPServerMain.class);

    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private static UDPServer udpServer;

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(UDPServerMain::stopAll));
        String host = Constants.getHost(args);
        Integer port = Constants.getPort(args, 3333);
        LOG.info("Starting TEST UDP Server {}:{}", host, port);
        udpServer = new UDPServer(host, port);
        executorService.submit(udpServer);
    }

    public static void stopAll() {
        try {
            LOG.info("Stopping TEST UDP Server");
            udpServer.close();
        } catch (Exception e) {
            LOG.error("TEST UDP Server stop ERROR:", e);
        }
    }

}
