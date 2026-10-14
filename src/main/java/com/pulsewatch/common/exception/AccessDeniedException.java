package com.pulsewatch.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Thrown when a user tries to access a resource owned by another user.
 * Always maps to 403 Forbidden.
 */
public class AccessDeniedException extends PulseWatchException {

    public AccessDeniedException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }

    public AccessDeniedException() {
        super("You do not have permission to access this resource", HttpStatus.FORBIDDEN);
    }
}
