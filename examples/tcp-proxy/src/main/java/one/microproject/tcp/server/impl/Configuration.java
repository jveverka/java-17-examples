package one.microproject.tcp.server.impl;

import java.util.List;

public record Configuration(String id, String name, List<ProxyConfiguration> proxies) {
}
