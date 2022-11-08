package io.roach.movrapi.util;

import io.roach.movrapi.exception.InvalidUUIDException;
import io.roach.movrapi.exception.InvalidValueException;

import java.util.UUID;

import static io.roach.movrapi.util.Constants.*;

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

    public static Integer convertBatteryToInt(String batteryLevel) throws InvalidValueException {

        Integer battery;
        try {
            battery = Integer.parseInt(batteryLevel);
        } catch (NumberFormatException e) {
            throw new InvalidValueException(ERR_BATTERY_INVALID);
        }
        if ((battery < 0) || (battery > 100)) {
            throw new InvalidValueException(ERR_BATTERY_INVALID);
        }
        return battery;
    }

    public static Double convertLatToDouble(String latitude) throws InvalidValueException {

        Double lat;
        try {
            lat = Double.parseDouble(latitude);
        } catch (NumberFormatException e) {
            throw new InvalidValueException(ERR_LAT_INVALID);
        }
        if ((lat < -90) || (lat > 90)) {
            throw new InvalidValueException(ERR_LAT_INVALID);
        }
        return lat;
    }

    public static Double convertLonToDouble(String longitude) throws InvalidValueException {

        Double lon;
        try {
            lon = Double.parseDouble(longitude);
        } catch (NumberFormatException e) {
            throw new InvalidValueException(ERR_LON_INVALID);
        }
        if ((lon < -180) || (lon > 180)) {
            throw new InvalidValueException(ERR_LON_INVALID);
        }
        return lon;
    }
}
