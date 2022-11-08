package io.roach.movrapi.util;

import io.roach.movrapi.dto.CredentialsDTO;
import io.roach.movrapi.dto.UserDTO;
import io.roach.movrapi.entity.User;

import java.util.Arrays;
import java.util.Random;

public class TestHelpers {
    private static Random rnd = new Random();

    public static User createUser() {
        User user = new User();
        user.setEmail(createEmail());
        user.setFirstName(createFirstName());
        user.setLastName(createLastName());
        user.setPhoneNumbers(new String[]{ createPhoneNumber() , createPhoneNumber() });
        return user;
    }

    public static UserDTO createUserDTO() {
        UserDTO dto = new UserDTO();
        dto.setEmail(createEmail());
        dto.setFirstName(createFirstName());
        dto.setLastName(createLastName());
        dto.setPhoneNumbers(Arrays.asList(createPhoneNumber(), createPhoneNumber(), createPhoneNumber()));
        return dto;
    }

    public static UserDTO toUserDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setPhoneNumbers(Arrays.asList(user.getPhoneNumbers()));
        return dto;
    }

    public static CredentialsDTO createCredentialsDTO() {
        CredentialsDTO dto = new CredentialsDTO();
        dto.setEmail(createEmail());
        return dto;
    }

    private static String rndString(String prefix) {
        return prefix+rnd.nextInt(1000000);
    }

    public static String createEmail() {
        return rndString("email")+"@email.com";
    }

    public static String createFirstName() {
        return rndString("FirstName");
    }

    public static String createLastName() {
        return rndString("LastName");
    }

    public static String createPhoneNumber() {
        return rnd.nextInt(999)+"-"+rnd.nextInt(999)+"-"+rnd.nextInt(9999);
    }
}
