package io.roach.movrapi.util;

import io.roach.movrapi.exception.InvalidValueException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

/**
 *  Misc math tests
 */

public class CommonTest {

    @Test
    public void BatteryValuesTest() throws InvalidValueException {

        String batteryStr = "45";
        Integer battery = 45;
        assertEquals(battery, Common.convertBatteryToInt(batteryStr));

        assertThrows(InvalidValueException.class, () -> Common.convertBatteryToInt("garbage"));
        assertThrows(InvalidValueException.class, () -> Common.convertBatteryToInt("-5"));
        assertThrows(InvalidValueException.class, () -> Common.convertBatteryToInt("110"));
    }

    @Test
    public void LatValuesTest() throws InvalidValueException {

        String latStr = "45.5";
        Double latitude = 45.5d;
        assertEquals(latitude, Common.convertLatToDouble(latStr));

        assertThrows(InvalidValueException.class, () -> Common.convertLatToDouble("garbage"));
        assertThrows(InvalidValueException.class, () -> Common.convertLatToDouble("-94"));
        assertThrows(InvalidValueException.class, () -> Common.convertLatToDouble("100"));
    }

    @Test
    public void LonValuesTest() throws InvalidValueException {

        String lonStr = "-73.5";
        Double longitude = -73.5d;
        assertEquals(longitude, Common.convertLonToDouble(lonStr));

        assertThrows(InvalidValueException.class, () -> Common.convertLonToDouble("garbage"));
        assertThrows(InvalidValueException.class, () -> Common.convertLonToDouble("-194"));
        assertThrows(InvalidValueException.class, () -> Common.convertLonToDouble("310"));
    }

}
