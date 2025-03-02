package com.healthy.backend.service;

import com.healthy.backend.entity.Notifications;
import com.healthy.backend.entity.TimeSlots;
import com.healthy.backend.enums.NotificationType;
import com.healthy.backend.enums.Role;
import com.healthy.backend.exception.ResourceInvalidException;
import com.healthy.backend.exception.ResourceNotFoundException;
import com.healthy.backend.repository.NotificationRepository;
import com.healthy.backend.repository.UserRepository;
import com.healthy.backend.security.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {
    
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final GeneralService __;

    public void createAppointmentNotification(String userId, String title, String message, String appointmentId) {
        Notifications notification = new Notifications();
        notification.setNotificationID(__.generateNextNotificationID());
        notification.setUserID(userId);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(NotificationType.APPOINTMENT);
        notification.setAppointmentID(appointmentId);
        notification.setIsRead(false);
        notificationRepository.save(notification);
    }
    
    public void createOnLeaveNotification(String userId, String title, String message, String leaveRequestId) {
        Notifications notification = new Notifications();
        notification.setNotificationID(__.generateNextNotificationID());
        notification.setUserID(userId);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(NotificationType.ONLEAVE);
        notification.setLeaveRequestID(leaveRequestId); 
        notification.setIsRead(false);
        notificationRepository.save(notification);
    }

    public void createProgramNotification(String userId, String title, String message, String programId) {
        Notifications notification = new Notifications();
        notification.setNotificationID(__.generateNextNotificationID());
        notification.setUserID(userId);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(NotificationType.PROGRAM);
        notification.setProgramID(programId);
        notification.setIsRead(false);
        notificationRepository.save(notification);
    }

    public void createSurveyNotification(String userId, String title, String message, String surveyId) {
        Notifications notification = new Notifications();
        notification.setNotificationID(__.generateNextNotificationID());
        notification.setUserID(userId);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(NotificationType.SURVEY);
        notification.setSurveyID(surveyId);
        notification.setIsRead(false);
        notificationRepository.save(notification);
    }

    public List<Notifications> getUserReadNotifications(String userId, HttpServletRequest request) {
        String finalUserId = validateUserID(request, userId);
        if (tokenService.validateUID(request, finalUserId) && !tokenService.validateRole(request, Role.MANAGER)) {
            throw new ResourceInvalidException("You can not get other users notifications");
        }
        return notificationRepository.findByUserIDAndIsReadTrueOrderByCreatedAtDesc(finalUserId);
    }

    public List<Notifications> getUserUnreadNotifications(String userId, HttpServletRequest request) {
        String finalUserId = validateUserID(request, userId);
        if (tokenService.validateUID(request, finalUserId) && !tokenService.validateRole(request, Role.MANAGER)) {
            throw new ResourceInvalidException("You can not get other users notifications");
        }
        return notificationRepository.findByUserIDAndIsReadFalseOrderByCreatedAtDesc(finalUserId);
    }

    public List<Notifications> getUserNotifications(String userId, HttpServletRequest request) {
        String finalUserId = validateUserID(request, userId);
        if (tokenService.validateUID(request, finalUserId) && !tokenService.validateRole(request, Role.MANAGER) ) {
            throw new ResourceInvalidException("You can not get other users notifications");
        }
        return notificationRepository.findByUserIDOrderByCreatedAtDesc(finalUserId);
    }

    public void markAsRead(String notificationId, HttpServletRequest request) {
        Notifications notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));
        notification.setIsRead(true);
        notificationRepository.save(notification);
    }

    public List<Notifications> getAllNotifications(HttpServletRequest request) {
        if (tokenService.validateRole(request, Role.MANAGER)) {
            throw new ResourceInvalidException("You can not get database notifications");
        }
        return notificationRepository.findAll();
    }

    private String validateUserID(HttpServletRequest request, String userId) {
        if(userId == null) {
            return tokenService.retrieveUser(request).getUserId();
        }
        if(!userRepository.existsById(userId)) {
            return tokenService.retrieveUser(request).getUserId();
        }
        return userId;
    }
}