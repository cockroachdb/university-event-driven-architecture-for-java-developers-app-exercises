package io.roach.movrapi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

class EventPublisherTest {

    @Mock
    private EventRepository mockRepository;

    private EventPublisher publisher;

    private ObjectMapper mapper = new ObjectMapper();

    private String EVENT_NAME = "test_event";

    class TestEvent implements Event {
        private String field;

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }
    }

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        publisher = new EventPublisher(mockRepository, mapper);
    }

    @Test
    public void publish_shouldWriteTheSerializedEventToTheRepository() {
        TestEvent event = new TestEvent();
        event.setField("SomeValue");

        publisher.publish(EVENT_NAME, event);

        ArgumentCaptor<EventEnvelope> envelopeCaptor = ArgumentCaptor.forClass(EventEnvelope.class);

        verify(mockRepository).save(envelopeCaptor.capture());

        EventEnvelope envelope = envelopeCaptor.getValue();
        assertEquals(EVENT_NAME, envelope.getEventType());
        assertEquals(mapper.convertValue(event, Map.class), envelope.getEventData());
    }

}