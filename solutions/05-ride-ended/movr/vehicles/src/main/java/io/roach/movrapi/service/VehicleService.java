package io.roach.movrapi.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import io.roach.movrapi.dto.VehicleInfoDTO;
import io.roach.movrapi.entity.Vehicle;
import io.roach.movrapi.entity.VehicleWithLocation;
import io.roach.movrapi.exception.InvalidVehicleStateException;
import io.roach.movrapi.exception.NotFoundException;

/**
 *  Service to handle basic CRUD functions for vehicles
 */
public interface VehicleService {

    Integer MAX_VEHICLES_TO_RETURN = 20;  // default LIMIT when querying

    Vehicle addVehicle(double latitude, double longitude, int batteryLevel, VehicleInfoDTO vehicleInfo);
    void removeVehicle(UUID vehicleId) throws NotFoundException, InvalidVehicleStateException;
    List<VehicleWithLocation> getVehiclesWithLocation(Integer maxRecords);
    Vehicle getVehicle(UUID vehicleId) throws NotFoundException;
    Vehicle checkoutVehicle(UUID vehicleId, LocalDateTime timestamp) throws NotFoundException;
    Vehicle checkinVehicle(UUID vehicleId, double latitude, double longitude, int batteryLevel, LocalDateTime timestamp)
            throws NotFoundException;
}
