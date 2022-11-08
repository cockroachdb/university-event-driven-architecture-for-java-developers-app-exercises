package io.roach.movrapi.util;

import io.roach.movrapi.exception.InvalidUUIDException;

import java.util.UUID;

/**
 * Utility class to handle common operations
 */

public final class Common {

    private Common() {}

    // converts String to UUID and throws exception if string is not a valid UUID
    public static UUID toUUID(String id, String errMsg) throws InvalidUUIDException {
        UUID uuid;
        try {
            uuid = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new InvalidUUIDException(String.format(errMsg, id, e.getMessage()));
        }
        return uuid;
    }
}
