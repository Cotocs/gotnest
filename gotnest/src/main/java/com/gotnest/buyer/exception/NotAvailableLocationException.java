package com.gotnest.buyer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NotAvailableLocationException extends ResponseStatusException {

    private static final String DEFAULT_MESSAGE =
            "We don’t have this location available at the moment. Please check other locations.";

    public NotAvailableLocationException() {
        super(HttpStatus.UNPROCESSABLE_ENTITY, DEFAULT_MESSAGE);
    }

    public NotAvailableLocationException(String reason) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, reason == null || reason.isBlank() ? DEFAULT_MESSAGE : reason);
    }
}

