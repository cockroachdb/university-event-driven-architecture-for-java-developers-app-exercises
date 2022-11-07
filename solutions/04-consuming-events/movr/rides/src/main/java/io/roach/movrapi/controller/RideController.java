package io.roach.movrapi.controller;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

import io.roach.movrapi.exception.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import io.roach.movrapi.dto.*;
import io.roach.movrapi.entity.Ride;
import io.roach.movrapi.service.RideService;
import static io.roach.movrapi.util.Common.*;
import static io.roach.movrapi.util.Constants.*;

import org.modelmapper.ModelMapper;

/**
 * REST Controller to manage ride activities
 */

@RestController
@RequestMapping("/api")
public class RideController {

    private static final ModelMapper modelMapper = new ModelMapper();
    private final Logger logger = LogManager.getLogger(this.getClass());

    private RideService rideService;

    @Autowired
    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    /**
     * Gets the active ride for this vehicle/user combination.
     *
     * @param vehicleId                the vehicle that the user is riding
     * @param email                    the email address that identifies the user
     * @return                         Json containing details about the ride
     * @throws NotFoundException       if the vehicle or user is not found
     * @throws InvalidUUIDException    if the passed vehicleId string is not a valid UUID
     *
     */
    @GetMapping("/rides/active")
    public ResponseEntity<RideDTO> getActiveRide(@RequestParam("vehicle_id") String vehicleId,
                                                                @RequestParam String email)
        throws NotFoundException, InvalidUUIDException {
        logger.info("[GET] /api/rides/active?email="+email+"&vehicle_id="+vehicleId);

        Ride ride = rideService.getActiveRide(toUUID(vehicleId, ERR_INVALID_VEHICLE_ID), email);
        return ResponseEntity.ok(toDto(ride));
    }

    /**
     * Starts a ride on this vehicle for this user.
     *
     * @param rideRequestDTO             a POJO holding the json that was passed in
     * @return                                Json with details about the started ride
     * @throws InvalidUUIDException           if the passed vehicleId string is not a valid UUID
     */
    @PostMapping("/rides/start")
    public ResponseEntity<RideResponseDTO> startRide(@RequestBody RideRequestDTO rideRequestDTO)
        throws InvalidUUIDException, InvalidVehicleStateException {
        logger.info("[POST] /api/rides/start");

        LocalDateTime startTime = LocalDateTime.now(ZoneOffset.UTC);
        Ride ride = rideService.startRide(toUUID(rideRequestDTO.getVehicleId(), ERR_INVALID_VEHICLE_ID),
            rideRequestDTO.getEmail(), startTime);
        RideDTO rideDTO = toDto(ride);
        RideResponseDTO rideResponseDTO = new RideResponseDTO();
        rideResponseDTO.setRideDTO(rideDTO);
        rideResponseDTO.setMessages(
            new String[] { String.format(MSG_RIDE_STARTED, rideRequestDTO.getVehicleId())});
        return ResponseEntity.ok(rideResponseDTO);
    }

    /**
     * Ends this specific ride (also calculates time, distance, and speed travelled).
     *
     * @param endRideRequestDTO               a POJO holding the json that was passed in
     * @return                                a message about the time, speed and distance travelled
     * @throws NotFoundException              if the vehicle or user is not found
     * @throws InvalidUUIDException           if the passed vehicleId string is not a valid UUID
     * @throws InvalidValueException          if the math calculations result in an error
     */
    @PostMapping("/rides/end")
    public ResponseEntity<RideResponseDTO> endRide(@RequestBody @Validated EndRideRequestDTO endRideRequestDTO)
        throws NotFoundException, InvalidUUIDException, InvalidValueException {
        logger.info("[POST] /api/rides/end");

        LocalDateTime endTime = LocalDateTime.now(ZoneOffset.UTC);
        Ride ride = rideService.endRide(
            toUUID(endRideRequestDTO.getVehicleId(), ERR_INVALID_VEHICLE_ID),
                endRideRequestDTO.getEmail(),
                convertBatteryToInt(endRideRequestDTO.getBattery()),
                convertLatToDouble(endRideRequestDTO.getLatitude()),
                convertLonToDouble(endRideRequestDTO.getLongitude()),
                endTime);

        RideDTO rideDTO = toDto(ride);
        RideResponseDTO rideResponseDTO = new RideResponseDTO();
        rideResponseDTO.setRideDTO(rideDTO);
        rideResponseDTO.setMessages(
                new String[] { String.format(MSG_RIDE_ENDED, endRideRequestDTO.getVehicleId())});
        return ResponseEntity.ok(rideResponseDTO);
    }

    /**
     * Gets a list of all rides for the given user.
     *
     * @param email               email of the user to get rides for
     * @return                    List of all the rides (active and history) for this user
     * @throws NotFoundException  if the vehicle or user is not found
     */
    @GetMapping("/rides")
    public ResponseEntity<List<RideDTO>> getRides(@RequestParam String email) throws NotFoundException {
        logger.info("[GET] /api/rides?email="+email);

        List<RideDTO> rideDTOList = rideService.getRidesForUser(email)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(rideDTOList);
    }

    /**
     * Converts the Ride entity object to a Data Transfer Object.
     *
     * @param ride  the Ride entity object
     * @return      RideDTO
     */
    private RideDTO toDto(Ride ride) {
        return  modelMapper.map(ride, RideDTO.class);
    }
}

