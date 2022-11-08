package io.roach.movrapi.dto;

import io.roach.movrapi.entity.Vehicle;
import org.junit.jupiter.api.Test;

import static io.roach.movrapi.util.TestHelpers.createVehicle;
import static org.junit.jupiter.api.Assertions.*;

class VehicleIdDTOTest {
    @Test
    public void fromVehicle_shouldReturnAVehicleIdDTO() {
        Vehicle vehicle = createVehicle();

        VehicleIdDTO dto = VehicleIdDTO.fromVehicle(vehicle);

        assertEquals(vehicle.getId(), dto.getId());
    }
}