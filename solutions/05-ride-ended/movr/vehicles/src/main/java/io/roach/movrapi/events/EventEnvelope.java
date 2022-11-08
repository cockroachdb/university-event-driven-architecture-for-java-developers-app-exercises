package io.roach.movrapi.events;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class EventEnvelope {
    @JsonProperty("event_type")
    private String eventType;

    @JsonProperty("event_data")
    private Map<String, Object> eventData;

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Map<String, Object> getEventData() {
        return eventData;
    }

    public void setEventData(Map<String, Object> eventData) {
        this.eventData = eventData;
    }
}
