package io.roach.movrapi.controller;

import io.roach.movrapi.dto.CredentialsDTO;
import io.roach.movrapi.entity.User;
import io.roach.movrapi.exception.NotFoundException;
import io.roach.movrapi.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static io.roach.movrapi.util.TestHelpers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AccessControllerTest {

    @Mock
    UserService userService;
    AccessController controller;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        controller = new AccessController(userService);
    }

    @Test
    void login_shouldThrowAnException_ifTheServiceFails() throws NotFoundException {
        CredentialsDTO dto = createCredentialsDTO();

        when(userService.getUser(dto.getEmail())).thenThrow(new NotFoundException("BOOM"));

        assertThrows(NotFoundException.class, () -> {
            controller.login(dto);
        });
    }

    @Test
    void login_shouldAuthenticateTheUser_ifItExists() throws NotFoundException {
        User user = createUser();
        CredentialsDTO dto = new CredentialsDTO();
        dto.setEmail(user.getEmail());

        when(userService.getUser(dto.getEmail())).thenReturn(user);

        Boolean authenticated = controller.login(dto).getBody().isAuthenticated();

        verify(userService).getUser(dto.getEmail());
        assertTrue(authenticated);
    }
}