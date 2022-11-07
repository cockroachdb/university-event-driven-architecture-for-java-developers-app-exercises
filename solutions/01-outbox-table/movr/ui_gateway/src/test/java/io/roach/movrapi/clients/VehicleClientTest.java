package io.roach.movrapi.clients;

import io.roach.movrapi.config.VehicleConfig;
import io.roach.movrapi.dto.*;
import org.aspectj.bridge.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static io.roach.movrapi.util.TestHelpers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class VehicleClientTest {
    @Mock
    private RestTemplate mockRestTemplate;
    private VehicleConfig vehicleConfig;
    private VehicleClient vehicleClient;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        vehicleConfig = new VehicleConfig("localhost", 1234);
        vehicleClient = new VehicleClient(mockRestTemplate, vehicleConfig);
    }

    @Test
    public void addVehicle_shouldPostToTheVehicleService() {
        Map<String, Object> expectedRequest = createArbitraryMap();
        Map<String, Object> expectedResponse = createArbitraryMap();

        when(mockRestTemplate.postForObject(anyString(), any(), any())).thenReturn(expectedResponse);

        Map<String, Object> response = vehicleClient.addVehicle(expectedRequest);

        verify(mockRestTemplate).postForObject(
                vehicleConfig.uri("/api/vehicles"),
                expectedRequest,
                Map.class
        );

        assertEquals(expectedResponse, response);
    }

    @Test
    public void removeVehicle_shouldPostToTheVehicleService() {
        UUID vehicleId = createVehicleId();
        Map<String, Object> expectedResponse = createArbitraryMap();

        when(mockRestTemplate.exchange(
                vehicleConfig.uri(String.format("%s/%s", "/api/vehicles", vehicleId)),
                HttpMethod.DELETE,
                null,
                Map.class)).thenReturn(ResponseEntity.ok(expectedResponse));

        Map<String, Object> response = vehicleClient.removeVehicle(vehicleId);

        verify(mockRestTemplate).exchange(
                vehicleConfig.uri(String.format("%s/%s", "/api/vehicles", vehicleId)),
                HttpMethod.DELETE,
                null,
                Map.class);

        assertEquals(expectedResponse, response);
    }

    @Test
    public void getVehiclesWithLocation_shouldGetFromTheVehicleService() {
        int maxVehicles = 5;
        List<Map<String, Object>> expectedResponse = IntStream.range(0, maxVehicles).mapToObj(i -> createArbitraryMap()).collect(Collectors.toList());

        when(mockRestTemplate.getForObject(anyString(), any())).thenReturn(expectedResponse.toArray(new Map[0]));

        List<Map<String, Object>> response = vehicleClient.getVehiclesWithLocation(maxVehicles);

        verify(mockRestTemplate).getForObject(
                vehicleConfig.uri(String.format("%s?max_vehicles=%s", "/api/vehicles", maxVehicles)),
                Map[].class
        );

        for(int i = 0; i < maxVehicles; i++) {
            assertEquals(expectedResponse.get(i), response.get(i));
        }
    }

    @Test
    public void getVehicleWithLocation_shouldGetFromTheVehicleService() {
        UUID vehicleId = createVehicleId();
        Map<String, Object> expectedResponse = createArbitraryMap();

        when(mockRestTemplate.getForObject(anyString(), any())).thenReturn(expectedResponse);

        Map<String, Object> response = vehicleClient.getVehicleWithLocation(vehicleId);

        verify(mockRestTemplate).getForObject(
                vehicleConfig.uri(String.format("%s/%s", "/api/vehicles/location", vehicleId.toString())),
                Map.class
        );

        assertEquals(expectedResponse, response);
    }

    @Test
    public void getVehicleWithHistory_shouldGetFromTheVehicleService() {
        UUID vehicleId = createVehicleId();
        Map<String, Object> expectedResponse = createArbitraryMap();

        when(mockRestTemplate.getForObject(anyString(), any())).thenReturn(expectedResponse);

        Map<String, Object> response = vehicleClient.getVehicleWithHistory(vehicleId);

        verify(mockRestTemplate).getForObject(
                vehicleConfig.uri(String.format("%s/%s", "/api/vehicles", vehicleId.toString())),
                Map.class
        );

        assertEquals(expectedResponse, response);
    }
}