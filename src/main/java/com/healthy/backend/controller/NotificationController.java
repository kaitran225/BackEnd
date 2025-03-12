package com.healthy.backend.controller;

import com.healthy.backend.dto.notification.NotificationResponse;
import com.healthy.backend.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Notification Controller", description = "Notification Management APIs")
public class NotificationController {

    private final NotificationService notificationService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @Operation(summary = "Get all notifications")
    @GetMapping("")
    public ResponseEntity<List<NotificationResponse>> getNotifications(
            @RequestParam(required = false) String userId,
            HttpServletRequest request) {
        List<NotificationResponse> responses = notificationService.getUserNotifications(userId, request);
        return ResponseEntity.ok(responses);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @Operation(summary = "Mark notification as read")
    @PostMapping("/{notificationId}/read")
    public ResponseEntity<Void> markAsRead(
            @RequestParam(required = false)String notificationId,
            HttpServletRequest request) {
        notificationService.markAsRead(notificationId, request);
        return ResponseEntity.ok().build();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @Operation(summary = "Get all read notifications")
    @GetMapping("/read")
    public ResponseEntity<List<NotificationResponse>> getReadNotifications(
            @RequestParam(required = false) String userId,
            HttpServletRequest request) {
        List<NotificationResponse> responses = notificationService.getUserReadNotifications(userId, request);
        return ResponseEntity.ok(responses);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @Operation(summary = "Get all unread notifications")
    @GetMapping("/unread")
    public ResponseEntity<List<NotificationResponse>> getUnreadNotifications(
            @RequestParam(required = false) String userId,
            HttpServletRequest request) {
        List<NotificationResponse> responses = notificationService.getUserUnreadNotifications(userId, request);
        return ResponseEntity.ok(responses);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @Operation(summary = "Get all notifications from the database")
    @GetMapping("/all")
    public ResponseEntity<List<NotificationResponse>> getAllNotifications(HttpServletRequest request) {
        List<NotificationResponse> responses = notificationService.getAllNotifications(request);
        return ResponseEntity.ok(responses);
    }
}