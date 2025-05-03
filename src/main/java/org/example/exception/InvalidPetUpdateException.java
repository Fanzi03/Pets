package org.example.exception;

public class InvalidPetUpdateException extends RuntimeException {
    public InvalidPetUpdateException(String message) {
        super(message);
    }
}
