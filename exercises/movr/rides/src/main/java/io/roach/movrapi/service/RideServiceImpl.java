package io.roach.movrapi.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import io.roach.movrapi.exception.InvalidVehicleStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import io.roach.movrapi.dao.RideRepository;
import io.roach.movrapi.entity.Ride;
import io.roach.movrapi.exception.InvalidValueException;
import io.roach.movrapi.exception.NotFoundException;
import static io.roach.movrapi.util.Constants.ERR_NO_ACTIVE_RIDE;
import static io.roach.movrapi.util.Constants.ERR_VEHICLE_IN_USE;

/**
 * Implementation of the Ride Service Interface
 */

@Service
public class RideServiceImpl implements RideService {

    private RideRepository rideRepository;

    @Autowired
    public RideServiceImpl(RideRepository rideRepository) {
        this.rideRepository = rideRepository;
    }

    /**
     * Starts a ride for this vehicle/user combination.
     *
     * @param vehicleId                         the vehicle that the user will be riding
     * @param userEmail                         the email address that identifies the user
     * @param startTime                         the date/time that the user is starting their ride
     * @return                                  the Ride object representing the user's ride
     * @throws NotFoundException                if the vehicle or user is not found
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Ride startRide(UUID vehicleId, String userEmail, LocalDateTime startTime) throws InvalidVehicleStateException {
        List<Ride> activeRidesForVehicle = rideRepository.getActiveRidesForVehicle(vehicleId);

        if(!activeRidesForVehicle.isEmpty()) {
            throw new InvalidVehicleStateException(String.format(ERR_VEHICLE_IN_USE, vehicleId.toString()));
        }

        Ride ride = new Ride();
        ride.setUserEmail(userEmail);
        ride.setVehicleId(vehicleId);
        ride.setStartTime(startTime);
        rideRepository.save(ride);

        return ride;
    }

    /**
     * Ends the active ride for this vehicle/email combination.
     *
     * @param vehicleId                     the vehicle that the user will be riding
     * @param userEmail                     the email address that identifies the user
     * @param endTime                       the date/time the ride ended
     * @return                              A status message describing the distance travelled, speed, and duration
     * @throws NotFoundException            if the vehicle or user is not found
     * @throws InvalidValueException        if an error occurs during the math calculations
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Ride endRide(
            UUID vehicleId,
            String userEmail,
            int battery,
            double latitude,
            double longitude,
            LocalDateTime endTime)
        throws NotFoundException {

        // get the active ride for this user/vehicle
        Ride ride = getActiveRide(vehicleId, userEmail);

        // set the end time for the ride
        ride.setEndTime(endTime);
        rideRepository.save(ride);

        return ride;
    }

    /**
     * Gets all rides for the specified user.
     *
     * @param userEmail           the email address that identifies the user
     * @return                    List of ride objects for this user
     * @throws NotFoundException  if the user is not found
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE, readOnly = true)
    public List<Ride> getRidesForUser(String userEmail) {
        List<Ride> rideList = rideRepository.findAllForUser(userEmail);
        return rideList;
    }

    /**
     * Gets a specific active ride (user/vehicle combination).
     *
     * @param vehicleId           the vehicle that the user is riding
     * @param userEmail           the email address that identifies the user
     * @return                    the Ride object representing the requested ride
     * @throws NotFoundException  if the vehicle or user is not found
     */
    @Override
    public Ride getActiveRide(UUID vehicleId, String userEmail) throws NotFoundException {
        // should only be one active, but get a list just in case
        List<Ride> rideList = rideRepository.getActiveRide(vehicleId, userEmail);

        if (rideList.isEmpty()) {
            throw new NotFoundException(String.format(ERR_NO_ACTIVE_RIDE, vehicleId, userEmail));
        }

        return rideList.get(0);
    }
}
