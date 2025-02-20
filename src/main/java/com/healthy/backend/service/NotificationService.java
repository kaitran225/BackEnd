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

    public void createNotification(String userId, String title, String message,
                                   Notifications.Type type, String appointmentId) {
        Notifications notification = new Notifications();
        notification.setNotificationID(generateNextNotificationID());
        notification.setUserID(userId);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(type);
        notification.setAppointmentID(appointmentId); // Lưu ID appointment riêng
        notificationRepository.save(notification);
    }
    private String generateNextNotificationID() {
        List<String> lastNotificationIDs = notificationRepository.findLastNotificationID();

        String lastNotificationID = null;
        if (!lastNotificationIDs.isEmpty()) {
            lastNotificationID = lastNotificationIDs.get(0); // Get the most recent notification ID
        }

        // Parse the numeric part of the ID and increment it
        int nextID = 1; // Default to 1 if no previous IDs exist
        if (lastNotificationID != null && lastNotificationID.startsWith("NOT")) {
            String numericPart = lastNotificationID.substring(3); // Extract the numeric part
            try {
                nextID = Integer.parseInt(numericPart) + 1; // Increment the numeric part
            } catch (NumberFormatException e) {
                // Handle invalid format, if necessary
                nextID = 1; // Reset to 1 if parsing fails
            }
        }

        return String.format("NOT%01d", nextID);
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