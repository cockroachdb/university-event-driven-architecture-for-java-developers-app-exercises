package io.roach.movrapi.util;

/**
 * various constants such as error messages and configuration values that are not expected to change
 */

public class Constants {

    // list of possible error messages
    public static final String ERR_INVALID_VEHICLE_ID = "Vehicle id <%s> is not valid id : %s";
    public static final String ERR_NO_ACTIVE_RIDE = "No active ride for this vehicle <%s> and user <%s> combination.";
    public static final String ERR_SERIALIZATION_FAILED = "Unable to serialize the requested object.";
    public static final String ERR_BATTERY_INVALID = "Battery (percent) must be between 0 and 100.";
    public static final String ERR_LAT_INVALID = "Latitude must be between -90 and 90.";
    public static final String ERR_LON_INVALID = "Longitude must be between -180 and 180.";
    public static final String ERR_VEHICLE_IN_USE = "Vehicle id <%s> is currently in use";

    // success messages
    public static final String MSG_RIDE_STARTED = "Ride started with vehicle %s";
    public static final String MSG_RIDE_ENDED = "You have completed your ride on vehicle %s.";

    
    private Constants() {
    }
}
