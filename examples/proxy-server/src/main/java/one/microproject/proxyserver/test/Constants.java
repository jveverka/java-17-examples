package one.microproject.proxyserver.test;

public final class Constants {

    private Constants() {
    }

    public static final int MESSAGE_END = 0x03;
    public static final int MESSAGE_EOF = -1;

    public static String getHost(String[] args) {
        if (args.length == 0) {
            return "localhost";
        } else {
            return args[0];
        }
    }

    public static int getPort(String[] args, int defaultPort) {
        if (args.length < 2) {
            return defaultPort;
        } else {
            try {
                return Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                return defaultPort;
            }
        }
    }

}
