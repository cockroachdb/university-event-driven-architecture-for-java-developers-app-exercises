package io.roach.movrapi.clients;

import io.roach.movrapi.config.RidesConfig;
import io.roach.movrapi.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static io.roach.movrapi.util.TestHelpers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RidesClientTest {

    @Mock
    private RestTemplate mockRestTemplate;
    private RidesConfig ridesConfig;
    private RidesClient ridesClient;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        ridesConfig = new RidesConfig("localhost", 1234);
        ridesClient = new RidesClient(mockRestTemplate, ridesConfig);
    }

    @Test
    public void startRide_shouldCallTheRidesService() {
        Map<String, Object> expectedRequest = createArbitraryMap();
        Map<String, Object> expectedResponse = createArbitraryMap();

        when(mockRestTemplate.postForObject(anyString(), any(), any())).thenReturn(expectedResponse);

        Map<String, Object> response = ridesClient.startRide(expectedRequest);

        verify(mockRestTemplate).postForObject(
                ridesConfig.uri("/api/rides/start"),
                expectedRequest,
                Map.class
        );

        assertEquals(expectedResponse, response);
    }

    @Test
    public void endRide_shouldCallTheRidesService() {
        Map<String, Object> expectedRequest = createArbitraryMap();
        Map<String, Object> expectedResponse = createArbitraryMap();

        when(mockRestTemplate.postForObject(anyString(), any(), any())).thenReturn(expectedResponse);

        Map<String, Object> response = ridesClient.endRide(expectedRequest);

        verify(mockRestTemplate).postForObject(
                ridesConfig.uri("/api/rides/end"),
                expectedRequest,
                Map.class
        );

        assertEquals(expectedResponse, response);
    }

    @Test
    public void getRides_shouldCallTheRidesService() {
        String email = createEmail();
        List<Map<String, Object>> expectedResponse = IntStream.range(0, 5).mapToObj(i -> createArbitraryMap()).collect(Collectors.toList());

        when(mockRestTemplate.getForObject(anyString(), any())).thenReturn(expectedResponse.toArray(new Map[0]));

        List<Map<String, Object>> response = ridesClient.getRides(email);

        verify(mockRestTemplate).getForObject(
                ridesConfig.uri(String.format("/api/rides?email=%s", email)),
                Map[].class
        );

        for(int i = 0; i < expectedResponse.size(); i++) {
            Map<String, Object> expected = expectedResponse.get(i);
            Map<String, Object> actual = response.get(i);

            assertEquals(expected, actual);
        }
    }

}