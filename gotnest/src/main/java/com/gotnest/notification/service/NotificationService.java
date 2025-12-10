package com.gotnest.notification.service;

import com.gotnest.notification.dto.NotificationDTO;
import com.gotnest.notification.dto.NotificationListResponseDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class NotificationService {

    public NotificationListResponseDTO getUserNotifications(String userId) {
        // Mock data - based on the image provided
        List<NotificationDTO> notifications = Arrays.asList(
                NotificationDTO.builder()
                        .id(UUID.randomUUID().toString())
                        .message("Jay recommended good properties save your favourites.")
                        .senderName("Jay")
                        .senderProfileImage("https://fastly.picsum.photos/id/64/4326/2884.jpg?hmac=9_SzX666YRpR_fOyYStXpfSiJ_edO3ghlSRnH2w09Kg")
                        .timestamp(LocalDateTime.now().minusDays(1))
                        .read(false)
                        .build(),
                NotificationDTO.builder()
                        .id(UUID.randomUUID().toString())
                        .message("Time to compare and organize save your favourites.")
                        .senderName("Favorite homes")
                        .senderProfileImage("https://fastly.picsum.photos/id/64/4326/2884.jpg?hmac=9_SzX666YRpR_fOyYStXpfSiJ_edO3ghlSRnH2w09Kg")
                        .timestamp(LocalDateTime.now().minusDays(2))
                        .read(false)
                        .build(),
                NotificationDTO.builder()
                        .id(UUID.randomUUID().toString())
                        .message("Time to compare and organize save your favourites.")
                        .senderName("Andre Luiz")
                        .senderProfileImage("https://fastly.picsum.photos/id/64/4326/2884.jpg?hmac=9_SzX666YRpR_fOyYStXpfSiJ_edO3ghlSRnH2w09Kg")
                        .timestamp(LocalDateTime.now().minusDays(5))
                        .read(false)
                        .build()
        );

        long unreadCount = notifications.stream()
                .filter(n -> !n.isRead())
                .count();

        return NotificationListResponseDTO.builder()
                .notifications(notifications)
                .totalCount(notifications.size())
                .unreadCount((int) unreadCount)
                .build();
    }
}
