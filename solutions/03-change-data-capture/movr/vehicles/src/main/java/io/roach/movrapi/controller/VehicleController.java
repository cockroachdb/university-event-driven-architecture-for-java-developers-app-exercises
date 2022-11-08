package io.roach.movrapi.controller;

import javax.validation.constraints.Min;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.roach.movrapi.dto.*;
import io.roach.movrapi.exception.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import io.roach.movrapi.entity.Vehicle;
import io.roach.movrapi.entity.VehicleWithLocation;
import io.roach.movrapi.service.VehicleService;
import static io.roach.movrapi.util.Common.*;
import static io.roach.movrapi.util.Constants.ERR_INVALID_VEHICLE_ID;
import static io.roach.movrapi.util.Constants.MSG_DELETED_VEHICLE;

/**
 * REST Controller to manage basic vehicle activities
 */

@RestController
@RequestMapping("/api")
@Validated
public class VehicleController {

    private VehicleService vehicleService;
    private final Logger logger = LogManager.getLogger(this.getClass());
    private final ObjectMapper mapper;

    @Autowired
    public VehicleController(VehicleService vehicleService, ObjectMapper mapper) {
        this.vehicleService = vehicleService;
        this.mapper = mapper;
    }

    /**
     * Adds a vehicle.
     *
     * @param newVehicleDTO             a POJO holding the json that was passed in containing the vehicle details
     * @return                          the generated uuid (key) of the added vehicle
     */
    @PostMapping("/vehicles")
    public ResponseEntity<VehicleIdDTO> addVehicle(@RequestBody NewVehicleDTO newVehicleDTO) throws InvalidValueException {
        logger.info("[POST] /api/vehicles");

        validateSerialNumber(newVehicleDTO.getSerialNumber());

        Vehicle vehicle = vehicleService.addVehicle(convertLatToDouble(newVehicleDTO.getLatitude()),
                                                    convertLonToDouble(newVehicleDTO.getLongitude()),
                                                    convertBatteryToInt(newVehicleDTO.getBattery()),
                                                    new VehicleInfoDTO(newVehicleDTO));

        return ResponseEntity.ok(VehicleIdDTO.fromVehicle(vehicle));
    }

    /**
     * Gets a list of vehicles with their location(limited by passed value).
     *
     * @param maxVehicles              the maximum number of vehicle rows to return
     * @return                         a json array containing the vehicle details
     * @throws InvalidValueException   if you pass 0 or a negative value for the maximum rows to return
     */
    @GetMapping("/vehicles")
    public ResponseEntity<List<VehicleWithLocationDTO>> getVehiclesWithLocation(
            @RequestParam(value = "max_vehicles", required = false) @Min(1) Integer maxVehicles) {
        logger.info("[GET] /api/vehicles");
        List<VehicleWithLocation> vehicleWithLocationList = vehicleService.getVehiclesWithLocation(maxVehicles);
        return ResponseEntity.ok(VehicleHelper.toVehicleWithLocationDTOList(vehicleWithLocationList));
    }

    /**
     * Gets a specific vehicle with its location history.
     *
     * @param vehicleId               the uuid of the vehicle to return location history for
     * @return                        json with the vehicle details and a json array of all its past locations
     * @throws InvalidUUIDException   if the passed vehicleId string is not a valid UUID
     * @throws NotFoundException      if the passed vehicleId is not in the database
     */
    @GetMapping("/vehicles/{vehicleId}")
    public ResponseEntity<VehicleWithHistoryDTO> getVehicleWithHistory(@PathVariable String vehicleId)
        throws InvalidUUIDException, NotFoundException {
        logger.info("[GET] /api/vehicles/{vehicleId}");

        Vehicle vehicle = vehicleService.getVehicle(toUUID(vehicleId, ERR_INVALID_VEHICLE_ID));
        return ResponseEntity.ok(VehicleHelper.toWithHistoryDTO(vehicle));
    }

    /**
     * Gets a specific vehicle and it's current location.
     * @param vehicleId
     * @return
     */
    @GetMapping("/vehicles/location/{vehicleId}")
    public ResponseEntity<VehicleWithLocationDTO> getVehicleWithLocation(@PathVariable String vehicleId) throws InvalidUUIDException, NotFoundException {
        logger.info("[GET] /api/vehicles/location/{vehicleId}");

        Vehicle vehicle = vehicleService.getVehicle(toUUID(vehicleId, ERR_INVALID_VEHICLE_ID));

        return ResponseEntity.ok(VehicleHelper.toWithLocationDTO(vehicle));
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
    public ResponseEntity<MessagesDTO> removeVehicle(@PathVariable String vehicleId)
        throws InvalidUUIDException, NotFoundException, InvalidVehicleStateException {
        logger.info("[DELETE] /api/vehicles/{vehicleId}");

        vehicleService.removeVehicle(toUUID(vehicleId, ERR_INVALID_VEHICLE_ID));

        String response = String.format(MSG_DELETED_VEHICLE, vehicleId);
        return ResponseEntity.ok(new MessagesDTO(response));
    }
}

