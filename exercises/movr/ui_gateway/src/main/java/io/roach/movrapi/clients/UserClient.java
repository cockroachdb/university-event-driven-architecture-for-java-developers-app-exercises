package io.roach.movrapi.clients;

import io.roach.movrapi.config.UserConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class UserClient {

    private RestTemplate restTemplate;
    private UserConfig config;

    @Autowired
    public UserClient(RestTemplate restTemplate, UserConfig config) {
        this.restTemplate = restTemplate;
        this.config = config;
    }

    public Map<String, Object> addUser(Map<String, Object> userDTO) {
        String uri = config.uri("/api/users");
        Map<String, Object> response = restTemplate.postForObject(uri, userDTO, Map.class);
        return response;
    }

    public Map<String, Object> getUser(String email) {
        String uri = config.uri(String.format("/api/users/%s", email));
        Map<String, Object> response = restTemplate.getForObject(uri, Map.class);
        return response;
    }

    public Map<String, Object> deleteUser(String email) {
        String uri = config.uri(String.format("/api/users/%s", email));
        // Using an exchange instead of a delete because it allows me to return a body. Delete just returns void.
        Map<String, Object> response = restTemplate.exchange(uri, HttpMethod.DELETE, null, Map.class).getBody();
        return response;
    }
}
