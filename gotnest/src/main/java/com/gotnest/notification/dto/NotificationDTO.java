package com.gotnest.notification.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
    private String id;
    private String message;
    private String senderName;
    private String senderProfileImage;
    private LocalDateTime timestamp;
    private boolean read;
}
