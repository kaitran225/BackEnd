package com.healthy.backend.service;

import com.healthy.backend.entity.Notifications;
import com.healthy.backend.entity.TimeSlots;
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

    public void createAppointmentNotification(String userId, String title, String message, String appointmentId) {
        Notifications notification = new Notifications();
        notification.setNotificationID(generateNextNotificationID());
        notification.setUserID(userId);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(Notifications.Type.Appointment);
        notification.setAppointmentID(appointmentId);
        notificationRepository.save(notification);
    }

    public void createProgramNotification(String userId, String title, String message, String programId) {
        Notifications notification = new Notifications();
        notification.setNotificationID(generateNextNotificationID());
        notification.setUserID(userId);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(Notifications.Type.Program);
        notification.setAppointmentID(programId);
        notificationRepository.save(notification);
    }

    public void createSurveyNotification(String userId, String title, String message, String surveyId) {
        Notifications notification = new Notifications();
        notification.setNotificationID(generateNextNotificationID());
        notification.setUserID(userId);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(Notifications.Type.Survey);
        notification.setAppointmentID(surveyId);
        notificationRepository.save(notification);
    }

    private String generateNextNotificationID() {
        return notificationRepository.findLastNotificationID().stream()
                .findFirst()
                .filter(id -> id.startsWith("NOT"))
                .map(id -> {
                    try {
                        return Integer.parseInt(id.substring(3)) + 1;
                    } catch (NumberFormatException e) {
                        return 1; // Reset to 1 if parsing fails
                    }
                })
                .orElse(1)
                .toString()
                .formatted("NOT%01d"); // Format the new ID
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