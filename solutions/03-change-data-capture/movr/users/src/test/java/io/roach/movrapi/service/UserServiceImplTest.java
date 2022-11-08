package io.roach.movrapi.service;

import java.util.Optional;

import io.roach.movrapi.dao.UserRepository;
import io.roach.movrapi.entity.User;
import io.roach.movrapi.exception.NotFoundException;
import io.roach.movrapi.exception.UserAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static io.roach.movrapi.util.TestHelpers.createEmail;
import static io.roach.movrapi.util.TestHelpers.createUser;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.mockito.Mock;

import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

/**
 * Unit Tests for UserServiceImpl.class
 */


public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    private ModelMapper modelMapper = new ModelMapper();

    private UserService userService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(userRepository);
    }

    @Test()
    public void addUser_shouldFail_ifTheUserExists() {
        User expected = createUser();
        when(userRepository.findById(expected.getEmail())).thenReturn(Optional.of(expected));

        Exception exception = assertThrows(UserAlreadyExistsException.class, () -> {
            userService.addUser(expected.getEmail(), expected.getFirstName(), expected.getLastName(), expected.getPhoneNumbers());
        });

        assertEquals("User email <"+expected.getEmail()+"> already exists", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test()
    public void addUser_shouldSaveTheUser_ifItDoesntExist() throws UserAlreadyExistsException {
        User expected = createUser();
        when(userRepository.findById(expected.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(expected);

        userService.addUser(expected.getEmail(), expected.getFirstName(), expected.getLastName(), expected.getPhoneNumbers());

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());
        User user= userArgumentCaptor.getValue();
        assertEquals(expected.getEmail(), user.getEmail());
        assertEquals(expected.getFirstName(), user.getFirstName());
        assertEquals(expected.getLastName(), user.getLastName());
        assertArrayEquals(expected.getPhoneNumbers(), user.getPhoneNumbers());
    }

    @Test()
    public void getUser_shouldThrowAnException_ifTheUserDoesntExist() {
        String expectedEmail = createEmail();
        when(userRepository.findById(expectedEmail)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundException.class, () -> {
            userService.getUser(expectedEmail);
        });

        assertEquals("User email <"+expectedEmail+"> not found", exception.getMessage());
    }

    @Test()
    public void getUser_shouldReturnTheUser_ifItExists() throws NotFoundException {
        User expected = createUser();

        when(userRepository.findById(expected.getEmail())).thenReturn(Optional.of(expected));

        User user = userService.getUser(expected.getEmail());

        assertEquals(expected.getEmail(), user.getEmail());
        assertEquals(expected.getFirstName(), user.getFirstName());
        assertEquals(expected.getLastName(), user.getLastName());
        assertArrayEquals(expected.getPhoneNumbers(), user.getPhoneNumbers());
    }

    @Test
    public void deleteUser_shouldThrowAnException_ifTheUserDoesntExist() {
        String expectedEmail = createEmail();

        when(userRepository.findById(expectedEmail)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundException.class, () -> {
            userService.deleteUser(expectedEmail);
        });

        assertEquals("User email <"+expectedEmail+"> not found", exception.getMessage());
    }

    @Test
    public void deleteUser_shouldDeleteTheUser_ifItExists() throws NotFoundException {
        User expected = createUser();

        when(userRepository.findById(expected.getEmail())).thenReturn(Optional.of(expected));

        userService.deleteUser(expected.getEmail());

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).delete(userArgumentCaptor.capture());

        User user = userArgumentCaptor.getValue();
        assertEquals(expected.getEmail(), user.getEmail());
        assertEquals(expected.getFirstName(), user.getFirstName());
        assertEquals(expected.getLastName(), user.getLastName());
        assertArrayEquals(expected.getPhoneNumbers(), user.getPhoneNumbers());
    }
}
