package com.pulsewatch.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Thrown when a request conflicts with the current state of a resource.
 * Examples: duplicate email, monitor already paused, incident already open.
 * Always maps to 409 Conflict.
 */
public class DuplicateResourceException extends PulseWatchException {

    public DuplicateResourceException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
