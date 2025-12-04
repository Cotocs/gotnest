package com.gotnest.buyer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AddressNotFoundException extends ResponseStatusException {
    private static final String DEFAULT_MESSAGE =
            "We couldn’t find any property for the address you searched for.";

    public AddressNotFoundException() {
        super(HttpStatus.UNPROCESSABLE_ENTITY, DEFAULT_MESSAGE);
    }

    public AddressNotFoundException(String reason) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, reason == null || reason.isBlank() ? DEFAULT_MESSAGE : reason);
    }
}

