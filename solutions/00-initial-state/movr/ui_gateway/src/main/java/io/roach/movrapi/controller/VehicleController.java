package io.roach.movrapi.controller;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.Map;

import io.roach.movrapi.clients.VehicleClient;
import io.roach.movrapi.dto.*;
import io.roach.movrapi.util.Common;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import io.roach.movrapi.exception.InvalidUUIDException;
import io.roach.movrapi.exception.InvalidValueException;
import io.roach.movrapi.exception.InvalidVehicleStateException;
import io.roach.movrapi.exception.NotFoundException;

import static io.roach.movrapi.util.Common.toUUID;
import static io.roach.movrapi.util.Constants.ERR_INVALID_VEHICLE_ID;

/**
 * REST Controller to manage basic vehicle activities
 */

@RestController
@RequestMapping("/ui")
@Validated
public class VehicleController {

    private VehicleClient vehicleClient;
    private final Logger logger = LogManager.getLogger(this.getClass());

    @Autowired
    public VehicleController(VehicleClient vehicleClient) {
        this.vehicleClient = vehicleClient;
    }

    /**
     * Adds a vehicle.
     *
     * @param newVehicleDTO             a POJO holding the json that was passed in containing the vehicle details
     * @return                          the generated uuid (key) of the added vehicle
     */
    @PostMapping("/vehicles")
    public ResponseEntity<Map<String, Object>> addVehicle(@RequestBody Map<String, Object> newVehicleDTO) {
        logger.info("[POST] /ui/vehicles");
        Map<String, Object> response = vehicleClient.addVehicle(newVehicleDTO);
        return ResponseEntity.ok(response);
    }

    /**
     * Gets a list of vehicles with their location(limited by passed value).
     *
     * @param maxVehicles              the maximum number of vehicle rows to return
     * @return                         a json array containing the vehicle details
     * @throws InvalidValueException   if you pass 0 or a negative value for the maximum rows to return
     */
    @GetMapping("/vehicles")
    public ResponseEntity<List<Map<String, Object>>> getVehiclesWithLocation(
            @RequestParam(value = "max_vehicles", required = false) @Min(1) Integer maxVehicles) {
        logger.info("[GET] /ui/vehicles");
        List<Map<String, Object>> vehicleWithLocationList = vehicleClient.getVehiclesWithLocation(maxVehicles);
        return ResponseEntity.ok(vehicleWithLocationList);
    }

    @GetMapping("/vehicles/location/{vehicleId}")
    public ResponseEntity<Map<String, Object>> getVehicleWithLocation(@PathVariable String vehicleId)
            throws InvalidUUIDException {
        logger.info("[GET] /ui/vehicles/location/{vehicleId}");
        Map<String, Object> vehicleWithLocation = vehicleClient.getVehicleWithLocation(toUUID(vehicleId, ERR_INVALID_VEHICLE_ID));
        return ResponseEntity.ok(vehicleWithLocation);
    }

    /**
     * Gets a specific vehicle with its location history.
     *
     * @param vehicleId               the uuid of the vehicle to return location history for
     * @return                        json with the vehicle details and a json array of all its past locations
     * @throws InvalidUUIDException   if the passed vehicleId string is not a valid UUID
     */
    @GetMapping("/vehicles/{vehicleId}")
    public ResponseEntity<Map<String, Object>> getVehicleWithHistory(@PathVariable String vehicleId)
            throws InvalidUUIDException {
        logger.info("[GET] /ui/vehicles/{vehicleId}");
        Map<String, Object> vehicle = vehicleClient.getVehicleWithHistory(toUUID(vehicleId, ERR_INVALID_VEHICLE_ID));
        return ResponseEntity.ok(vehicle);
    }

    /**
     * Removes a specific vehicle.
     *
     * @param vehicleId               the uuid of the vehicle to delete
     * @return                        "nothing"
     * @throws InvalidUUIDException   if the passed vehicleId string is not a valid UUID
     * @throws NotFoundException      if the passed vehicleId is not in the database
     */
    @DeleteMapping("/vehicles/{vehicleId}")
    public ResponseEntity<Map<String, Object>> removeVehicle(@PathVariable String vehicleId) throws InvalidUUIDException {
        logger.info("[DELETE] /ui/vehicles/{vehicleId}");
        Map<String, Object> messages = vehicleClient.removeVehicle(toUUID(vehicleId, ERR_INVALID_VEHICLE_ID));
        return ResponseEntity.ok(messages);
    }

}

