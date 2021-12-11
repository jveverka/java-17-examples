package one.microproject.proxyserver;

import java.io.IOException;

public interface ProxyServer extends AutoCloseable {

    void start() throws IOException;

}
