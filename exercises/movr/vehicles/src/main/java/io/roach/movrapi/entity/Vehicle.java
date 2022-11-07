package io.roach.movrapi.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

/**
 * Hibernate entity for the Vehicles Table
 */

@Entity
@Table(name = "vehicles")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Vehicle {

    @Id
    @GeneratedValue
    private UUID id;
    private Integer battery;
    private LocalDateTime lastRideStart;
    private LocalDateTime lastRideEnd;
    // JPA/Hibernate cannot handle JSONB directly, so we define it as a UserType using Hibernate Types
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private String vehicleInfo;
    @Column(updatable = false, insertable = false)
    private Integer serialNumber;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "vehicle", cascade = CascadeType.ALL)
    @OrderBy("timestamp DESC")
    private List<LocationHistory> locationHistoryList;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getBattery() {
        return battery;
    }

    public void setBattery(Integer battery) {
        this.battery = battery;
    }

    public LocalDateTime getLastRideStart() {
        return lastRideStart;
    }

    public void setLastRideStart(LocalDateTime lastRideStart) {
        this.lastRideStart = lastRideStart;
    }

    public LocalDateTime getLastRideEnd() {
        return lastRideEnd;
    }

    public void setLastRideEnd(LocalDateTime lastRideEnd) {
        this.lastRideEnd = lastRideEnd;
    }

    public String getVehicleInfo() {
        return vehicleInfo;
    }

    public void setVehicleInfo(String vehicleInfo) {
        this.vehicleInfo = vehicleInfo;
    }

    public Integer getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Integer serialNumber) {
        this.serialNumber = serialNumber;
    }

    public List<LocationHistory> getLocationHistoryList() {
        return locationHistoryList;
    }

    public void setLocationHistoryList(List<LocationHistory> locationHistoryList) {
        this.locationHistoryList = locationHistoryList;
    }

    public Boolean getInUse() {
        if(lastRideStart == null)
            return false;
        else if(lastRideEnd == null)
            return true;
        else
            return lastRideStart.isAfter(lastRideEnd);
    }
}
