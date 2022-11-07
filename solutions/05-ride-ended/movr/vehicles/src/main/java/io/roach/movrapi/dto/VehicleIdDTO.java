package io.roach.movrapi.dto;

import io.roach.movrapi.entity.Vehicle;
import java.util.UUID;

public class VehicleIdDTO {
    public static VehicleIdDTO fromVehicle(Vehicle vehicle) {
        VehicleIdDTO dto = new VehicleIdDTO();
        dto.setId(vehicle.getId());
        return dto;
    }

    private UUID id;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
