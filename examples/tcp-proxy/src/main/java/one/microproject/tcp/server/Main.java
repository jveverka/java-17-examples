package one.microproject.tcp.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import one.microproject.tcp.server.impl.Configuration;
import one.microproject.tcp.server.impl.TCPProxyImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Main {

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    private static final Map<String, TcpProxy> proxies = new ConcurrentHashMap<>();

    public static void main(String[] args) throws IOException {
        LOG.info("Loading configuration from: {}", args[0]);
        ObjectMapper mapper = new ObjectMapper();
        Configuration configuration = mapper.readValue(new File(args[0]), Configuration.class);
        LOG.info("Starting TCP Proxy id={} name={} proxies={}", configuration.id(), configuration.name(), configuration.proxies().size());
        configuration.proxies().forEach(c -> {
            LOG.info("Proxy Config: {}:{} -> {}:{}", c.serverHost(), c.serverPort(), c.targetHost(), c.targetPort());
            TCPProxyImpl tcpProxy = new TCPProxyImpl(c);
            try {
                tcpProxy.start();
            } catch (IOException e) {
                LOG.error("TCP Proxy start ERROR: ", e);
            }
        });
        LOG.info("Registering shutdown hook.");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                LOG.info("TCP Proxy shutdown triggered !");
                proxies.forEach((k,v) -> {
                    try {
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
