package io.roach.movrapi.exception;

/**
 * Thrown when we attempt to do something a vehicle that's not in the correct status (e.g., start a ride on a vehicle that's
 * already marked as "in use"
 */

public class InvalidVehicleStateException extends Exception {

    public InvalidVehicleStateException() {
    }

    public InvalidVehicleStateException(String message) {
        super(message);
    }
}
