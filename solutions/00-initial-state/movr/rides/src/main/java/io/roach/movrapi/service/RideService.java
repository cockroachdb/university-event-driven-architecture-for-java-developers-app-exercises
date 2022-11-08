package io.roach.movrapi.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import io.roach.movrapi.entity.Ride;
import io.roach.movrapi.exception.InvalidVehicleStateException;
import io.roach.movrapi.exception.NotFoundException;

public interface RideService {

    Ride startRide(UUID vehicleId, String userEmail, LocalDateTime startTime)
            throws InvalidVehicleStateException;
    Ride endRide(UUID vehicleId,
                 String userEmail,
                 int battery,
                 double latitude,
                 double longitude,
                 LocalDateTime endTime) throws NotFoundException;
    List<Ride> getRidesForUser(String userEmail);
    Ride getActiveRide(UUID vehicleId, String userEmail) throws NotFoundException;

}
