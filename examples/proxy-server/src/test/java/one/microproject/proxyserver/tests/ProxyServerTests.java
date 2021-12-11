package one.microproject.proxyserver.tests;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import one.microproject.proxyserver.Main;
import one.microproject.proxyserver.impl.Configuration;
import one.microproject.proxyserver.impl.ProxyConfiguration;
import one.microproject.proxyserver.tests.tools.TCPClient;
import one.microproject.proxyserver.tests.tools.TCPServer;
import one.microproject.proxyserver.tests.tools.UDPClient;
import one.microproject.proxyserver.tests.tools.UDPServer;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProxyServerTests {

    private ExecutorService executor;
    private TCPServer tcpServer;
    private UDPServer udpServer;
    private ProxyConfiguration tcpConfig;
    private ProxyConfiguration udpConfig;

    @BeforeClass
    public void init() throws IOException {
        InputStream is = ProxyServerTests.class.getClassLoader().getResourceAsStream("proxy-server-config.json");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Configuration configuration = mapper.readValue(is, Configuration.class);
        Main.startAll(configuration);
        executor = Executors.newFixedThreadPool(32);
        Optional<ProxyConfiguration> tcpConfigOptional = configuration.proxies().stream().filter(p -> p.protocol().equals("tcp")).findFirst();
        if (tcpConfigOptional.isEmpty()) {
            throw new UnsupportedOperationException("Missing TCP Configuration !");
        } else {
            tcpConfig = tcpConfigOptional.get();
            tcpServer = new TCPServer(tcpConfig.targetHost(), tcpConfig.targetPort());
            executor.submit(tcpServer);
        }
        Optional<ProxyConfiguration> udpConfigOptional = configuration.proxies().stream().filter(p -> p.protocol().equals("udp")).findFirst();
        if (udpConfigOptional.isEmpty()) {
            throw new UnsupportedOperationException("Missing UDP Configuration !");
        } else {
            udpConfig = udpConfigOptional.get();
            udpServer = new UDPServer(udpConfig.targetHost(), udpConfig.targetPort());
            executor.submit(udpServer);
        }
    }

    @Test
    public void testTCPConnection() throws IOException, InterruptedException {
        String request = "hi";
        TCPClient tcpClient = new TCPClient(tcpConfig.serverHost(), tcpConfig.serverPort());
        tcpClient.connect();
        for (int i=0; i<100; i++) {
            String response = tcpClient.sendAndWaitForResponse(request + i);
            assertEquals((request + i), response);
        }
        tcpClient.close();
    }

    @Test
    public void testUDPConnection() throws IOException, InterruptedException {
        String request = "hi";
        UDPClient udpClient = new UDPClient(tcpConfig.serverHost(), tcpConfig.serverPort());
        udpClient.connect();
        for (int i=0; i<100; i++) {
            String response = udpClient.sendAndWaitForResponse(request + i);
            assertEquals((request + i), response);
        }
        udpClient.close();
    }

    @AfterClass
    public void shutdown() throws InterruptedException {
        Main.stopAll();
        if (tcpServer != null) {
            try {
                tcpServer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (udpServer != null) {
            try {
                udpServer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (executor != null) {
            executor.shutdown();
            executor.awaitTermination(10, TimeUnit.SECONDS);
        }
    }

}
