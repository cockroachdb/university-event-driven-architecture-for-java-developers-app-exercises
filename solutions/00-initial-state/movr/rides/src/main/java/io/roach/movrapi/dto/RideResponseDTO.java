package io.roach.movrapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Data Transfer Object for Start Ride
 */

public class RideResponseDTO {

    @JsonProperty("ride")
    private RideDTO rideDTO;
    private String[] messages;

    public RideDTO getRideDTO() {
        return rideDTO;
    }

    public void setRideDTO(RideDTO rideDTO) {
        this.rideDTO = rideDTO;
    }

    public String[] getMessages() {
        return messages;
    }

    public void setMessages(String[] messages) {
        this.messages = messages;
    }
}
