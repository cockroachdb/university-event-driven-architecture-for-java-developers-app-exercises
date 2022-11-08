package io.roach.movrapi.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServiceConfigTest {

    @Test
    public void uri_shouldReturnTheFormattedURI() {
        ServiceConfig config = new ServiceConfig("host",1234) {};
        assertEquals("http://host:1234/suffix", config.uri("/suffix"));
    }

}