package one.microproject.proxyserver.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TCPServerMain {

    private static final Logger LOG = LoggerFactory.getLogger(TCPServerMain.class);

    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private static TCPServer tcpServer;

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(TCPServerMain::stopAll));
        String host = Constants.getHost(args);
        Integer port = Constants.getPort(args, 2222);
        LOG.info("Starting TEST TCP Server {}:{}", host, port);
        tcpServer = new TCPServer(host, port);
        executorService.submit(tcpServer);
    }

    public static void stopAll() {
        try {
            LOG.info("Stopping TEST TCP Server");
            tcpServer.close();
        } catch (Exception e) {
            LOG.error("TEST TCP Server stop ERROR:", e);
        }
    }

}
