package io.roach.movrapi.clients;

import io.roach.movrapi.config.UserConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static io.roach.movrapi.util.TestHelpers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserClientTest {
    @Mock
    private RestTemplate mockRestTemplate;
    private UserConfig userConfig;
    private UserClient userClient;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        userConfig = new UserConfig("localhost", 1234);
        userClient = new UserClient(mockRestTemplate, userConfig);
    }

    @Test
    public void addUser_shouldPostToTheUserService() {
        Map<String, Object> expectedResponse = createArbitraryMap();

        when(mockRestTemplate.postForObject(anyString(), any(), any())).thenReturn(expectedResponse);

        Map<String, Object> response = userClient.addUser(expectedResponse);

        verify(mockRestTemplate).postForObject(
                userConfig.uri("/api/users"),
                expectedResponse,
                Map.class
        );

        assertEquals(expectedResponse, response);
    }

    @Test
    public void getUser_shouldGetFromTheUserService() {
        String email = createEmail();
        Map<String, Object> expectedResponse = createArbitraryMap();

        when(mockRestTemplate.getForObject(
                userConfig.uri(String.format("/api/users/%s", email)),
                Map.class)
        ).thenReturn(expectedResponse);

        Map<String, Object> response = userClient.getUser(email);

        verify(mockRestTemplate).getForObject(
                userConfig.uri(String.format("/api/users/%s", email)),
                Map.class
        );

        assertEquals(expectedResponse, response);
    }

    @Test
    public void deleteUser_shouldDeleteFromTheUserService() {
        String email = createEmail();
        Map<String, Object> expectedResponse = createArbitraryMap();

        when(mockRestTemplate.exchange(
                userConfig.uri(String.format("/api/users/%s", email)),
                HttpMethod.DELETE,
                null,
                Map.class)
        ).thenReturn(ResponseEntity.ok(expectedResponse));

        Map<String, Object> response = userClient.deleteUser(email);

        verify(mockRestTemplate).exchange(
                userConfig.uri(String.format("/api/users/%s", email)),
                HttpMethod.DELETE,
                null,
                Map.class);

        assertEquals(expectedResponse, response);
    }
}