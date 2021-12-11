package one.microproject.proxyserver;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import one.microproject.proxyserver.impl.Configuration;
import one.microproject.proxyserver.impl.UDPProxyImpl;
import one.microproject.proxyserver.impl.TCPProxyImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

public class Main {

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    private static final Map<String, ProxyServer> proxies = new ConcurrentHashMap<>();
    private static final CountDownLatch cl = new CountDownLatch(1);

    public static void main(String[] args) throws IOException, InterruptedException {
        LOG.info("Loading configuration from: {}", args[0]);
        Runtime.getRuntime().addShutdownHook(new Thread(Main::stopAll));
        try(FileInputStream configFile = new FileInputStream(args[0])) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            Configuration configuration = mapper.readValue(configFile, Configuration.class);
            startAll(configuration);
        }
        cl.await();
        LOG.info("Main terminated !");
    }

    public static void startAll(Configuration configuration) throws IOException {
        LOG.info("Starting Proxy Server id={} name=\"{}\" proxies={}", configuration.id(), configuration.name(), configuration.proxies().size());
        configuration.proxies().forEach(c -> {
            LOG.info("Proxy Config: {} {}:{} -> {}:{} maxConnections={}", c.protocol(), c.serverHost(), c.serverPort(), c.targetHost(), c.targetPort(), c.maxConnections());
            try {
                String protocol = c.protocol().trim().toUpperCase();
                ProxyServer proxyServer = null;
                if ("UDP".equals(protocol)) {
                    proxyServer = new UDPProxyImpl(c);
                } else if ("TCP".equals(protocol)) {
                    proxyServer = new TCPProxyImpl(c);
                } else {
                    throw new UnsupportedOperationException("Unsupported protocol type: " + c.protocol());
                }
                String id = UUID.randomUUID().toString();
                proxyServer.start();
                proxies.put(id, proxyServer);
            } catch (IOException e) {
                LOG.error("Proxy Server start ERROR: ", e);
            } catch (UnsupportedOperationException e) {
                LOG.error(e.getMessage());
            }
        });
    }

    public static void stopAll() {
        try {
            LOG.info("Proxy Server shutdown triggered !");
            cl.countDown();
            proxies.forEach((k,v) -> {
                try {
                    LOG.info("Closing Proxy Server {}", k);
                    v.close();
                } catch (Exception e) {
                    LOG.error("Proxy Server close ERROR: ", e);
                }
            });
            LOG.info("done.");
        } catch (Exception e) {
            LOG.error("Proxy Server shutdown ERROR: ", e);
        }
    }

}
