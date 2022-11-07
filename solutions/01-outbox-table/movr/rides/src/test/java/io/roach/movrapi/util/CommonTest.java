package io.roach.movrapi.util;

import io.roach.movrapi.exception.InvalidValueException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 *  Misc math tests
 */

public class CommonTest {
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
}
