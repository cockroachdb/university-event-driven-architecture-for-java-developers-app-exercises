package io.roach.movrapi.dto;

import java.util.Map;
import java.util.UUID;

/**
 * Base Data Transfer Object for Vehicle Entity (abstract)
 */

public abstract class VehicleDTO {

    private UUID id;
    private Map<String, Object> vehicleInfo;
    private int battery;
    private boolean inUse;
    private int serialNumber;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Map<String, Object> getVehicleInfo() {
        return vehicleInfo;
    }

    public void setVehicleInfo(Map<String, Object> vehicleInfo) {
        this.vehicleInfo = vehicleInfo;
    }

    public int getBattery() {
        return battery;
    }

    public void setBattery(int battery) {
        this.battery = battery;
    }

    public boolean isInUse() {
        return inUse;
    }

    public void setInUse(boolean inUse) {
        this.inUse = inUse;
    }

    public int getSerialNumber() {
        return serialNumber;
    }
    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }
}
