package io.roach.movrapi.controller;

import java.util.List;
import java.util.stream.Collectors;

import io.roach.movrapi.dto.LocationDetailsDTO;
import io.roach.movrapi.dto.VehicleWithHistoryDTO;
import io.roach.movrapi.dto.VehicleWithLocationDTO;
import io.roach.movrapi.entity.LocationHistory;
import io.roach.movrapi.entity.Vehicle;
import io.roach.movrapi.entity.VehicleWithLocation;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;

/**
 * Helper class to translate Vehicles to Data Transfer Objects.
 * (This is in a separate class because both the VehicleController and the RideController need it.)
 */

public final class VehicleHelper {

    private static final ModelMapper modelMapper = new ModelMapper();

    // this class just contains static methods so don't allow it to be created
    private VehicleHelper() {
    }

    /**
     * Converts a list of Vehicle entity objects to a list of VehicleWithLocationDTO.
     *
     * @param vehicleList   list of Vehicle objects
     * @return              List of RideWithVehicleDTOs
     */
    public static final List<VehicleWithLocationDTO> toWithLocationList(List<Vehicle> vehicleList) {
        return vehicleList.stream().map(v -> toWithLocationDTO(v))
            .collect(Collectors.toList());
    }

    /**
     * Converts a list of VehicleWithLocation entity objects to a list of VehicleWithLocationDTO objects.
     * @param vehicleWithLocationList   List of VehicleWithLocation objects
     * @return                          List of VehicleWithLocationDTO's
     */
    public static final List<VehicleWithLocationDTO> toVehicleWithLocationDTOList(List<VehicleWithLocation> vehicleWithLocationList) {
        return vehicleWithLocationList.stream().map(v -> toVehicleWithLocationDTO(v)).collect(Collectors.toList());
    }

    /**
     * Converts the Vehicle entity object to a VehicleWithLocationDTO.
     *
     * @param vehicle   the Vehicle entity object
     * @return          VehicleWithLocationDTO
     */
    public static final VehicleWithLocationDTO toWithLocationDTO(Vehicle vehicle) {
        VehicleWithLocationDTO vehicleWithLocationDTO = modelMapper.map(vehicle, VehicleWithLocationDTO.class);
        vehicleWithLocationDTO.setVehicleInfo(new JSONObject(vehicle.getVehicleInfo()).toMap());

        // get the location history info (already sorted, 
        // descending by timestamp)
        if (!vehicle.getLocationHistoryList().isEmpty()) {
            LocationHistory locationHistory = vehicle.getLocationHistoryList().get(0);
            vehicleWithLocationDTO.setTimestamp(locationHistory.getTimestamp());
            vehicleWithLocationDTO.setLastLatitude(locationHistory.getLatitude());
            vehicleWithLocationDTO.setLastLongitude(locationHistory.getLongitude());
        }
        return vehicleWithLocationDTO;
    }

    /**
     * Converts the VehicleWithLocation entity object to a VehicleWithLocationDTO.
     *
     * @param vehicleWithLocation   the Vehicle entity object
     * @return          VehicleWithLocationDTO
     */
    public static final VehicleWithLocationDTO toVehicleWithLocationDTO(VehicleWithLocation vehicleWithLocation) {
        VehicleWithLocationDTO vehicleWithLocationDTO = modelMapper.map(vehicleWithLocation, VehicleWithLocationDTO.class);
        vehicleWithLocationDTO.setVehicleInfo(new JSONObject(vehicleWithLocation.getVehicleInfo()).toMap());

        return vehicleWithLocationDTO;
    }

    /**
     * Converts the Vehicle entity object to a VehicleWithHistoryDTO.
     *
     * @param vehicle   the Vehicle entity object
     * @return          VehicleWithHistoryDTO
     */
    public static final VehicleWithHistoryDTO toWithHistoryDTO(Vehicle vehicle) {
        VehicleWithHistoryDTO vehicleWithLocationDTO = modelMapper.map(vehicle, VehicleWithHistoryDTO.class);
        vehicleWithLocationDTO.setVehicleInfo(new JSONObject(vehicle.getVehicleInfo()).toMap());
        vehicleWithLocationDTO.setLocationDetailsDTOList(
            vehicle.getLocationHistoryList().stream().map(lh -> {
                return modelMapper.map(lh, LocationDetailsDTO.class);
            })
                .collect(Collectors.toList()));
        return vehicleWithLocationDTO;
    }
}
