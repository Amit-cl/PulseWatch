package com.pulsewatch.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Thrown when a requested resource does not exist.
 * Always maps to 404 Not Found.
 */
public class ResourceNotFoundException extends PulseWatchException {

    public ResourceNotFoundException(String resource, Long id) {
        super(resource + " not found with id: " + id, HttpStatus.NOT_FOUND);
    }

    public ResourceNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
