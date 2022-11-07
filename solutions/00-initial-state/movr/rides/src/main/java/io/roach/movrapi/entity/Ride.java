package io.roach.movrapi.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Hibernate entity for the Ride Table
 */

@Entity
@Table(name = "rides")
public class Ride {

    @Id
    @GeneratedValue
    private UUID id;
    @Column(name = "start_ts")
    private LocalDateTime startTime;
    @Column(name = "end_ts")
    private LocalDateTime endTime;
    @Column(name = "user_email")
    private String userEmail;
    @Column(name = "vehicle_id")
    private UUID vehicleId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUserEmail() { return userEmail; }

    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public UUID getVehicleId() { return vehicleId; }

    public void setVehicleId(UUID vehicleId) { this.vehicleId = vehicleId; }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
