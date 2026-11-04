package pe.edu.utp.thanosgym.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "thanosgym.gateway")
public class GatewayProperties {
    private final Security security = new Security();
    private final Timeout timeout = new Timeout();
    private Map<String, String> routes;

    public Security getSecurity() {
        return security;
    }

    public Timeout getTimeout() {
        return timeout;
    }

    public Map<String, String> getRoutes() {
        return routes;
    }

    public void setRoutes(Map<String, String> routes) {
        this.routes = routes;
    }

    public static class Security {
        private boolean enabled = true;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

    public static class Timeout {
        private int connectMs = 2000;
        private int readMs = 3000;

        public int getConnectMs() {
            return connectMs;
        }

        public void setConnectMs(int connectMs) {
            this.connectMs = connectMs;
        }

        public int getReadMs() {
            return readMs;
        }

        public void setReadMs(int readMs) {
            this.readMs = readMs;
        }
    }
}
