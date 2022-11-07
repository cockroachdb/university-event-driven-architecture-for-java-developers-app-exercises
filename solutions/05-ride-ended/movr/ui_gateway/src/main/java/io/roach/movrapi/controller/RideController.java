package io.roach.movrapi.controller;

import java.util.List;
import java.util.Map;

import io.roach.movrapi.clients.RidesClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import io.roach.movrapi.dto.*;
import io.roach.movrapi.exception.InvalidUUIDException;
import io.roach.movrapi.exception.InvalidValueException;
import io.roach.movrapi.exception.InvalidVehicleStateException;
import io.roach.movrapi.exception.NotFoundException;

/**
 * REST Controller to manage ride activities
 */

@RestController
@RequestMapping("/ui")
public class RideController {

    private final Logger logger = LogManager.getLogger(this.getClass());

    private RidesClient ridesClient;

    @Autowired
    public RideController(RidesClient ridesClient) {
        this.ridesClient = ridesClient;
    }

    /**
     * Starts a ride on this vehicle for this user.
     *
     * @param startRideRequestDTO             a POJO holding the json that was passed in
     * @return                                Json with details about the started ride
     * @throws NotFoundException              if the vehicle or user is not found
     * @throws InvalidUUIDException           if the passed vehicleId string is not a valid UUID
     * @throws InvalidVehicleStateException   if the requested vehicle is not already marked "in use"
     */
    @PostMapping("/rides/start")
    public ResponseEntity<Map<String, Object>> startRide(@RequestBody Map<String, Object> startRideRequestDTO) {
        logger.info("[POST] /ui/rides/start");
        Map<String, Object> response = ridesClient.startRide(startRideRequestDTO);
        return ResponseEntity.ok(response);
    }

    /**
     * Ends this specific ride (also calculates time, distance, and speed travelled).
     *
     * @param endRideRequestDTO               a POJO holding the json that was passed in
     * @return                                a message about the time, speed and distance travelled
     * @throws NotFoundException              if the vehicle or user is not found
     * @throws InvalidUUIDException           if the passed vehicleId string is not a valid UUID
     * @throws InvalidVehicleStateException   if the requested vehicle is not already marked "in use"
     * @throws InvalidValueException          if the math calculations result in an error
     */
    @PostMapping("/rides/end")
    public ResponseEntity<Map<String, Object>> endRide(@RequestBody @Validated Map<String, Object> endRideRequestDTO) {
        logger.info("[POST] /ui/rides/end");
        Map<String, Object> response = ridesClient.endRide(endRideRequestDTO);
        return ResponseEntity.ok(response);
    }

    /**
     * Gets a list of all rides for the given user.
     *
     * @param email               email of the user to get rides for
     * @return                    List of all the rides (active and history) for this user
     * @throws NotFoundException  if the vehicle or user is not found
     */
    @GetMapping("/rides")
    public ResponseEntity<List<Map<String, Object>>> getRides(@RequestParam String email) {
        logger.info("[GET] /ui/rides?email={email}");
        List<Map<String, Object>> rides = ridesClient.getRides(email);
        return ResponseEntity.ok(rides);
    }
}

