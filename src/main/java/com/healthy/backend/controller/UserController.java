package com.healthy.backend.controller;

import com.healthy.backend.dto.appointment.AppointmentResponse;
import com.healthy.backend.dto.programs.ProgramParticipationResponse;
import com.healthy.backend.dto.survey.SurveyResultsResponse;
import com.healthy.backend.dto.user.UsersResponse;
import com.healthy.backend.entity.Programs;
import com.healthy.backend.entity.Users;
import com.healthy.backend.exception.ResourceNotFoundException;
import com.healthy.backend.service.ProgramService;
import com.healthy.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "User Controller", description = "Users related management APIs")
public class UserController {

    private final UserService userService;

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

    // Working but not tested
    @Operation(
            summary = "Get programs by user ID",
            description = "Returns a list of programs associated with the specified user ID."
    )
    @GetMapping("/{userId}/programs")
    public ResponseEntity<?> getProgramsByUserId(
            @Valid @PathVariable String userId) {
        try {
            List<ProgramParticipationResponse> programs = userService.getUserProgramsParticipation(userId);
            return ResponseEntity.ok(programs);  // Return 200 OK with programs
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    @Operation(
            summary = "Get user appointments",
            description = "Retrieves all appointments for a user."
    )
    @GetMapping("/{userId}/appointments")
    public ResponseEntity<List<AppointmentResponse>> getAppointmentsByUserId(@PathVariable String userId) {
        List<AppointmentResponse> appointmentResponseList = userService.getUserAppointments(userId);
        return appointmentResponseList.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(appointmentResponseList);
    }


    @Operation(
            summary = "Get user survey results",
            description = "Retrieves all survey results for a user."
    )
    @GetMapping("/{userId}/surveys")
    public ResponseEntity<List<SurveyResultsResponse>> getSurveyResultsByUserId(@PathVariable String userId) {
        List<SurveyResultsResponse> surveyResults = userService.getUserSurveyResults(userId);
        return surveyResults.isEmpty()
                ? ResponseEntity.noContent().build() // 204 No Content if list is empty
                : ResponseEntity.ok(surveyResults);
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
            deprecated = true,
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
    public String updateUserRole(@PathVariable String userId, @RequestBody String role) {
        return "Role updated for user " + userId;
    }

    @Operation(
            deprecated = true,
            summary = "Send notification to user",
            description = "Sends a notification to a specific user."
    )
    @PostMapping("/{userId}/notifications")
    public String sendUserNotification(@PathVariable String userId, @RequestBody String message) {
        return "Notification sent to user " + userId;
    }

    @Operation(
            deprecated = true,
            summary = "Get user dashboard",
            description = "Retrieves the dashboard for a specific user."
    )
    @GetMapping("/{userId}/dashboard")
    public String getUserDashboard(@PathVariable String userId) {
        return "User dashboard for user " + userId;
    }

    @Operation(
            deprecated = true,
            summary = "Export user data",
            description = "Exports user data in a specified format."
    )
    @GetMapping("/{userId}/export")
    public String exportUserData(@PathVariable String userId, @RequestParam String format) {
        return "User data exported in format: " + format;
    }

    @Operation(
            deprecated = true,
            summary = "Get completed programs for user",
            description = "Retrieves a list of completed programs for a specific user."
    )
    @GetMapping("/{userId}/programs/completed")
    public List<String> getCompletedPrograms(@PathVariable String userId) {
        return List.of("Completed programs for user " + userId);
    }

    @Operation(
            deprecated = true,
            summary = "Submit feedback for user",
            description = "Submits feedback for a specific user."
    )
    @PostMapping("/{userId}/feedback")
    public String submitFeedback(@PathVariable String userId, @RequestBody String feedback) {
        return "Feedback submitted by user " + userId;
    }

    @Operation(
            deprecated = true,
            summary = "Get upcoming appointments for user",
            description = "Retrieves a list of upcoming appointments for a specific user."
    )
    @GetMapping("/{userId}/appointments/upcoming")
    public List<String> getUpcomingAppointments(@PathVariable String userId) {
        return List.of("Upcoming appointments for user " + userId);
    }

    @Operation(
            deprecated = true,
            summary = "Get pending surveys for user",
            description = "Retrieves a list of pending surveys for a specific user."
    )
    @GetMapping("/{userId}/surveys/pending")
    public List<String> getPendingSurveys(@PathVariable String userId) {
        return List.of("Pending surveys for user " + userId);
    }

    @Operation(
            deprecated = true,
            summary = "Search users by name",
            description = "Searches for users with a specific name."
    )
    @GetMapping("/search")
    public List<String> searchUsers(@RequestParam String name) {
        return List.of("Search results for users with name: " + name);
    }

    @Operation(
            deprecated = true,
            summary = "Delete user account",
            description = "Deletes a user's account."
    )
    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable String userId) {
        return "User deleted successfully";
    }
}