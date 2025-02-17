package com.healthy.backend.dto.notification;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Builder
@Data
public class NotificationResponse {
    private String id;
    private String title;
    private String message;
    private String type;
    private LocalDateTime createdAt;
    private Boolean isRead;


}