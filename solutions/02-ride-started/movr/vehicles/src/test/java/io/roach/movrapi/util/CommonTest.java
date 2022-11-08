package io.roach.movrapi.util;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.roach.movrapi.exception.DeserializationException;
import io.roach.movrapi.exception.InvalidValueException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 *  Misc math tests
 */

public class CommonTest {

    // create known time interval (30 minutes)
    private static final long TEST_DURATION_MINUTES = 30;
    private static final LocalDateTime TEST_START_DATE_TIME =
        LocalDateTime.of(2020, 10, 30, 12, 00);
    private static final LocalDateTime TEST_END_DATE_TIME = TEST_START_DATE_TIME.plusMinutes(TEST_DURATION_MINUTES);

    // known distance between the following two lat/lon pairs (based on existing python app calc)
    private static final double TEST_DISTANCE = 48.31d;
    private static final double TEST_START_LAT = 40.58901;
    private static final double TEST_START_LON = -74.4754;
    private static final double TEST_END_LAT = 40.73061;
    private static final double TEST_END_LON = -73.935242;

    @Test
    public void calculateDistance_shouldReturnTheDistanceBetweenTwoPoints() {

        double testDistance = Common.calculateDistance(TEST_START_LAT, TEST_START_LON, TEST_END_LAT, TEST_END_LON);
        assertEquals(TEST_DISTANCE, testDistance);

    }

    @Test
    public void calculateDurationMinutes_shouldReturnTheMinutesBetweenTwoTimestamps() {

        double minutes = Common.calculateDurationMinutes(TEST_START_DATE_TIME, TEST_END_DATE_TIME);

        assertEquals(Double.valueOf(TEST_DURATION_MINUTES), minutes);
    }

    @Test
    public void calculateVelocity_shouldReturnTheVelocityBetweenTwoPoints() throws InvalidValueException {

        double speed = TEST_DISTANCE / (TEST_DURATION_MINUTES / 60d);

        double calculatedVelocity =
            Common.calculateVelocity(TEST_START_LAT, TEST_START_LON, TEST_START_DATE_TIME, TEST_END_LAT, TEST_END_LON, TEST_END_DATE_TIME);

        assertEquals(speed, calculatedVelocity);
    }

    @Test
    public void convertBatteryToInt_shouldReturnTheIntegerValue_ifTheValueIsValid() throws InvalidValueException {

        String batteryStr = "45";
        Integer battery = 45;
        assertEquals(battery, Common.convertBatteryToInt(batteryStr));
    }

    @Test
    public void convertBatteryToInt_shouldThrowAnException_ifTheValueIsNotAnInteger() {
        assertThrows(InvalidValueException.class, () -> Common.convertBatteryToInt("garbage"));
    }

    @Test
    public void convertBatteryToInt_shouldThrowAnException_ifTheValueIsOutsideTheValidRange() {
        assertThrows(InvalidValueException.class, () -> Common.convertBatteryToInt("-5"));
        assertThrows(InvalidValueException.class, () -> Common.convertBatteryToInt("110"));
    }

    @Test
    public void convertLatToDouble_shouldReturnTheDoubleValue_ifTheValueIsValid() throws InvalidValueException {
        String latStr = "45.5";
        Double latitude = 45.5d;
        assertEquals(latitude, Common.convertLatToDouble(latStr));
    }

    @Test
    public void convertLatToDouble_shouldThrowAnException_ifTheValueIsNotADouble() {
        assertThrows(InvalidValueException.class, () -> Common.convertLatToDouble("garbage"));
    }

    @Test
    public void convertLatToDouble_shouldThrowAnException_ifTheValueOutsideTheValidRange() {
        assertThrows(InvalidValueException.class, () -> Common.convertLatToDouble("-94"));
        assertThrows(InvalidValueException.class, () -> Common.convertLatToDouble("100"));
    }

    @Test
    public void convertLanToDouble_shouldReturnTheDoubleValue_ifTheValueIsValid() throws InvalidValueException {

        String lonStr = "-73.5";
        Double longitude = -73.5d;
        assertEquals(longitude, Common.convertLonToDouble(lonStr));
    }

    @Test
    public void convertLanToDouble_shouldThrowAnException_ifTheValueIsNotADouble() {

        assertThrows(InvalidValueException.class, () -> Common.convertLonToDouble("garbage"));
    }

    @Test
    public void convertLanToDouble_shouldThrowAnException_ifTheValueIsOutsideTheValidRange() {
        assertThrows(InvalidValueException.class, () -> Common.convertLonToDouble("-194"));
        assertThrows(InvalidValueException.class, () -> Common.convertLonToDouble("310"));
    }

    @Test
    public void validateSerialNumber_shouldReturnTrue_ifTheSerialNumberIsAValidInteger() throws InvalidValueException {
        assertTrue(Common.validateSerialNumber("1234"));
    }

    @Test
    public void validateSerialNumber_shouldThrowAnException_ifTheSerialNumberIsNotAnInteger() {
        assertThrows(InvalidValueException.class, () -> Common.validateSerialNumber("Invalid"));
    }

    @Test
    public void validateSerialNumber_shouldThrowAnException_ifTheSerialNumberIsOutsideTheValidRange() {
        assertThrows(InvalidValueException.class, () -> Common.validateSerialNumber("111111111111111111111"));
        assertThrows(InvalidValueException.class, () -> Common.validateSerialNumber("-111111111111111111111"));
    }

    public static class DeserializationTestClass {
        private String stringField;
        private int intField;

        public String getStringField() {
            return stringField;
        }

        public void setStringField(String stringField) {
            this.stringField = stringField;
        }

        public int getIntField() {
            return intField;
        }

        public void setIntField(int intField) {
            this.intField = intField;
        }
    }

    @Test
    public void deserialize_shouldThrowAnException_ifTheObjectCantBeDeserialized() {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> data = new HashMap<>();
        data.put("invalidField", "someValue");
        assertThrows(DeserializationException.class, () -> Common.deserialize(mapper, data, DeserializationTestClass.class));
    }

    @Test
    public void deserialize_shouldReturnTheObject_ifTheObjectIsValid() throws DeserializationException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> data = new HashMap<>();
        data.put("stringField", "value");
        data.put("intField", 5);

        DeserializationTestClass result = Common.deserialize(mapper, data, DeserializationTestClass.class);

        assertEquals("value", result.getStringField());
        assertEquals(5, result.getIntField());
    }
}
