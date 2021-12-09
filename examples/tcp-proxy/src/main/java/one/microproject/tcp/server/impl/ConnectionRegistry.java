package one.microproject.tcp.server.impl;

public interface ConnectionRegistry {

    void register(ActiveConnection activeConnection);

    int getActiveConnections();

    void unregister(String id);

}
