package com.healthy.backend.controller;

import com.healthy.backend.dto.programs.ProgramParticipationResponse;
import com.healthy.backend.dto.programs.ProgramsResponse;
import com.healthy.backend.service.ProgramService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/programs")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Programs Controller", description = "Program related APIs")
public class ProgramController {

    private final ProgramService programService;

    @Operation(summary = "Get all programs", description = "Returns a list of all programs.")
    @GetMapping()
    public ResponseEntity<?> getPrograms() {
        List<ProgramsResponse> programsResponseList = programService.getAllPrograms();
        return programsResponseList.isEmpty()
                ? ResponseEntity.noContent().build() // 204 No Content if list is empty
                : ResponseEntity.ok(programsResponseList); // 200 OK if list is not empty
    }


    @Operation(summary = "Get program details", description = "Returns details of a specific program.")
    @GetMapping("/{programId}")
    public ResponseEntity<?> getProgramDetails(@PathVariable String programId) {
        ProgramsResponse programsResponse = programService.getProgramById(programId);
        return programsResponse == null
                ? ResponseEntity.noContent().build()    // 204 No Content if program not found;
                : ResponseEntity.ok(programsResponse);  // 200 OK if program found
    }

    @Operation(deprecated = true, summary = "Register for a program", description = "Registers a student for a program.")
    @PostMapping("/{programId}/register")
    public ResponseEntity<?> registerForProgram(@PathVariable String programId) {
        return ResponseEntity.ok("Registered for program " + programId);
    }

    @Operation(deprecated = true, summary = "Cancel registration for a program", description = "Cancels registration for a program.")
    @PutMapping("/{programId}/cancel-request")
    public ResponseEntity<?> cancelParticipation(@PathVariable String programId) {
        return ResponseEntity.ok("Cancel request for program " + programId);
    }

    @Operation(deprecated = true, summary = "Get program location", description = "Returns the location of a specific program.")
    @GetMapping("/{programId}/location")
    public ResponseEntity<?> getProgramLocation(@PathVariable String programId) {
        return ResponseEntity.ok("Location for program " + programId);
    }

    @Operation(deprecated = true, summary = "Get program status", description = "Returns the status of a specific program.")
    @GetMapping("/{programId}/status")
    public ResponseEntity<?> getRegistrationStatus(@PathVariable String programId) {
        return ResponseEntity.ok("Registration status for program " + programId);
    }

    @Operation(deprecated = true, summary = "Get enrolled programs", description = "Returns a list of enrolled programs.")
    @GetMapping("/enrolled")
    public ResponseEntity<?> getEnrolledPrograms() {
        return ResponseEntity.ok("List of enrolled programs");
    }

    @Operation(deprecated = true, summary = "Submit feedback", description = "Submits feedback for a specific program.")
    @PostMapping("/{programId}/feedback")
    public ResponseEntity<?> submitFeedback(@PathVariable String programId) {
        return ResponseEntity.ok("Feedback submitted for program " + programId);
    }

    // For Managers or Psychologists
    @Operation(deprecated = true, summary = "Create a new program", description = "Creates a new program.")
    @PostMapping("")
    public ResponseEntity<?> createProgram() {
        return ResponseEntity.ok("Program created");
    }

    @Operation(deprecated = true, summary = "Update a program", description = "Updates an existing program.")
    @PutMapping("/{programId}")
    public ResponseEntity<?> updateProgram(@PathVariable String programId) {
        return ResponseEntity.ok("Program updated " + programId);
    }

    @Operation(deprecated = true, summary = "Delete a program", description = "Deletes an existing program.")
    @DeleteMapping("/{programId}")
    public ResponseEntity<?> deleteProgram(@PathVariable String programId) {
        return ResponseEntity.ok("Program deleted " + programId);
    }

    @Operation(deprecated = true, summary = "Get program full details", description = "Returns full details of a specific program.")
    @GetMapping("/{programId}/details")
    public ResponseEntity<?> getProgramFullDetails(@PathVariable String programId) {
        return ResponseEntity.ok("Full details for program " + programId);
    }

    @Operation(deprecated = true, summary = "Get all program statuses", description = "Returns a list of all program statuses.")
    @GetMapping("/details")
    public ResponseEntity<?> getAllProgramStatuses() {
        return ResponseEntity.ok("All program statuses and list");
    }

    @Operation(deprecated = true, summary = "Get program participants", description = "Returns a list of participants for a specific program.")
    @GetMapping("/{programId}/participants")
    public ResponseEntity<?> getProgramParticipants(@PathVariable String programId) {
        return ResponseEntity.ok("List of participants for program " + programId);
    }

    // Students only
    @Operation(deprecated = true, summary = "Get enrolled programs for a student", description = "Returns a list of enrolled programs for a specific student.")
    @GetMapping("/students/{studentId}/programs/enrolled")
    public ResponseEntity<?> getStudentEnrolledPrograms(@PathVariable String studentId) {
        return ResponseEntity.ok("Enrolled programs for student " + studentId);
    }

    // Very low priority
    @Operation(deprecated = true, summary = "Approve cancellation request", description = "Approves a cancellation request for a specific program.")
    @PutMapping("/{programId}/approve-cancel/{studentId}")
    public ResponseEntity<?> approveCancelRequest(@PathVariable String programId, @PathVariable String studentId) {
        return ResponseEntity.ok("Cancellation request processed for student " + studentId);
    }

    @Operation(deprecated = true, summary = "Send program announcement", description = "Sends an announcement for a specific program.")
    @PostMapping("/{programId}/announcement")
    public ResponseEntity<?> sendProgramAnnouncement(@PathVariable String programId) {
        return ResponseEntity.ok("Announcement sent for program " + programId);
    }
}