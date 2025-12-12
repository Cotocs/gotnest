package com.gotnest.notification.controller;

import com.gotnest.notification.dto.NotificationListResponseDTO;
import com.gotnest.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/bff/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping(path = "/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<NotificationListResponseDTO> getUserNotifications(@PathVariable String userId) {
        return Mono.fromCallable(() -> notificationService.getUserNotifications(userId));
    }
}
