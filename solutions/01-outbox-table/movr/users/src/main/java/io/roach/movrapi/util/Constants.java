package io.roach.movrapi.util;

/**
 * various constants such as error messages and configuration values that are not expected to change
 */

public class Constants {

    // list of possible error messages
    public static final String ERR_USER_EMAIL_NOT_FOUND = "User email <%s> not found";
    public static final String ERR_USER_ALREADY_EXISTS = "User email <%s> already exists";

    // success messages
    public static final String MSG_DELETED_EMAIL = "You have successfully deleted your account.";

    private Constants() {
    }
}
