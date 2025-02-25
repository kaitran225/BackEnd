package com.healthy.backend.mapper;

import com.healthy.backend.dto.notification.NotificationResponse;
import com.healthy.backend.entity.Notifications;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {
    public NotificationResponse toResponse(Notifications notification) {
        return NotificationResponse.builder()
                .id(notification.getNotificationID())
                .IDType(notification.getAppointmentID() != null ? notification.getAppointmentID() : 
                        notification.getLeaveRequestID() != null ? notification.getLeaveRequestID() : 
                        notification.getProgramID() != null ? notification.getProgramID() : 
                        notification.getSurveyID())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .type(notification.getType().name())
                .createdAt(notification.getCreatedAt())
                .isRead(notification.getIsRead())
                .UserID(notification.getUserID())
                .build();
    }


}