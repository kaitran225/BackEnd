package com.healthy.backend.controller;

import com.healthy.backend.dto.appointment.AppointmentResponse;
import com.healthy.backend.dto.event.EventResponse;
import com.healthy.backend.dto.user.UsersResponse;
import com.healthy.backend.entity.Users;
import com.healthy.backend.exception.OperationFailedException;
import com.healthy.backend.service.ExportService;
import com.healthy.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "User Controller", description = "Users related management APIs")
public class UserController {

    private final UserService userService;
    private final ExportService exportService;

    @Operation(
            summary = "Get all users",
            description = "Returns a list of all registered users."
    )
    @GetMapping("/")
    public ResponseEntity<List<UsersResponse>> getAllUsers() {
        List<UsersResponse> users = userService.getAllUsers();
        return users.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(users);
    }

    @Operation(
            summary = "Get user by ID",
            description = "Returns the user with the specified ID."
    )
    @GetMapping("/{userId}")
    public ResponseEntity<UsersResponse> getUserById(@PathVariable String userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @Operation(
            summary = "Get user by ID",
            description = "Returns the user details with the specified ID."
    )
    @GetMapping("/{userId}/details")
    public ResponseEntity<UsersResponse> getUserDetailsById(@PathVariable String userId) {
        return ResponseEntity.ok(userService.getUserDetailsById(userId));
    }

    @Operation(
            summary = "Update user details",
            description = "Updates a user's details."
    )
    @PutMapping("/{userId}/update")
    public ResponseEntity<?> updateUser(@PathVariable String userId, @RequestBody Users updatedUser) {
        UsersResponse updatedUserResponse = userService.updateUser(userId, updatedUser);
        return updatedUserResponse == null
                ? ResponseEntity.noContent().build() // 204 if no changes detected
                : ResponseEntity.ok(updatedUserResponse); // 200 if update successful
    }

    @Operation(
            deprecated = true,
            summary = "Deactivate user account",
            description = "Deactivates a user's account."
    )
    @PostMapping("/{userId}/deactivate")
    public String deactivateUser(@PathVariable String userId) {
        return "User account " + userId + " deactivated";
    }

    @Operation(
            summary = "Reactivate user account",
            description = "Reactivates a user's account."
    )
    @PostMapping("/{userId}/reactivate")
    public String reactivateUser(@PathVariable String userId) {
        return "User account " + userId + " reactivated";
    }

    @Operation(
            deprecated = true,
            summary = "Update user role",
            description = "Updates a user's role."
    )
    @PutMapping("/{userId}/role")
    public String updateUserRole(@PathVariable String userId, @RequestParam String role) {
        return userService.getUserById(userId).getRole();
    }

    @Operation(
            summary = "Export user data",
            description = "Exports user data in a specified format (CSV, JSON, or PDF)."
    )
    @GetMapping("/{userId}/export")
    public ResponseEntity<?> exportUserData(@PathVariable String userId, @RequestParam String format) {
        byte[] exportedData;
        return switch (format.toLowerCase()) {
            case "csv" -> {
                exportedData = exportService.exportUserData(userId,"csv");
                yield ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=user_" + userId + ".csv")
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(exportedData);
            }
            case "json" -> {
                exportedData = exportService.exportUserData(userId, "json");
                yield ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=user_" + userId + ".json")
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(exportedData);
            }
            case "pdf" -> {
                exportedData = exportService.exportUserData(userId, "pdf");
                yield ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=user_" + userId + ".pdf")
                        .contentType(MediaType.APPLICATION_PDF)
                        .body(exportedData);
            }
            default -> ResponseEntity.badRequest().body("Invalid format. Supported formats: csv, json, pdf.");
        };
    }

    @Operation(
            summary = "Submit feedback for user",
            description = "Submits feedback for a specific user."
    )
    @PostMapping("/{userId}/feedback")
    public String submitFeedback(@PathVariable String userId, @RequestBody String feedback) {
        return "Feedback submitted by user " + userId;
    }

    @Operation(
            summary = "Search users by name",
            description = "Searches for users with a specific name."
    )
    @GetMapping("/search")
    public ResponseEntity<List<UsersResponse>> searchUsers(@RequestParam String name) {
        List<UsersResponse> list = userService.searchUsers(name);
        if (list.isEmpty()) {
            return ResponseEntity.noContent().build(); // Returns 204 No Content with no body
        }
        return ResponseEntity.ok(list); // Returns 200 OK with the list
    }

    @Operation(
            summary = "Delete user account",
            description = "Deletes a user's account."
    )
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable String userId) {
        if (userService.deleteUser(userId)){
            return ResponseEntity.ok("User account " + userId + " deleted");
        }
        throw new OperationFailedException("Failed to delete user account");
    }

    // Get all user's events
    @Operation(
            summary = "Get all events",
            description = "Returns a list of all events for a specific user."
    )
    @GetMapping("/{userId}/events")
    public ResponseEntity<?> getAllEvents(@PathVariable String userId) {
        EventResponse events = userService.getAllEvents(userId);
        if(events.getEvent().isEmpty()){
            return ResponseEntity.noContent().build();  // Return 204
        }
        return ResponseEntity.ok(userService.getAllEvents(userId));
    }

    @Operation(
            summary = "Get user's appointments",
            description = "Returns all appointments for a specific user (student or psychologist)."
    )
    @GetMapping("/{userId}/appointments")
    public ResponseEntity<List<AppointmentResponse>> getUserAppointments(@PathVariable String userId) {
        List<AppointmentResponse> appointments = userService.getUserAppointments(userId);
        return appointments.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(appointments);
    }
}