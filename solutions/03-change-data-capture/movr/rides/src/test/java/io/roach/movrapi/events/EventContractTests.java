package io.roach.movrapi.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import java.time.LocalDateTime;
import java.util.Map;

import static io.roach.movrapi.util.TestHelpers.createRideStarted;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/*
These are "contract" tests. They ensure that the serialized events match the format expected by downstream
applications.
If these tests start failing, be wary. You may have broken the contract.
*/
@JsonTest
class EventContractTests {

    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    public void init() {
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    }

    @Test
    public void rideStarted_shouldProduceTheExpectedJson() throws JsonProcessingException {
        RideStarted event = createRideStarted();

        String json = mapper.writeValueAsString(event);

        TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String, Object>>() {};
        Map<String, Object> eventMap = mapper.readValue(json, typeRef);
        assertEquals(4, eventMap.size());
        assertEquals(event.getRideId().toString(), eventMap.get("ride_id"));
        assertEquals(event.getUserEmail(), eventMap.get("user_email"));
        assertEquals(event.getVehicleId().toString(), eventMap.get("vehicle_id"));
        assertEquals(event.getStartTime(), LocalDateTime.parse((String) eventMap.get("start_time")));
    }
}