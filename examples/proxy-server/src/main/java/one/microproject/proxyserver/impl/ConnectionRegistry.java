package one.microproject.proxyserver.impl;

public interface ConnectionRegistry {

    void register(TcpActiveConnection activeConnection);

    int getActiveConnections();

    void unregister(String id);

}
