package com.pulsewatch.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Base exception for all PulseWatch business-rule violations.
 * The HTTP status is carried with the exception so the global
 * handler can translate it into the correct response code.
 */
public class PulseWatchException extends RuntimeException {

    private final HttpStatus status;

    public PulseWatchException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
