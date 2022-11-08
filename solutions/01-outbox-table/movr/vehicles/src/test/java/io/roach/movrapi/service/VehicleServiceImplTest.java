package io.roach.movrapi.service;

import io.roach.movrapi.dao.LocationHistoryRepository;
import io.roach.movrapi.dao.VehicleRepository;
import io.roach.movrapi.dao.VehicleWithLocationRepository;
import io.roach.movrapi.dto.VehicleInfoDTO;
import io.roach.movrapi.entity.LocationHistory;
import io.roach.movrapi.entity.Vehicle;
import io.roach.movrapi.entity.VehicleWithLocation;
import io.roach.movrapi.exception.InvalidVehicleStateException;
import io.roach.movrapi.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.roach.movrapi.util.TestHelpers.*;
import static io.roach.movrapi.util.TestHelpers.createTimestamp;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit Tests for the VehicleServiceImpl.class
 */
public class VehicleServiceImplTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private VehicleWithLocationRepository vehicleWithLocationRepository;

    @Mock
    private LocationHistoryRepository locationHistoryRepository;

    private VehicleService vehicleService;

    @BeforeEach
    public void init() {

        MockitoAnnotations.openMocks(this);
        vehicleService = new VehicleServiceImpl(
                vehicleRepository,
                vehicleWithLocationRepository,
                locationHistoryRepository);
    }

    @Test
    void getVehicle_shouldThrowAnException_ifTheVehicleDoesntExist() {
        UUID id = createVehicleId();

        when(vehicleRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundException.class, () -> {
            vehicleService.getVehicle(id);
        });

        assertEquals("Vehicle id <"+id+"> not found", exception.getMessage());
        verify(vehicleRepository).findById(id);
    }

    @Test
    void getVehicle_shouldReturnTheVehicle_ifItExists() throws NotFoundException {
        Vehicle expected = createVehicle();

        when(vehicleRepository.findById(expected.getId())).thenReturn(Optional.of(expected));

        Vehicle vehicle = vehicleService.getVehicle(expected.getId());

        assertEquals(expected.getId(), vehicle.getId());
        assertEquals(expected.getSerialNumber(), vehicle.getSerialNumber());
        assertEquals(expected.getBattery(), vehicle.getBattery());
        assertEquals(expected.getInUse(), vehicle.getInUse());
        assertArrayEquals(
                expected.getLocationHistoryList().toArray(new LocationHistory[0]),
                vehicle.getLocationHistoryList().toArray(new LocationHistory[0])
        );
        assertEquals(expected.getVehicleInfo(), vehicle.getVehicleInfo());
        verify(vehicleRepository).findById(expected.getId());
    }

    @Test
    void getVehiclesWithLocation_shouldReturnAnEmptyList_ifThereAreNoVehicles() {
        int numVehicles = 20;

        when(vehicleWithLocationRepository.getVehiclesWithLocation(numVehicles)).thenReturn(new ArrayList<>());

        List<VehicleWithLocation> vehicles = vehicleService.getVehiclesWithLocation(numVehicles);

        assertEquals(0, vehicles.size());
        verify(vehicleWithLocationRepository).getVehiclesWithLocation(numVehicles);
    }

    @Test
    void getVehiclesWithLocation_shouldReturnTheVehicles_ifTheyExist() {
        int numVehicles = 20;
        List<VehicleWithLocation> expected = Stream.generate(() -> createVehicleWithLocation())
                .limit(numVehicles)
                .collect(Collectors.toList());

        when(vehicleWithLocationRepository.getVehiclesWithLocation(numVehicles)).thenReturn(expected);

        List<VehicleWithLocation> vehicles = vehicleService.getVehiclesWithLocation(numVehicles);

        verify(vehicleWithLocationRepository).getVehiclesWithLocation(numVehicles);
        for(int i = 0; i < numVehicles; i++) {
            assertEquals(expected.get(i).getId(), vehicles.get(i).getId());
        }
    }

    @Test
    void getVehiclesWithLocation_shouldRequestTheMaximum_ifNumRecordsIsNull() {
        List<VehicleWithLocation> vehicles = vehicleService.getVehiclesWithLocation(null);
        verify(vehicleWithLocationRepository).getVehiclesWithLocation(VehicleService.MAX_VEHICLES_TO_RETURN);
    }

    @Test
    void addVehicle_shouldSaveTheVehicleAndLocationHistory() {
        Vehicle expectedVehicle = createVehicle();
        LocationHistory expectedLocation = createLocationHistory(expectedVehicle);
        VehicleInfoDTO expectedVehicleInfoDTO = createVehicleInfoDTO();

        vehicleService.addVehicle(
                expectedLocation.getLatitude(),
                expectedLocation.getLongitude(),
                expectedVehicle.getBattery(),
                expectedVehicleInfoDTO
        );

        ArgumentCaptor<Vehicle> vehicleArgumentCaptor =
                ArgumentCaptor.forClass(Vehicle.class);
        ArgumentCaptor<LocationHistory> locationHistoryArgumentCaptor =
                ArgumentCaptor.forClass(LocationHistory.class);

        verify(vehicleRepository).save(vehicleArgumentCaptor.capture());
        verify(locationHistoryRepository).save(locationHistoryArgumentCaptor.capture());

        Vehicle vehicle = vehicleArgumentCaptor.getValue();
        assertEquals(expectedVehicle.getBattery(), vehicle.getBattery());
        assertEquals(expectedVehicleInfoDTO.getAsJsonString(), vehicle.getVehicleInfo());
        LocationHistory locationHistory = locationHistoryArgumentCaptor.getValue();
        assertEquals(expectedLocation.getLatitude(), locationHistory.getLatitude());
        assertEquals(expectedLocation.getLongitude(), locationHistory.getLongitude());
    }

    @Test
    public void removeVehicle_shouldThrowAnException_IfTheVehicleDoesntExist() {
        UUID vehicleId = createVehicleId();

        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            vehicleService.removeVehicle(vehicleId);
        });

        verify(vehicleRepository, never()).delete(any());
    }

    @Test
    public void removeVehicle_shouldThrowAnException_IfTheVehicleIsInUse() {
        Vehicle vehicle = createVehicle();
        vehicle.setLastRideStart(createTimestamp());
        vehicle.setLastRideEnd(null);

        when(vehicleRepository.findById(vehicle.getId())).thenReturn(Optional.of(vehicle));

        assertThrows(InvalidVehicleStateException.class, () -> {
            vehicleService.removeVehicle(vehicle.getId());
        });

        verify(vehicleRepository, never()).delete(any());
    }

    @Test
    public void removeVehicle_shouldDeleteTheVehicle_IfTheVehicleExistsAndIsValid()
            throws NotFoundException,InvalidVehicleStateException {
        Vehicle vehicle = createVehicle();

        when(vehicleRepository.findById(vehicle.getId())).thenReturn(Optional.of(vehicle));

        vehicleService.removeVehicle(vehicle.getId());

        verify(vehicleRepository).delete(vehicle);
    }

    @Test
    public void checkoutVehicle_shouldThrowAnException_ifTheVehicleDoesntExist() {
        Vehicle vehicle = createVehicle();

        when(vehicleRepository.findById(vehicle.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            vehicleService.checkoutVehicle(vehicle.getId(), vehicle.getLastRideStart());
        });

        verify(vehicleRepository, never()).save(any());
    }

    @Test
    public void checkoutVehicle_shouldUpdateTheLastRideStart_ifThereIsNoPreviousRide()
            throws NotFoundException,InvalidVehicleStateException {
        Vehicle expectedVehicle = createVehicle();
        LocalDateTime startTime = createTimestamp();
        expectedVehicle.setLastRideStart(null);
        expectedVehicle.setLastRideEnd(null);

        when(vehicleRepository.findById(expectedVehicle.getId())).thenReturn(Optional.of(expectedVehicle));

        Vehicle vehicle = vehicleService.checkoutVehicle(expectedVehicle.getId(), startTime);

        assertEquals(expectedVehicle.getId(), vehicle.getId());
        assertEquals(true, vehicle.getInUse());
        assertEquals(startTime, vehicle.getLastRideStart());
        assertNull(vehicle.getLastRideEnd());
        verify(vehicleRepository).save(vehicle);
    }

    @Test
    public void checkoutVehicle_shouldUpdateTheLastRideStart_ifThereIsAnEarlierRide()
            throws NotFoundException,InvalidVehicleStateException {
        Vehicle expectedVehicle = createVehicle();
        LocalDateTime earlierTime = createTimestamp();
        LocalDateTime laterTime = createTimestamp().plusSeconds(1000);
        expectedVehicle.setLastRideStart(earlierTime);
        expectedVehicle.setLastRideEnd(earlierTime);

        when(vehicleRepository.findById(expectedVehicle.getId())).thenReturn(Optional.of(expectedVehicle));

        Vehicle vehicle = vehicleService.checkoutVehicle(expectedVehicle.getId(), laterTime);

        assertEquals(expectedVehicle.getId(), vehicle.getId());
        assertEquals(true, vehicle.getInUse());
        assertEquals(laterTime, vehicle.getLastRideStart());
        assertEquals(earlierTime, vehicle.getLastRideEnd());
        verify(vehicleRepository).save(vehicle);
    }

    @Test
    public void checkoutVehicle_shouldNotUpdateTheLastRideStart_ifThereIsALaterRide()
            throws NotFoundException,InvalidVehicleStateException {
        Vehicle expectedVehicle = createVehicle();
        LocalDateTime earlierTime = createTimestamp();
        LocalDateTime laterTime = createTimestamp().plusSeconds(1000);
        expectedVehicle.setLastRideStart(laterTime);
        expectedVehicle.setLastRideEnd(laterTime);

        when(vehicleRepository.findById(expectedVehicle.getId())).thenReturn(Optional.of(expectedVehicle));

        Vehicle vehicle = vehicleService.checkoutVehicle(expectedVehicle.getId(), earlierTime);

        assertEquals(expectedVehicle.getId(), vehicle.getId());
        assertEquals(false, vehicle.getInUse());
        assertEquals(laterTime, vehicle.getLastRideStart());
        assertEquals(laterTime, vehicle.getLastRideEnd());
        verify(vehicleRepository).save(vehicle);
    }

    @Test
    public void checkinVehicle_shouldThrowAnException_ifTheVehicleDoesntExist() {
        Vehicle vehicle = createVehicle();
        LocationHistory locationHistory = createLocationHistory(vehicle);

        when(vehicleRepository.findById(vehicle.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            vehicleService.checkinVehicle(
                    vehicle.getId(),
                    locationHistory.getLatitude(),
                    locationHistory.getLongitude(),
                    vehicle.getBattery(),
                    locationHistory.getTimestamp()
            );
        });

        verify(vehicleRepository, never()).save(any());
    }

    @Test
    public void checkinVehicle_shouldMarkTheVehicleNotInUseAndAddLocationHistory()
            throws NotFoundException,InvalidVehicleStateException {
        Vehicle expectedVehicle = createVehicle();
        expectedVehicle.setLastRideStart(createTimestamp());
        expectedVehicle.setLastRideEnd(null);
        LocationHistory expectedLocationHistory = createLocationHistory(expectedVehicle);

        when(vehicleRepository.findById(expectedVehicle.getId())).thenReturn(Optional.of(expectedVehicle));

        Vehicle vehicle = vehicleService.checkinVehicle(
                expectedVehicle.getId(),
                expectedLocationHistory.getLatitude(),
                expectedLocationHistory.getLongitude(),
                expectedVehicle.getBattery(),
                expectedLocationHistory.getTimestamp()
        );

        verify(vehicleRepository).save(vehicle);

        assertEquals(expectedVehicle.getId(), vehicle.getId());
        assertEquals(false, vehicle.getInUse());

        ArgumentCaptor<LocationHistory> locationHistoryArgumentCaptor =
                ArgumentCaptor.forClass(LocationHistory.class);

        verify(locationHistoryRepository).save(locationHistoryArgumentCaptor.capture());
        LocationHistory locationHistory = locationHistoryArgumentCaptor.getValue();
        assertEquals(expectedLocationHistory.getLatitude(), locationHistory.getLatitude());
        assertEquals(expectedLocationHistory.getLongitude(), locationHistory.getLongitude());
        assertEquals(expectedLocationHistory.getTimestamp(), locationHistory.getTimestamp());
        assertEquals(expectedLocationHistory.getVehicle().getId(), locationHistory.getVehicle().getId());
    }
}
