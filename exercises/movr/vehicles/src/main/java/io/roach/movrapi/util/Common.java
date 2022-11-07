package io.roach.movrapi.util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.roach.movrapi.exception.DeserializationException;
import io.roach.movrapi.exception.InvalidUUIDException;
import io.roach.movrapi.exception.InvalidValueException;
import static io.roach.movrapi.util.Constants.*;
import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GeodeticCurve;
import org.gavaghan.geodesy.GlobalCoordinates;

/**
 * Utility class to handle common operations
 */

public final class Common {

    private Common() {}

    private static double distance(double lat1, double lon1, double lat2, double lon2) {

        GeodeticCalculator geoCalc = new GeodeticCalculator();
        // select a reference elllipsoid
        Ellipsoid reference = Ellipsoid.WGS84;
        // set first coordinates
        GlobalCoordinates start = new GlobalCoordinates(lat1, lon1);
        // set second coordinates
        GlobalCoordinates end = new GlobalCoordinates(lat2, lon2);
        // calculate the geodetic curve
        GeodeticCurve geoCurve = geoCalc.calculateGeodeticCurve(
            reference, start, end
        );
        return geoCurve.getEllipsoidalDistance() / 1000.0;
    }

    // Finds the distance between two points, in kilometers, to a precision of 10 meters.
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        return Math.round(distance(lat1, lon1, lat2, lon2) * 100d) / 100d;
    }

    // returns the time between two timestamps, in decimal minutes
    public static double calculateDurationMinutes(LocalDateTime startTime, LocalDateTime endTime) {
        return calculateDuration(startTime, endTime).toMillis()/60000d;
    }

    private static Duration calculateDuration(LocalDateTime startTime, LocalDateTime endTime) {
        return Duration.between(startTime, endTime);
    }

    // Finds the magnitude of the velocity, in kilometers per hour
    public static double calculateVelocity(double lat1, double lon1, LocalDateTime startTime,
                                            double lat2, double lon2, LocalDateTime endTime)
        throws InvalidValueException {


        double distanceTravelled = calculateDistance(lat1, lon1, lat2, lon2);
        if (distanceTravelled == 0d) {
            return 0d;
        }
        double hoursElapsed = calculateDurationMinutes(startTime, endTime) / 60d;
        if (hoursElapsed == 0d) {
            throw new InvalidValueException(ERR_DIVIDE_BY_ZERO);
        }
        // round to 2 dec places to make it look nice :)
        return Math.round(distanceTravelled / hoursElapsed * 100d) / 100d;

    }

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

    public static boolean validateSerialNumber(String serialNumber) throws InvalidValueException {
        try {
            Integer.parseInt(serialNumber);
        } catch (NumberFormatException ex) {
            throw new InvalidValueException(ERR_SERIAL_INVALID);
        }

        return true;
    }

    public static <T> T deserialize(ObjectMapper mapper, Map<String, Object> data, Class<T> type) throws DeserializationException {
        try {
            T result = mapper.convertValue(data, type);
            return result;
        } catch (IllegalArgumentException ex) {
            throw new DeserializationException(ERR_DESERIALIZATION_FAILED + " " + ex.getMessage());
        }
    }

}
