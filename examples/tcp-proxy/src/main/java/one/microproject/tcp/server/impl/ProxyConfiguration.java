package one.microproject.tcp.server.impl;

public record ProxyConfiguration(String serverHost, Integer serverPort, String targetHost, Integer targetPort) {
}
