package io.roach.movrapi.dao;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.roach.movrapi.entity.Vehicle;

/**
 * JPA Repository for Vehicles - we have no specific requirements for Vehicle
 * other than the standard Create, Update, Delete options we inherit from the
 * JpaRepository interface we're sub-classing, so we don't need any special
 * logic here
 */

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {
}
