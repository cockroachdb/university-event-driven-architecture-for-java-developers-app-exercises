package io.roach.movrapi.entity;

import org.junit.jupiter.api.Test;

import static io.roach.movrapi.util.TestHelpers.createTimestamp;
import static io.roach.movrapi.util.TestHelpers.createVehicle;
import static org.junit.jupiter.api.Assertions.*;

class VehicleTest {

    @Test
    public void getInUse_shouldReturnFalse_ifNoRideTimesRecorded() {
        Vehicle vehicle = createVehicle();
        vehicle.setLastRideStart(null);
        vehicle.setLastRideEnd(null);

        assertFalse(vehicle.getInUse());
    }

    @Test
    public void getInUse_shouldReturnFalse_ifNoStartTimeRecorded() {
        Vehicle vehicle = createVehicle();
        vehicle.setLastRideStart(null);
        vehicle.setLastRideEnd(createTimestamp());

        assertFalse(vehicle.getInUse());
    }

    @Test
    public void getInUse_shouldReturnTrue_ifNoEndTimeRecorded() {
        Vehicle vehicle = createVehicle();
        vehicle.setLastRideStart(createTimestamp());
        vehicle.setLastRideEnd(null);

        assertTrue(vehicle.getInUse());
    }

    @Test
    public void getInUse_shouldReturnTrue_ifStartTimeAfterEndTime() {
        Vehicle vehicle = createVehicle();
        vehicle.setLastRideStart(createTimestamp().plusSeconds(1000));
        vehicle.setLastRideEnd(createTimestamp());

        assertTrue(vehicle.getInUse());
    }

    @Test
    public void getInUse_shouldReturnFalse_ifStartTimeBeforeEndTime() {
        Vehicle vehicle = createVehicle();
        vehicle.setLastRideStart(createTimestamp());
        vehicle.setLastRideEnd(createTimestamp().plusSeconds(1000));

        assertFalse(vehicle.getInUse());
    }
}