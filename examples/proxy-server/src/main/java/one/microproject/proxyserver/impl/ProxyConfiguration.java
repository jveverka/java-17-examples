package one.microproject.proxyserver.impl;

public record ProxyConfiguration(String protocol, String serverHost, Integer serverPort, String targetHost, Integer targetPort, Integer maxConnections) {
}
