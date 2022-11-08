package io.roach.movrapi.controller;

import io.roach.movrapi.clients.AuthClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;

import static io.roach.movrapi.util.TestHelpers.createArbitraryMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AccessControllerTest {

    @Mock
    private AuthClient mockAuthClient;

    private AccessController accessController;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        accessController = new AccessController(mockAuthClient);
    }

    @Test
    public void login_shouldCallTheAuthClient() {
        Map<String, Object> expectedRequest = createArbitraryMap();
        Map<String, Object> expectedResponse = createArbitraryMap();

        when(mockAuthClient.login(expectedRequest)).thenReturn(expectedResponse);

        Map<String, Object> response = accessController.login(expectedRequest).getBody();

        verify(mockAuthClient).login(expectedRequest);
        assertEquals(expectedResponse, response);
    }
}