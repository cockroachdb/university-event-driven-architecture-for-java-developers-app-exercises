package io.roach.movrapi.config;

public abstract class ServiceConfig {

    private String host;
    private int port;

    protected ServiceConfig() {
    }

    protected ServiceConfig(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String uri(String suffix) {
        return String.format("http://%s:%s%s", host, port, suffix);
    }
}
