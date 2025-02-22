package com.healthy.backend.service;

import com.healthy.backend.entity.Notifications;
import com.healthy.backend.entity.TimeSlots;
import com.healthy.backend.enums.NotificationType;
import com.healthy.backend.exception.ResourceInvalidException;
import com.healthy.backend.exception.ResourceNotFoundException;
import com.healthy.backend.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {
    
    private final NotificationRepository notificationRepository;
    private final GeneralService __;

    public void createAppointmentNotification(String userId, String title, String message, String appointmentId) {
        Notifications notification = new Notifications();
        notification.setNotificationID(__.generateNextNotificationID());
        notification.setUserID(userId);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(NotificationType.APPOINTMENT);
        notification.setAppointmentID(appointmentId);
        notificationRepository.save(notification);
    }

    public void createProgramNotification(String userId, String title, String message, String programId) {
        Notifications notification = new Notifications();
        notification.setNotificationID(__.generateNextNotificationID());
        notification.setUserID(userId);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(NotificationType.PROGRAM);
        notification.setAppointmentID(programId);
        notificationRepository.save(notification);
    }

    public void createSurveyNotification(String userId, String title, String message, String surveyId) {
        Notifications notification = new Notifications();
        notification.setNotificationID(__.generateNextNotificationID());
        notification.setUserID(userId);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(NotificationType.SURVEY);
        notification.setAppointmentID(surveyId);
        notificationRepository.save(notification);
    }

    public List<Notifications> getUserReadNotifications(String userId) {
        return notificationRepository.findByUserIDAndIsReadTrueOrderByCreatedAtDesc(userId);
    }

    public List<Notifications> getUserUnreadNotifications(String userId) {
        return notificationRepository.findByUserIDAndIsReadFalseOrderByCreatedAtDesc(userId);
    }

    public List<Notifications> getUserNotifications(String userId) {
        return notificationRepository.findByUserIDOrderByCreatedAtDesc(userId);
    }

    public void markAsRead(String notificationId) {
        Notifications notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));
        notification.setIsRead(true);
        notificationRepository.save(notification);
    }
}