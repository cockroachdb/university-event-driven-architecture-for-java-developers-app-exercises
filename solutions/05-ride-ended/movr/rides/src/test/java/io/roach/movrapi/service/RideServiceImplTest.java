package io.roach.movrapi.service;

import io.roach.movrapi.dao.RideRepository;
import io.roach.movrapi.entity.Ride;
import io.roach.movrapi.events.EventPublisher;
import io.roach.movrapi.events.RideEnded;
import io.roach.movrapi.events.RideStarted;
import io.roach.movrapi.exception.InvalidVehicleStateException;
import io.roach.movrapi.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static io.roach.movrapi.util.TestHelpers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit Tests for RideServiceImpl.class
 */

public class RideServiceImplTest {
    @Mock
    private RideRepository rideRepository;
    @Mock
    private EventPublisher eventPublisher;

    private RideService rideService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        rideService = new RideServiceImpl(rideRepository, eventPublisher);
    }

    @Test
    public void getActiveRide_shouldThrowAnException_ifTheRideDoesntExist() {
        String email = createEmail();
        UUID vehicleId = createVehicleId();

        when(rideRepository.getActiveRide(vehicleId, email)).thenReturn(Collections.EMPTY_LIST);

        assertThrows(NotFoundException.class, () -> {
            rideService.getActiveRide(vehicleId, email);
        });
    }

    @Test
    public void getActiveRide_shouldReturnTheRide_ifItExists() throws NotFoundException {
        Ride expected = createRide();

        when(rideRepository.getActiveRide(expected.getVehicleId(), expected.getUserEmail())).thenReturn(Collections.singletonList(expected));

        Ride ride = rideService.getActiveRide(expected.getVehicleId(), expected.getUserEmail());

        assertEquals(expected.getId(), ride.getId());
        assertEquals(expected.getUserEmail(), ride.getUserEmail());
        assertEquals(expected.getVehicleId(), ride.getVehicleId());
        assertEquals(expected.getStartTime(), ride.getStartTime());
        assertEquals(expected.getEndTime(), ride.getEndTime());
    }

    @Test
    public void getActiveRide_shouldReturnTheFirstRide_ifMultiplesExist() throws NotFoundException {
        Ride expected = createRide();
        Ride extra1 = createRide();
        Ride extra2 = createRide();

        when(rideRepository.getActiveRide(expected.getVehicleId(), expected.getUserEmail())).thenReturn(Arrays.asList(expected, extra1, extra2));

        Ride ride = rideService.getActiveRide(expected.getVehicleId(), expected.getUserEmail());

        assertEquals(expected.getId(), ride.getId());
        assertEquals(expected.getUserEmail(), ride.getUserEmail());
        assertEquals(expected.getVehicleId(), ride.getVehicleId());
        assertEquals(expected.getStartTime(), ride.getStartTime());
        assertEquals(expected.getEndTime(), ride.getEndTime());
    }

    @Test
    public void getRidesForUser_shouldReturnAnEmptyList_ifNoRidesExist() {
        String email = createEmail();

        when(rideRepository.findAllForUser(email)).thenReturn(Collections.EMPTY_LIST);

        List<Ride> rides = rideService.getRidesForUser(email);

        assertEquals(0, rides.size());
    }

    @Test
    public void getRidesForUser_shouldReturnARide_ifItExists() {
        Ride expected = createRide();

        when(rideRepository.findAllForUser(expected.getUserEmail())).thenReturn(Collections.singletonList(expected));

        List<Ride> rides = rideService.getRidesForUser(expected.getUserEmail());

        assertEquals(1, rides.size());
        assertEquals(expected.getId(), rides.get(0).getId());
        assertEquals(expected.getUserEmail(), rides.get(0).getUserEmail());
        assertEquals(expected.getVehicleId(), rides.get(0).getVehicleId());
        assertEquals(expected.getStartTime(), rides.get(0).getStartTime());
        assertEquals(expected.getEndTime(), rides.get(0).getEndTime());
    }

    @Test
    public void getRidesForUser_shouldAllRides_ifMultiplesExist() {
        Ride expected1 = createRide();
        Ride expected2 = createRide();
        Ride expected3 = createRide();

        when(rideRepository.findAllForUser(expected1.getUserEmail())).thenReturn(Arrays.asList(expected1, expected2, expected3));

        List<Ride> rides = rideService.getRidesForUser(expected1.getUserEmail());

        assertEquals(3, rides.size());
        assertEquals(expected1.getId(), rides.get(0).getId());
        assertEquals(expected2.getId(), rides.get(1).getId());
        assertEquals(expected3.getId(), rides.get(2).getId());
    }

    @Test
    public void startRide_shouldCreateANewRideAndPublishAnEvent() throws InvalidVehicleStateException {
        UUID vehicleId = createVehicleId();
        String email = createEmail();
        LocalDateTime startTime = createDateTime();

        when(rideRepository.save(any(Ride.class))).thenAnswer(input -> input.getArgument(0));

        Ride ride = rideService.startRide(vehicleId, email, startTime);

        verify(rideRepository).save(ride);

        assertNull(ride.getId()); // The ID is assigned during persistence which is mocked.
        assertEquals(vehicleId, ride.getVehicleId());
        assertEquals(email, ride.getUserEmail());
        assertEquals(startTime, ride.getStartTime());
        assertNull(ride.getEndTime());

        ArgumentCaptor<RideStarted> eventCaptor = ArgumentCaptor.forClass(RideStarted.class);
        verify(eventPublisher).publish(eq(RideStarted.EVENT_NAME), eventCaptor.capture());

        RideStarted event = eventCaptor.getValue();
        assertEquals(ride.getId(), event.getRideId());
        assertEquals(email, event.getUserEmail());
        assertEquals(vehicleId, event.getVehicleId());
        assertEquals(startTime, event.getStartTime());
    }

    @Test
    public void startRide_shouldThrowAnException_ifThereIsAnExistingRideForThatVehicle() {
        Ride existing = createRide();

        when(rideRepository.getActiveRidesForVehicle(existing.getVehicleId())).thenReturn(Arrays.asList(existing));

        assertThrows(InvalidVehicleStateException.class, () -> rideService.startRide(existing.getVehicleId(), createEmail(), createDateTime()));

        verify(rideRepository, never()).save(any());
        verify(eventPublisher, never()).publish(anyString(), any());
    }

    @Test
    public void endRide_shouldThrowAnException_ifTheRideDoesntExist() {
        String email = createEmail();
        UUID vehicleId = createVehicleId();
        int battery = createBattery();
        double latitude = createLatitude();
        double longitude = createLongitude();
        LocalDateTime endTime = createDateTime();

        when(rideRepository.getActiveRide(vehicleId, email)).thenReturn(Collections.EMPTY_LIST);

        assertThrows(NotFoundException.class, () -> {
            rideService.endRide(vehicleId, email, battery, latitude, longitude, endTime);
        });

        verify(rideRepository, never()).save(any());
    }

    @Test
    public void endRide_shouldUpdateTheRideAndPublishAnEvent_ifItExists() throws NotFoundException {
        int battery = createBattery();
        double latitude = createLatitude();
        double longitude = createLongitude();
        Ride existing = createRide();
        existing.setEndTime(null);
        LocalDateTime endTime = createDateTime();

        when(rideRepository.getActiveRide(existing.getVehicleId(), existing.getUserEmail())).thenReturn(Collections.singletonList(existing));
        when(rideRepository.save(any(Ride.class))).thenAnswer(input -> input.getArgument(0));

        Ride updated = rideService.endRide(existing.getVehicleId(), existing.getUserEmail(), battery, latitude, longitude, endTime);

        verify(rideRepository).save(updated);

        assertEquals(existing.getId(), updated.getId());
        assertEquals(existing.getVehicleId(), updated.getVehicleId());
        assertEquals(existing.getUserEmail(), updated.getUserEmail());
        assertEquals(existing.getStartTime(), updated.getStartTime());
        assertEquals(endTime, updated.getEndTime());

        ArgumentCaptor<RideEnded> eventCaptor = ArgumentCaptor.forClass(RideEnded.class);
        verify(eventPublisher).publish(eq(RideEnded.EVENT_NAME), eventCaptor.capture());

        RideEnded event = eventCaptor.getValue();
        assertEquals(existing.getId(), event.getRideId());
        assertEquals(existing.getUserEmail(), event.getUserEmail());
        assertEquals(existing.getVehicleId(), event.getVehicleId());
        assertEquals(battery, event.getBattery());
        assertEquals(latitude, event.getLatitude());
        assertEquals(longitude, event.getLongitude());
        assertEquals(endTime, event.getEndTime());
    }
}
