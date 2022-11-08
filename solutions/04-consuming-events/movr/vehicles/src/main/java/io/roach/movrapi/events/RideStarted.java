package io.roach.movrapi.events;

import java.time.LocalDateTime;
import java.util.UUID;

public class RideStarted {
    public static final String EVENT_TYPE = "RideStarted";

    private UUID vehicleId;
    private LocalDateTime startTime;

    public UUID getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(UUID vehicleId) {
        this.vehicleId = vehicleId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
}
