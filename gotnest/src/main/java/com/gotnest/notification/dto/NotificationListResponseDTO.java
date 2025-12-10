package com.gotnest.notification.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationListResponseDTO {
    private List<NotificationDTO> notifications;
    private int totalCount;
    private int unreadCount;
}
