package io.roach.movrapi.controller;

import io.roach.movrapi.clients.RidesClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static io.roach.movrapi.util.TestHelpers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RideControllerTest {

    @Mock
    private RidesClient mockRidesClient;

    private RideController rideController;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        rideController = new RideController(mockRidesClient);
    }

    @Test
    public void startRide_shouldCallTheRideClient() {
        Map<String, Object> expectedRequest = createArbitraryMap();
        Map<String, Object> expectedResponse = createArbitraryMap();

        when(mockRidesClient.startRide(expectedRequest)).thenReturn(expectedResponse);

        Map<String, Object> response = rideController.startRide(expectedRequest).getBody();

        verify(mockRidesClient).startRide(expectedRequest);

        assertEquals(expectedResponse, response);
    }

    @Test
    public void endRide_shouldCallTheRideClient() {
        Map<String, Object> expectedRequest = createArbitraryMap();
        Map<String, Object> expectedResponse = createArbitraryMap();

        when(mockRidesClient.endRide(expectedRequest)).thenReturn(expectedResponse);

        Map<String, Object> response = rideController.endRide(expectedRequest).getBody();

        verify(mockRidesClient).endRide(expectedRequest);

        assertEquals(expectedResponse, response);
    }

    @Test
    public void getRides_shouldCallTheRideClient() {
        String email = createEmail();
        List<Map<String, Object>> expectedResponse = IntStream.range(0, 5).mapToObj(i -> createArbitraryMap()).collect(Collectors.toList());

        when(mockRidesClient.getRides(email)).thenReturn(expectedResponse);

        List<Map<String, Object>> response = rideController.getRides(email).getBody();

        verify(mockRidesClient).getRides(email);

        assertEquals(expectedResponse, response);
    }

}