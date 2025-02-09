package com.healthy.backend.controller;

import com.healthy.backend.dto.appointment.AppointmentResponse;
import com.healthy.backend.dto.survey.SurveyResultsResponse;
import com.healthy.backend.dto.user.UsersResponse;
import com.healthy.backend.entity.Programs;
import com.healthy.backend.entity.Users;
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
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "Users related management APIs")
public class UserController {

    private final UserService userService;

    private final ProgramService programService;

    @Operation(
            summary = "Get all users",
            description = "Returns a list of all registered users."
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/users")
    public ResponseEntity<List<UsersResponse>> getAllUsers() {
        List<UsersResponse> users = userService.getAllUsers();
        return users.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(users);
    }

    @Operation(
            summary = "Get user by ID",
            description = "Returns the user with the specified ID."
    )
    @GetMapping("/users/{userId}")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<UsersResponse> getUserById(@PathVariable String userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    // Working but not tested
    @GetMapping("/users/{userId}/programs")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<?> getProgramsByUserId(
            @Valid @PathVariable String userId) {
        try {
            List<Programs> programs = programService.getProgramsByUserId(userId);
            return ResponseEntity.ok(programs);  // Return 200 OK with programs
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    @Operation(
            summary = "Get user appointments",
            description = "Retrieves all appointments for a user."
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/users/{userId}/appointments")
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
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/users/{userId}/surveys")
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
    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping("/users/{userId}/update")
    public ResponseEntity<?> updateUser(@PathVariable String userId, @RequestBody Users updatedUser) {
        UsersResponse updatedUserResponse = userService.updateUser(userId, updatedUser);
        return updatedUserResponse == null
                ? ResponseEntity.noContent().build() // 204 if no changes detected
                : ResponseEntity.ok(updatedUserResponse); // 200 if update successful
    }

}