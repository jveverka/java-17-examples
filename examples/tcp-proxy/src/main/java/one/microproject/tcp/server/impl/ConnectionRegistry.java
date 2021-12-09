package one.microproject.tcp.server.impl;

public interface ConnectionRegistry {

    void register(ActiveConnection activeConnection);

    void unregister(String id);

}
