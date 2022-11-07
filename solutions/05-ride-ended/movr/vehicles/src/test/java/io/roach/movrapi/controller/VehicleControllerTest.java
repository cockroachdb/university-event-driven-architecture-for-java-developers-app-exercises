package io.roach.movrapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.roach.movrapi.dto.*;
import io.roach.movrapi.entity.Vehicle;
import io.roach.movrapi.entity.VehicleWithLocation;
import io.roach.movrapi.events.KafkaMessage;
import io.roach.movrapi.events.RideEnded;
import io.roach.movrapi.events.RideStarted;
import io.roach.movrapi.exception.*;
import io.roach.movrapi.service.VehicleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.roach.movrapi.util.TestHelpers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@JsonTest
class VehicleControllerTest {

    @Mock
    private VehicleService vehicleService;

    private VehicleController vehicleController;
    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        vehicleController = new VehicleController(vehicleService, mapper);
    }

    @Test
    public void addVehicle_shouldThrowAnException_ifTheSerialNumberIsInvalid() {
        NewVehicleDTO newVehicle = createNewVehicleDTO();
        newVehicle.setSerialNumber("BAD DATA");

        assertThrows(InvalidValueException.class, () -> {
            vehicleController.addVehicle(newVehicle).getBody();
        });

        verify(vehicleService, never()).addVehicle(anyDouble(), anyDouble(), anyInt(), any());
    }

    @Test
    public void addVehicle_shouldThrowAnException_ifTheLatitudeIsInvalid() {
        NewVehicleDTO newVehicle = createNewVehicleDTO();
        newVehicle.setLatitude("BAD DATA");

        assertThrows(InvalidValueException.class, () -> {
            vehicleController.addVehicle(newVehicle).getBody();
        });

        verify(vehicleService, never()).addVehicle(anyDouble(), anyDouble(), anyInt(), any());
    }

    @Test
    public void addVehicle_shouldThrowAnException_ifTheLongitudeIsInvalid() {
        NewVehicleDTO newVehicle = createNewVehicleDTO();
        newVehicle.setLongitude("BAD DATA");

        assertThrows(InvalidValueException.class, () -> {
            vehicleController.addVehicle(newVehicle).getBody();
        });

        verify(vehicleService, never()).addVehicle(anyDouble(), anyDouble(), anyInt(), any());
    }

    @Test
    public void addVehicle_shouldThrowAnException_ifTheBatteryIsInvalid() {
        NewVehicleDTO newVehicle = createNewVehicleDTO();
        newVehicle.setBattery("BAD DATA");

        assertThrows(InvalidValueException.class, () -> {
            vehicleController.addVehicle(newVehicle).getBody();
        });

        verify(vehicleService, never()).addVehicle(anyDouble(), anyDouble(), anyInt(), any());
    }

    @Test
    public void addVehicle_shouldCallAddVehicleOnTheService() throws InvalidValueException {
        NewVehicleDTO newVehicle = createNewVehicleDTO();
        Vehicle vehicle = createVehicle();
        VehicleInfoDTO expectedVehicleInfo = new VehicleInfoDTO(newVehicle);

        when(vehicleService.addVehicle(anyDouble(), anyDouble(), anyInt(), any()))
                .thenReturn(vehicle);

        VehicleIdDTO response = vehicleController.addVehicle(newVehicle).getBody();

        ArgumentCaptor<VehicleInfoDTO> captor = ArgumentCaptor.forClass(VehicleInfoDTO.class);

        assertEquals(vehicle.getId().toString(), response.getId().toString());
        verify(vehicleService).addVehicle(
                eq(Double.parseDouble(newVehicle.getLatitude())),
                eq(Double.parseDouble(newVehicle.getLongitude())),
                eq(Integer.parseInt(newVehicle.getBattery())),
                captor.capture()
        );

        VehicleInfoDTO vehicleInfoDTO = captor.getValue();
        assertEquals(expectedVehicleInfo.getAsJsonString(), vehicleInfoDTO.getAsJsonString());
    }

    @Test
    public void getVehiclesWithLocation_shouldReturnTheVehicles_ifTheyExist() {
        int numVehicles = 10;

        List<VehicleWithLocation> expectedVehicles = Stream.generate(() -> createVehicleWithLocation())
                .limit(numVehicles)
                .collect(Collectors.toList());

        when(vehicleService.getVehiclesWithLocation(numVehicles)).thenReturn(expectedVehicles);

        ResponseEntity<List<VehicleWithLocationDTO>> response = vehicleController.getVehiclesWithLocation(numVehicles);

        List<VehicleWithLocationDTO> vehicles = response.getBody();

        assertEquals(numVehicles, vehicles.size());
        for(int i = 0; i < numVehicles; i++) {
            VehicleWithLocation expected = expectedVehicles.get(i);
            VehicleWithLocationDTO vehicle = vehicles.get(i);
            assertEquals(expected.getId(), vehicle.getId());
            assertEquals(expected.getBattery(), vehicle.getBattery());
            assertEquals(expected.getSerialNumber(), String.valueOf(vehicle.getSerialNumber()));
            assertEquals(expected.getInUse(), vehicle.isInUse());
        }
    }

    @Test
    public void getVehicleWithLocation_shouldReturnTheVehicle_ifItExists() throws NotFoundException, InvalidUUIDException {
        Vehicle expected = createVehicle();

        when(vehicleService.getVehicle(expected.getId())).thenReturn(expected);

        VehicleWithLocationDTO vehicle = vehicleController.getVehicleWithLocation(expected.getId().toString()).getBody();

        assertEquals(expected.getId(), vehicle.getId());
        assertEquals(expected.getBattery(), vehicle.getBattery());
        assertEquals(expected.getSerialNumber(), vehicle.getSerialNumber());
        assertEquals(expected.getInUse(), vehicle.isInUse());
        assertEquals(expected.getLocationHistoryList().get(0).getLatitude(), vehicle.getLastLatitude());
        assertEquals(expected.getLocationHistoryList().get(0).getLongitude(), vehicle.getLastLongitude());
        assertEquals(expected.getLocationHistoryList().get(0).getTimestamp(), vehicle.getTimestamp());
    }

    @Test
    public void getVehicleWithHistory_shouldReturnTheVehicle_ifItExists() throws NotFoundException, InvalidUUIDException {
        Vehicle expected = createVehicle();

        when(vehicleService.getVehicle(expected.getId())).thenReturn(expected);

        VehicleWithHistoryDTO vehicle = vehicleController.getVehicleWithHistory(expected.getId().toString()).getBody();

        assertEquals(expected.getId(), vehicle.getId());
        assertEquals(expected.getBattery(), vehicle.getBattery());
        assertEquals(expected.getSerialNumber(), vehicle.getSerialNumber());
        assertEquals(expected.getInUse(), vehicle.isInUse());
        assertEquals(expected.getLocationHistoryList().size(), vehicle.getLocationDetailsDTOList().size());
    }

    @Test
    public void removeVehicle_shouldRemoveTheVehicle_ifItExists() throws NotFoundException, InvalidVehicleStateException, InvalidUUIDException {
        UUID vehicleId = createVehicleId();

        MessagesDTO messages = vehicleController.removeVehicle(vehicleId.toString()).getBody();

        assertEquals("Deleted vehicle with id <"+vehicleId.toString()+"> from database.", messages.getMessages()[0]);
    }

    @Test
    public void handle_shouldDoNothing_ifItsAnUnknownEventType() throws InvalidVehicleStateException, NotFoundException, DeserializationException {
        KafkaMessage dto = createKafkaMessage();
        dto.getMessage().setEventType("Unknown");

        vehicleController.handleRideEvent(dto);

        verifyNoInteractions(vehicleService);
    }

    @Test
    public void handle_shouldCallCheckoutVehicle_forRideStartedEvents() throws InvalidVehicleStateException, NotFoundException, DeserializationException, JsonProcessingException {
        KafkaMessage dto = createKafkaMessage();
        RideStarted rideStarted = createRideStarted();
        dto.getMessage().setEventType(RideStarted.EVENT_TYPE);
        TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String, Object>>() {};
        dto.getMessage().setEventData(mapper.convertValue(rideStarted, typeRef));

        vehicleController.handleRideEvent(dto);

        verify(vehicleService).checkoutVehicle(rideStarted.getVehicleId(), rideStarted.getStartTime());
    }

    @Test
    public void handle_shouldCallCheckinVehicle_forRideEndedEvents() throws InvalidVehicleStateException, NotFoundException, DeserializationException {
        KafkaMessage dto = createKafkaMessage();
        RideEnded rideEnded = createRideEnded();
        dto.getMessage().setEventType(RideEnded.EVENT_TYPE);
        TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String, Object>>() {};
        dto.getMessage().setEventData(mapper.convertValue(rideEnded, typeRef));

        vehicleController.handleRideEvent(dto);

        verify(vehicleService).checkinVehicle(
                rideEnded.getVehicleId(),
                rideEnded.getLatitude(),
                rideEnded.getLongitude(),
                rideEnded.getBattery(),
                rideEnded.getEndTime());
    }

}