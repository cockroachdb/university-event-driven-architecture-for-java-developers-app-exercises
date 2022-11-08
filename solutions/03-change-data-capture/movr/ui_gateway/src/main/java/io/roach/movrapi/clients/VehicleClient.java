package io.roach.movrapi.clients;

import io.roach.movrapi.config.VehicleConfig;
import io.roach.movrapi.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class VehicleClient {
    private RestTemplate restTemplate;
    private VehicleConfig config;

    @Autowired
    public VehicleClient(RestTemplate restTemplate, VehicleConfig config) {
        this.restTemplate = restTemplate;
        this.config = config;
    }

    public Map<String, Object> addVehicle(Map<String, Object> newVehicleDTO) {
        String uri = config.uri("/api/vehicles");
        Map<String, Object> response = restTemplate.postForObject(uri, newVehicleDTO, Map.class);
        return response;
    }

    public Map<String, Object> removeVehicle(UUID vehicleId) {
        String uri = config.uri(String.format("/api/vehicles/%s", vehicleId));
        // Using an exchange instead of a delete because it allows me to return a body. Delete just returns void.
        Map<String, Object> response = restTemplate.exchange(uri, HttpMethod.DELETE, null, Map.class).getBody();
        return response;
    }

    public List<Map<String, Object>> getVehiclesWithLocation(Integer maxVehicles) {
        String uri = config.uri(String.format("/api/vehicles?max_vehicles=%s", maxVehicles));
        Map<String, Object>[] response = restTemplate.getForObject(uri, Map[].class);
        return Arrays.asList(response);
    }

    public Map<String, Object> getVehicleWithLocation(UUID vehicleId) {
        String uri = config.uri(String.format("/api/vehicles/location/%s", vehicleId.toString()));
        Map<String, Object> response = restTemplate.getForObject(uri, Map.class);
        return response;
    }

    public Map<String, Object> getVehicleWithHistory(UUID vehicleId) {
        String uri = config.uri(String.format("/api/vehicles/%s", vehicleId.toString()));
        Map<String, Object> response = restTemplate.getForObject(uri, Map.class);
        return response;
    }
}
