package io.roach.movrapi.events;

import io.roach.movrapi.entity.Ride;

import java.time.LocalDateTime;
import java.util.UUID;

public class RideStarted implements Event {
    public static final String EVENT_NAME = "RideStarted";

    private UUID rideId;
    private String userEmail;
    private UUID vehicleId;
    private LocalDateTime startTime;

    public UUID getRideId() {
        return rideId;
    }

    public void setRideId(UUID rideId) {
        this.rideId = rideId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

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
