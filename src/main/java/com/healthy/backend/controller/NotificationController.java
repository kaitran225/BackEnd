package com.healthy.backend.controller;

import com.healthy.backend.dto.notification.NotificationResponse;
import com.healthy.backend.entity.Notifications;
import com.healthy.backend.entity.Users;
import com.healthy.backend.mapper.NotificationMapper;
import com.healthy.backend.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Notification Controller", description = "Notification Management APIs")
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificationMapper notificationMapper;

    @Operation(summary = "Get all notifications")
    @GetMapping("/{id}")
    public ResponseEntity<List<NotificationResponse>> getNotifications(@PathVariable String id) {
        List<Notifications> notifications = notificationService.getUserNotifications(id);
        List<NotificationResponse> responses = notifications.stream()
                .map(notificationMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Mark notification as read")
    @PostMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable String id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get all read notifications")
    @GetMapping("/{userId}/read")
    public ResponseEntity<List<NotificationResponse>> getReadNotifications(@PathVariable String userId) {
        List<Notifications> notifications = notificationService.getUserReadNotifications(userId);
        List<NotificationResponse> responses = notifications.stream()
                .map(notificationMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Get all unread notifications")
    @GetMapping("/{userId}/unread")
    public ResponseEntity<List<NotificationResponse>> getUnreadNotifications(@PathVariable String userId) {
        List<Notifications> notifications = notificationService.getUserUnreadNotifications(userId);
        List<NotificationResponse> responses = notifications.stream()
                .map(notificationMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
}