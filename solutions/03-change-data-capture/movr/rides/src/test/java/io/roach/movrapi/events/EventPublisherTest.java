package io.roach.movrapi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;

import static io.roach.movrapi.util.TestHelpers.createRideStarted;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

class EventPublisherTest {

    @Mock
    private EventRepository mockRepository;

    private EventPublisher publisher;

    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        publisher = new EventPublisher(mockRepository, mapper);
    }

    @Test
    public void publish_shouldWriteTheSerializedEventToTheRepository() {
        RideStarted event = createRideStarted();

        publisher.publish(RideStarted.EVENT_NAME, event);

        ArgumentCaptor<EventEnvelope> envelopeCaptor = ArgumentCaptor.forClass(EventEnvelope.class);

        verify(mockRepository).save(envelopeCaptor.capture());

        EventEnvelope envelope = envelopeCaptor.getValue();
        assertEquals(RideStarted.EVENT_NAME, envelope.getEventType());
        assertEquals(mapper.convertValue(event, Map.class), envelope.getEventData());
    }

}