package io.roach.movrapi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("auth-service")
public class AuthConfig extends ServiceConfig {
    public AuthConfig() {
        super();
    }

    public AuthConfig(String host, int port) {
        super(host, port);
    }
}
