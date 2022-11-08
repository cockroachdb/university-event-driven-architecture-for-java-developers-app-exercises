package io.roach.movrapi.clients;

import io.roach.movrapi.config.AuthConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static io.roach.movrapi.util.TestHelpers.createArbitraryMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuthClientTest {
    @Mock
    private RestTemplate mockRestTemplate;
    private AuthConfig authConfig;
    private AuthClient authClient;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        authConfig = new AuthConfig("localhost", 1234);
        authClient = new AuthClient(mockRestTemplate, authConfig);
    }

    @Test
    public void login_shouldPostToTheAuthService() {
        Map<String, Object> expectedRequest = createArbitraryMap();
        Map<String, Object> expectedResponse = createArbitraryMap();

        when(mockRestTemplate.postForObject(anyString(), any(), any())).thenReturn(expectedResponse);

        Map<String, Object> response = authClient.login(expectedRequest);

        verify(mockRestTemplate).postForObject(
                authConfig.uri("/api/auth/login"),
                expectedRequest,
                Map.class
        );
        
        assertEquals(expectedResponse, response);
    }

}