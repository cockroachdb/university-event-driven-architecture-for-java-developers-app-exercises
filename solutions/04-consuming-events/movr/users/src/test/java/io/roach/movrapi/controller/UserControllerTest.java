package io.roach.movrapi.controller;

import io.roach.movrapi.dto.UserDTO;
import io.roach.movrapi.entity.User;
import io.roach.movrapi.exception.NotFoundException;
import io.roach.movrapi.exception.UserAlreadyExistsException;
import io.roach.movrapi.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static io.roach.movrapi.util.TestHelpers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {
    @Mock
    UserService userService;
    UserController controller;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        controller = new UserController(userService);
    }

    @Test
    void getUser_shouldThrowAnException_ifTheServiceFails() throws NotFoundException {
        String email = createEmail();

        when(userService.getUser(email)).thenThrow(new NotFoundException("BOOM"));

        assertThrows(NotFoundException.class, () -> {
            controller.getUser(email);
        });
    }

    @Test
    void getUser_shouldReturnTheUser_ifItExists() throws NotFoundException {
        User expected = createUser();
        when(userService.getUser(expected.getEmail())).thenReturn(expected);

        UserDTO user = controller.getUser(expected.getEmail()).getBody().getUserDTO();

        verify(userService).getUser(expected.getEmail());
        assertEquals(expected.getEmail(), user.getEmail());
        assertEquals(expected.getFirstName(), user.getFirstName());
        assertEquals(expected.getLastName(), user.getLastName());
        assertArrayEquals(expected.getPhoneNumbers(), user.getPhoneNumbers().toArray(new String[0]));
    }

    @Test
    void addUser_shouldThrowAnException_ifTheServiceFails() throws UserAlreadyExistsException {
        UserDTO dto = createUserDTO();

        when(userService.addUser(dto.getEmail(), dto.getFirstName(), dto.getLastName(), dto.getPhoneNumbers().toArray(new String[0])))
                .thenThrow(new UserAlreadyExistsException("BOOM"));

        assertThrows(UserAlreadyExistsException.class, () -> {
            controller.addUser(dto);
        });
    }

    @Test
    void addUser_shouldCallAddUserOnTheService() throws UserAlreadyExistsException {
        User user = createUser();
        UserDTO dto = toUserDTO(user);

        when(userService.addUser(dto.getEmail(), dto.getFirstName(), dto.getLastName(), dto.getPhoneNumbers().toArray(new String[0])))
                .thenReturn(user);

        UserDTO result = controller.addUser(dto).getBody();

        verify(userService).addUser(dto.getEmail(), dto.getFirstName(), dto.getLastName(), dto.getPhoneNumbers().toArray(new String[0]));
        assertEquals(dto.getEmail(), result.getEmail());
        assertEquals(dto.getFirstName(), result.getFirstName());
        assertEquals(dto.getLastName(), result.getLastName());
        assertArrayEquals(dto.getPhoneNumbers().toArray(), result.getPhoneNumbers().toArray());
    }

    @Test
    void deleteUser_shouldThrowAnException_ifTheServiceFails() throws NotFoundException {
        String email = createEmail();

        doThrow(new NotFoundException("BOOM")).when(userService).deleteUser(email);

        assertThrows(NotFoundException.class, () -> {
            controller.deleteUser(email);
        });
    }

    @Test
    void deleteUser_shouldCallDeleteUserOnTheService() throws NotFoundException {
        String email = createEmail();

        controller.deleteUser(email);

        verify(userService).deleteUser(email);
    }
}