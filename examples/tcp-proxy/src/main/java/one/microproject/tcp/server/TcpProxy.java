package one.microproject.tcp.server;

import java.io.IOException;

public interface TcpProxy extends AutoCloseable {

    void start() throws IOException;

}
