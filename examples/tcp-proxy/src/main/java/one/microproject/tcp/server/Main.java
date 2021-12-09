package one.microproject.tcp.server;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import one.microproject.tcp.server.impl.Configuration;
import one.microproject.tcp.server.impl.TCPProxyImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

public class Main {

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    private static final Map<String, TcpProxy> proxies = new ConcurrentHashMap<>();
    private static final CountDownLatch cl = new CountDownLatch(1);

    public static void main(String[] args) throws IOException, InterruptedException {
        LOG.info("Loading configuration from: {}", args[0]);
        registerShutdownHook();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Configuration configuration = mapper.readValue(new File(args[0]), Configuration.class);
        LOG.info("Starting TCP Proxy id={} name=\"{}\" proxies={}", configuration.id(), configuration.name(), configuration.proxies().size());
        configuration.proxies().forEach(c -> {
            LOG.info("Proxy Config: {}:{} -> {}:{} maxConnections={}", c.serverHost(), c.serverPort(), c.targetHost(), c.targetPort(), c.maxConnections());
            TCPProxyImpl tcpProxy = new TCPProxyImpl(c);
            try {
                String id = UUID.randomUUID().toString();
                tcpProxy.start();
                proxies.put(id, tcpProxy);
            } catch (IOException e) {
                LOG.error("TCP Proxy start ERROR: ", e);
            }
        });
        cl.await();
        LOG.info("Main terminated !");
    }

    private static void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                LOG.info("TCP Proxy shutdown triggered !");
                cl.countDown();
                proxies.forEach((k,v) -> {
                    try {
                        LOG.info("Closing TCP Proxy {}", k);
                        v.close();
                    } catch (Exception e) {
                        LOG.error("TCP Proxy close ERROR: ", e);
                    }
                });
                LOG.info("done.");
            } catch (Exception e) {
                LOG.error("TCP Proxy shutdown ERROR: ", e);
            }
        }));
    }

}
