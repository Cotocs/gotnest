package com.gotnest.buyer.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatus(ResponseStatusException ex,
                                                                    ServerWebExchange exchange) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("path", exchange.getRequest().getPath().value());
        body.put("status", ex.getStatusCode().value());
        String error = ex.getStatusCode() == HttpStatus.UNPROCESSABLE_ENTITY ? "Unprocessable Content" :
                (ex.getReason() != null ? ex.getReason() : ex.getStatusCode().toString());
        body.put("error", error);
        if (ex.getStatusCode() == HttpStatus.UNPROCESSABLE_ENTITY) {
            // Diferenciar mensagens 422 conforme o tipo de erro
            String reason = ex.getReason() != null ? ex.getReason() : "";
            if (reason.contains("location available")) {
                body.put("message", "We don’t have this location available at the moment. Please check other locations.");
            } else if (reason.contains("couldn’t find any property")) {
                body.put("message", "We couldn’t find any property for the address you searched for.");
            } else {
                body.put("message", reason);
            }
        } else if (ex.getReason() != null) {
            body.put("message", ex.getReason());
        }
        return ResponseEntity.status(ex.getStatusCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body(body);
    }
}
