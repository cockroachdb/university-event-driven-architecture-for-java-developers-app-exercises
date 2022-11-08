package io.roach.movrapi.events;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KafkaMessage {
    @JsonProperty("after")
    private EventEnvelope message;

    public EventEnvelope getMessage() {
        return message;
    }

    public void setMessage(EventEnvelope message) {
        this.message = message;
    }
}
