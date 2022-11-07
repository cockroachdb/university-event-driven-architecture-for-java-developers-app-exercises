package io.roach.movrapi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("rides-service")
public class RidesConfig extends ServiceConfig {
    public RidesConfig() {
        super();
    }

    public RidesConfig(String host, int port) {
        super(host, port);
    }
}
