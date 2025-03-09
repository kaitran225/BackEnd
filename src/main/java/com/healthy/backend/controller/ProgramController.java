package com.healthy.backend.controller;

import com.healthy.backend.dto.programs.*;
import com.healthy.backend.entity.Users;
import com.healthy.backend.enums.Role;
import com.healthy.backend.exception.OperationFailedException;
import com.healthy.backend.exception.ResourceNotFoundException;
import com.healthy.backend.repository.StudentRepository;
import com.healthy.backend.security.TokenService;
import com.healthy.backend.service.ProgramService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/programs")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Programs Controller", description = "Program related APIs")
public class ProgramController {

    private final StudentRepository studentRepository;
    private final ProgramService programService;
    private final TokenService tokenService;

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////GET REQUESTS//////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // Get all programs
    @ApiResponse(
            responseCode = "200",
            description = "Programs retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProgramsResponse.class),
                    examples = @ExampleObject(
                            name = "Success Response",
                            value = "{ \"status\": \"success\", \"data\": [ { \"programId\": \"P001\", \"name\": \"Program A\", \"status\": \"Active\" }, { \"programId\": \"P002\", \"name\": \"Program B\", \"status\": \"Inactive\" } ] }"
                    )
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid program ID format",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            name = "Bad Request Example",
                            value = "{\"error\": \"Invalid program ID format\", \"status\": 400}"
                    )
            )
    )
    @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            name = "Internal Server Error Example",
                            value = "{\"error\": \"Unexpected error occurred\", \"status\": 500}"
                    )
            )
    )
    @Operation(summary = "Get all programs", description = "Returns a list of all programs.")
    @GetMapping()
    public ResponseEntity<List<ProgramsResponse>> getPrograms(HttpServletRequest request) {
        if (!tokenService.validateRoles(request, List.of(Role.MANAGER, Role.PSYCHOLOGIST, Role.STUDENT, Role.PARENT))) {
            throw new OperationFailedException("You don't have permission to get program tags");
        }
        Users user = tokenService.retrieveUser(request);
        List<ProgramsResponse> programsResponseList = programService.getAllPrograms(
                switch (user.getRole()) {
                    case STUDENT -> tokenService.getRoleID(user);
                    case PSYCHOLOGIST, MANAGER, PARENT -> null;
                    default ->
                            throw new IllegalArgumentException("Invalid role: " + user.getRole());
                }
        );
        if (programsResponseList.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(programsResponseList);
    }

    // Get all program details
    @ApiResponse(
            responseCode = "200",
            description = "Programs retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProgramsResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid program ID format"
    )
    @ApiResponse(
            responseCode = "401",
            description = "Unauthorized access"
    )
    @ApiResponse(
            responseCode = "500",
            description = "Internal server error"
    )
    @Operation(summary = "Get all program statuses", description = "Returns a list of all program statuses.")
    @GetMapping("/details/all")
    public ResponseEntity<?> getAllProgramStatuses(HttpServletRequest request) {
        if (!tokenService.validateRoles(request, List.of(Role.MANAGER, Role.PSYCHOLOGIST))) {
            throw new OperationFailedException("You don't have permission to view data for all programs");
        }
        List<ProgramsResponse> programsResponseList = programService.getAllProgramsDetails();
        if (programsResponseList.isEmpty()) throw new ResourceNotFoundException("No programs found");
        return ResponseEntity.ok(programsResponseList);
    }

    // Get program details
    @Operation(summary = "Get program details", description = "Returns details of a specific program.")
    @GetMapping("/details")
    public ResponseEntity<?> getProgramDetails(@RequestParam String programId, HttpServletRequest request) {
        if (!tokenService.validateRoles(request, List.of(Role.MANAGER, Role.PSYCHOLOGIST, Role.STUDENT, Role.PARENT))) {
            throw new OperationFailedException("You don't have permission to get program tags");
        }
        Users user = tokenService.retrieveUser(request);
        ProgramsResponse programsResponse = programService.getProgramById(programId,
                switch (user.getRole()) {
                    case STUDENT -> tokenService.getRoleID(user);
                    case PSYCHOLOGIST, MANAGER, PARENT -> null;
                    default ->
                            throw new IllegalArgumentException("Invalid role: " + user.getRole());
                }
        );
        if (programsResponse == null) throw new ResourceNotFoundException("Program not found");
        return ResponseEntity.ok(programsResponse);
    }

    // Get program status
    @Operation(summary = "Get program status",
            description = "Returns the status of a specific program.")
    @GetMapping("/status")
    public ResponseEntity<?> getRegistrationStatus(
            @RequestParam String programId,
            HttpServletRequest request) {
        if (!tokenService.validateRoles(request, List.of(Role.MANAGER, Role.PSYCHOLOGIST, Role.STUDENT, Role.PARENT))) {
            throw new OperationFailedException("You don't have permission to get program tags");
        }
        String status = programService.getProgramStatus(programId);
        if (status.isEmpty()) {
            throw new ResourceNotFoundException("Program not found");
        }
        return ResponseEntity.ok("Status for program " + programId + " is " + status);
    }

    // Get enrolled programs of a student
    @Operation(summary = "Get enrolled programs of a student",
            description = "Returns a list of enrolled program.")
    @GetMapping("/enrolled")
    public ResponseEntity<List<ProgramsResponse>> getEnrolledPrograms(
            @RequestParam(required = false) String studentId,
            HttpServletRequest request) {
        String finalStudentId = validateStudentID(request, studentId);
        if (!tokenService.getRoleID(tokenService.retrieveUser(request)).equals(finalStudentId)
                && !tokenService.isManager(request)) {
            throw new OperationFailedException("You don't have permission to view this student's enrolled programs");
        }
        List<ProgramsResponse> programsResponseList = programService.getEnrolledPrograms(finalStudentId);
        if (programsResponseList.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(programsResponseList);
    }

    @Operation(summary = "Check psychologist availability", description = "Checks if a psychologist is available for a specific date range.")
    @GetMapping("/psychologists_availability")
    public boolean checkPsychologistAvailability(
            @RequestParam String psychologistId,
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam String dateOfWeek,
            @RequestParam String startTime,
            @RequestParam String endTime) {
        return programService.isFacilitatorAvailable(psychologistId, startDate, endDate, dateOfWeek, startTime, endTime);
    }

    // Get program participants
    @Operation(summary = "Get program participants",
            description = "Returns a list of participants for a specific program.")
    @GetMapping("/participants")
    public ResponseEntity<ProgramsResponse> getProgramParticipants(
            @RequestParam String programId,
            HttpServletRequest request) {
        if (!tokenService.validateRoles(request, List.of(Role.MANAGER, Role.PSYCHOLOGIST))) {
            throw new OperationFailedException("You don't have permission to get participants of this program");
        }
        ProgramsResponse programsResponse = programService.getProgramParticipants(programId);
        if (programsResponse == null) throw new ResourceNotFoundException("Program not found");
        return ResponseEntity.ok(programsResponse);
    }

    // Get program tags
    @Operation(summary = "Get program tags", description = "Returns a list of tags for programs.")
    @GetMapping("/tags")
    public ResponseEntity<List<ProgramTagResponse>> getProgramTags(HttpServletRequest request) {
        if (!tokenService.validateRoles(request, List.of(Role.MANAGER, Role.PSYCHOLOGIST, Role.STUDENT, Role.PARENT))) {
            throw new OperationFailedException("You don't have permission to get program tags");
        }
        List<ProgramTagResponse> programTagResponseList = programService.getProgramTags();
        if (programTagResponseList.isEmpty()) throw new ResourceNotFoundException("No tags found");
        return ResponseEntity.ok(programTagResponseList);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////POST REQUESTS/////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Register for a program
    @Operation(summary = "Register for a program", description = "Registers a student for a program.")
    @PostMapping("/register")
    public ResponseEntity<?> registerForProgram(
            @RequestParam String programId,
            HttpServletRequest request) {
        if (!tokenService.isStudent(request)) {
            throw new OperationFailedException("You don't have permission to register for a program");
        }
        String studentId = tokenService.getRoleID(tokenService.retrieveUser(request));
        if (programService.registerForProgram(programId, studentId)) {
            return ResponseEntity.ok("Registration successful for program " + programId);
        }
        throw new ResourceNotFoundException("Failed to register for program");
    }

    // Submit feedback
    @PostMapping("/tags/create")
    public ResponseEntity<?> createProgramTag(
            @RequestBody ProgramTagRequest programTagRequest,
            HttpServletRequest request) {
        if (!tokenService.validateRoles(request, List.of(Role.MANAGER, Role.PSYCHOLOGIST))) {
            throw new OperationFailedException("You don't have permission to create this program tag");
        }
        ProgramTagResponse programTagResponse = programService.createProgramTag(programTagRequest);
        if (programTagResponse == null) throw new ResourceNotFoundException("Failed to create tag");
        return ResponseEntity.status(HttpStatus.CREATED).body(programTagResponse);
    }

    // Submit feedback
    @Operation(deprecated = true, summary = "Submit feedback", description = "Submits feedback for a specific program.")
    @PostMapping("/{programId}/feedback")
    public ResponseEntity<?> submitFeedback(@RequestParam String programId) {
        return ResponseEntity.ok("Feedback submitted for program " + programId);
    }

    // Create a new program
    @Operation(summary = "Create a new program", description = "Creates a new program.")
    @PostMapping("/create")
    public ResponseEntity<?> createProgram(@RequestBody ProgramsRequest programsRequest, HttpServletRequest request) {
        if (!tokenService.validateRoles(request, List.of(Role.MANAGER, Role.PSYCHOLOGIST))) {
            throw new OperationFailedException("You don't have permission to create this program");
        }
        String managerId = tokenService.retrieveUser(request).getUserId();
        ProgramsResponse programsResponse = programService.createProgram(programsRequest, managerId);
        if (programsResponse.getProgramID() == null) throw new OperationFailedException("Failed to create program");
        return ResponseEntity.status(HttpStatus.CREATED).body(programsResponse);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////PUT REQUESTS//////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // Update a program
    @Operation(summary = "Update a program",
            description = "Updates an existing program.")
    @PutMapping("/edit")
    public ResponseEntity<ProgramsResponse> updateProgram(
            @RequestParam String programId,
            @RequestBody ProgramUpdateRequest programsRequest,
            HttpServletRequest request) {
        if (!tokenService.validateRoles(request, List.of(Role.MANAGER, Role.PSYCHOLOGIST))) {
            throw new OperationFailedException("You don't have permission to update this program");
        }

        ProgramsResponse response = programService.updateProgram(programId, programsRequest);
        return ResponseEntity.ok(response);
    }

    // Cancel registration
    @Operation(summary = "Cancel registration for a program", description = "Cancels registration for a program.")
    @PutMapping("/cancel-request")
    public ResponseEntity<String> cancelParticipation(
            @RequestParam String programId,
            HttpServletRequest request) {
        String studentId = tokenService.getRoleID(tokenService.retrieveUser(request));
        if (!tokenService.getRoleID(tokenService.retrieveUser(request)).equals(studentId)
                && !tokenService.isManager(request)) {
            throw new OperationFailedException("You don't have permission to cancel this student participation");
        }
        boolean isCancelled = programService.cancelParticipation(programId, studentId);
        if (isCancelled) {
            return ResponseEntity.ok("Participation successfully cancelled.");
        }
        throw new OperationFailedException("Failed to cancel participation.");
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////DELETE REQUESTS////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // Delete a program
    @Operation(summary = "Delete a program", description = "Deletes an existing program.")
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteProgram(@RequestParam String programId,
                                           HttpServletRequest request) {
        if (!tokenService.validateRoles(request, List.of(Role.MANAGER, Role.PSYCHOLOGIST))) {
            throw new OperationFailedException("You don't have permission to delete this program");
        }
        if (!programService.deleteProgram(programId))
            throw new ResourceNotFoundException("Program not found");
        return ResponseEntity.noContent().build();
    }

    private String validateStudentID(HttpServletRequest request, String studentId) {
        if (studentId == null) {
            return tokenService.getRoleID(tokenService.retrieveUser(request));
        }
        if (!studentRepository.existsById(studentId)) {
            return tokenService.getRoleID(tokenService.retrieveUser(request));
        }
        return studentId;
    }
}