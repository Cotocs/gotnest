package com.gotnest.notification.controller;

import com.gotnest.notification.dto.NotificationListResponseDTO;
import com.gotnest.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/user/{userId}")
    public Mono<ResponseEntity<NotificationListResponseDTO>> getUserNotifications(
            @PathVariable String userId) {
        NotificationListResponseDTO response = notificationService.getUserNotifications(userId);
        return Mono.just(ResponseEntity.ok(response));
    }
}
