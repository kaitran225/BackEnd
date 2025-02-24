package com.healthy.backend.controller;

import com.healthy.backend.dto.psychologist.*;
import com.healthy.backend.dto.timeslot.TimeSlotResponse;
import com.healthy.backend.dto.timeslot.TimeSlotResponseWrapper;
import com.healthy.backend.entity.OnLeaveRequest;
import com.healthy.backend.enums.OnLeaveStatus;
import com.healthy.backend.exception.ResourceNotFoundException;
import com.healthy.backend.mapper.TimeSlotMapper;
import com.healthy.backend.repository.LeaveRequestRepository;
import com.healthy.backend.service.AppointmentService;
import com.healthy.backend.service.PsychologistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/psychologists")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Psychologist Controller", description = "Psychologist related APIs")
public class PsychologistController {

    private final TimeSlotMapper timeSlotMapper;
    private final AppointmentService appointmentService;
    private final PsychologistService psychologistService;
    private final LeaveRequestRepository leaveRequestRepository;

    @Operation(
            summary = "Get all psychologists",
            description = "Returns a list of all registered psychologists." )
    @GetMapping()
    public ResponseEntity<List<PsychologistResponse>> getAllPsychologist() {
        List<PsychologistResponse> psychologistResponse = psychologistService.getAllPsychologistDTO();
        if (!psychologistResponse.isEmpty()) {
            return ResponseEntity.ok(psychologistResponse);
        }
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get psychologist by ID",
            description = "Returns the psychologist with the specified ID." )
    @GetMapping("/{psychologistId}")
    public ResponseEntity<PsychologistResponse> getPsychologistById(
            @Valid @PathVariable String psychologistId ) {
        PsychologistResponse psychologistResponse = psychologistService.getPsychologistById(psychologistId);
        if (psychologistResponse != null) {
            return ResponseEntity.ok(psychologistResponse);
        }
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Update psychologist details",
            description = "Updates a psychologist's details." )
    @PutMapping("/{psychologistId}")
    public ResponseEntity<PsychologistResponse> updatePsychologist(
            @PathVariable String psychologistId,
            @RequestBody @Valid PsychologistRequest request) {
        PsychologistResponse updatedPsychologist = psychologistService.
                updatePsychologist(psychologistId, request);
        return ResponseEntity.ok(updatedPsychologist);
    }


    @Operation(
            deprecated = true,
            summary = "Get psychologist feedback",
            description = "Returns feedback for a psychologist." )
    @GetMapping("/{psychologistId}/feedback")
    public List<String> getFeedback(@PathVariable String psychologistId) {
        return List.of("Feedback for psychologist " + psychologistId);
    }

    @Operation(
            summary = "Get all psychologists",
            description = "Returns a list of all registered psychologists filtered by specialization."
    )
    @GetMapping("/specializations")
    public ResponseEntity<List<PsychologistResponse>> getAllPsychologistByDepartment(
            @RequestParam(required = false) String department) { // Thêm parameter
        List<PsychologistResponse> psychologistResponse = psychologistService.getAllPsychologistByDepartment(department);
        if (!psychologistResponse.isEmpty()) {
            return ResponseEntity.ok(psychologistResponse);
        }
        return ResponseEntity.noContent().build();
    }

    @Operation(
            hidden = true,
            summary = "Create default time slots",
            description = "Creates time slots for a psychologist on a given date." )
    @PostMapping("/{psychologistId}/timeslots")
    public ResponseEntity<List<TimeSlotResponse>> createTimeSlots(
            @Valid @PathVariable String psychologistId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if (date == null) {
            return ResponseEntity.badRequest().build();
        }
        if (!psychologistService.getTimeSlots(date, psychologistId).isEmpty()) {
            throw new RuntimeException("Time slots already exist");
        }
        List<TimeSlotResponse> timeSlots = psychologistService.createDefaultTimeSlots(date, psychologistId);
        if (!timeSlots.isEmpty()) {
            return ResponseEntity.ok(timeSlots);
        }
        throw new RuntimeException("Failed to create time slots");
    }

    @Operation(
            summary = "Get all departments",
            description = "Returns a list of all departments." )
    @GetMapping("/departments")
    public ResponseEntity<List<DepartmentResponse>> getDepartments() {
        List<DepartmentResponse> appointmentResponse = appointmentService.getAllDepartments();
        if (!appointmentResponse.isEmpty()) {
            return ResponseEntity.ok(appointmentResponse);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{psychologistId}/timeslots")
    public ResponseEntity<TimeSlotResponseWrapper> getAvailableTimeSlots(
            @PathVariable String psychologistId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        if (date == null) {
            throw new ResourceNotFoundException("Date is required");
        }

        // Check if the psychologist is on leave
        List<OnLeaveRequest> leaves = leaveRequestRepository.findByPsychologistPsychologistIDAndStatusAndDateRange(
                psychologistId, OnLeaveStatus.APPROVED, date);

        if (!leaves.isEmpty()) {
            // Return an empty list with a message when the psychologist is on leave
            TimeSlotResponseWrapper response = new TimeSlotResponseWrapper(
                    Collections.emptyList(),
                    "The psychologist is on leave. Please choose another psychologist "
            );
            return ResponseEntity.ok(response);
        }

        // If not on leave, proceed with fetching or creating time slots
        List<TimeSlotResponse> timeSlots = psychologistService.getTimeSlots(date, psychologistId);
        if (timeSlots.isEmpty()) {
            timeSlots = psychologistService.createDefaultTimeSlots(date, psychologistId);
        }

        // Return time slots with no message
        TimeSlotResponseWrapper response = new TimeSlotResponseWrapper(timeSlots, null);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Create leave request",
            description = "Creates a new leave request for a psychologist." )
    @PostMapping("/{psychologistId}/leave-requests")
    public ResponseEntity<LeaveResponse> createLeaveRequest(
            @RequestBody @Valid LeaveRequest dto) {
        LeaveResponse response = psychologistService.createLeaveRequest(dto);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get leave-requests",
            description = "Returns a list of leave requests for a psychologist." )
    @GetMapping("/{psychologistId}/leave-requests")
    public ResponseEntity<List<LeaveResponse>> getMyLeaveRequests(
            @PathVariable String psychologistId) {
        List<LeaveResponse> requests = psychologistService.getLeaveRequestsByPsychologist(psychologistId);
        return ResponseEntity.ok(requests);
    }

    @Operation(
            summary = "Cancel leave request",
            description = "Requests a cancel update for a psychologist." )
    @PutMapping("/{psychologistId}/leave-requests/{onLeaveId}/cancel")
    public ResponseEntity<LeaveResponse> cancelLeave(
            @PathVariable String psychologistId,
            @PathVariable String onLeaveId) {
        return ResponseEntity.ok(psychologistService.cancelLeave(psychologistId, onLeaveId));
    }

    @Operation(
            summary = "Request return",
            description = "Requests a return for a psychologist." )
    @PutMapping("/{psychologistId}/leave-requests/{onLeaveId}/return")
    public ResponseEntity<PsychologistResponse> onReturn(
            @PathVariable String psychologistId,
            @PathVariable String onLeaveId) {
        return ResponseEntity.ok(psychologistService.onReturn(psychologistId, onLeaveId));
    }
}