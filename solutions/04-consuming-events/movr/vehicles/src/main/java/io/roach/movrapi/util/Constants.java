package io.roach.movrapi.util;

/**
 * various constants such as error messages and configuration values that are not expected to change
 */

public class Constants {

    // list of possible error messages
    public static final String ERR_VEHICLE_NOT_FOUND = "Vehicle id <%s> not found";
    public static final String ERR_INVALID_VEHICLE_ID = "Vehicle id <%s> is not valid id : %s";
    public static final String ERR_VEHICLE_IN_USE = "Vehicle id <%s> is currently in use";
    public static final String ERR_VEHICLE_NOT_IN_USE = "Vehicle id <%s> is not currently being used";
    public static final String ERR_DIVIDE_BY_ZERO = "Cannot calculate an average velocity when the time interval is 0.";
    public static final String ERR_BATTERY_INVALID = "Battery (percent) must be between 0 and 100.";
    public static final String ERR_LAT_INVALID = "Latitude must be between -90 and 90.";
    public static final String ERR_LON_INVALID = "Longitude must be between -180 and 180.";
    public static final String ERR_SERIAL_INVALID = "Serial Number must be a number.";
    public static final String ERR_DESERIALIZATION_FAILED = "Unable to deserialize the requested object.";

    // success messages
    public static final String MSG_DELETED_VEHICLE = "Deleted vehicle with id <%s> from database.";

    private Constants() {
    }
}
