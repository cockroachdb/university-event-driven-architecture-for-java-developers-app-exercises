package io.roach.movrapi.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Data Transfer Object for Vehicle with Location History Information appended
 */

public class VehicleWithHistoryDTO extends VehicleDTO {

    @JsonProperty(value = "locationHistory")
    List<LocationDetailsDTO> locationDetailsDTOList;

    public List<LocationDetailsDTO> getLocationDetailsDTOList() {
        return locationDetailsDTOList;
    }

    public void setLocationDetailsDTOList(List<LocationDetailsDTO> locationDetailsDTOList) {
        this.locationDetailsDTOList = locationDetailsDTOList;
    }
}
