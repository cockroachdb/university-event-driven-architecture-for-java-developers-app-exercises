package io.roach.movrapi.clients;

import io.roach.movrapi.config.AuthConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class AuthClient {

    private RestTemplate restTemplate;
    private AuthConfig config;

    @Autowired
    public AuthClient(RestTemplate restTemplate, AuthConfig config) {
        this.restTemplate = restTemplate;
        this.config = config;
    }

    public Map<String, Object> login(Map<String, Object> credentialsDTO) {
        String uri = config.uri("/api/auth/login");
        Map<String, Object> response = restTemplate.postForObject(uri, credentialsDTO, Map.class);
        return response;
    }
}
