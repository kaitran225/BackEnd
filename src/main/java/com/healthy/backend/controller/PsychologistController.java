package com.healthy.backend.controller;

import com.healthy.backend.dto.appointment.AppointmentResponse;
import com.healthy.backend.dto.timeslot.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.format.annotation.DateTimeFormat;
import com.healthy.backend.repository.LeaveRequestRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import com.healthy.backend.dto.psychologist.*;
import com.healthy.backend.enums.OnLeaveStatus;
import org.springframework.http.ResponseEntity;
import com.healthy.backend.mapper.TimeSlotMapper;
import com.healthy.backend.entity.OnLeaveRequest;
import org.springframework.web.bind.annotation.*;
import com.healthy.backend.service.AppointmentService;
import com.healthy.backend.service.PsychologistService;
import com.healthy.backend.exception.ResourceNotFoundException;
import com.healthy.backend.dto.appointment.AppointmentFeedbackResponse;


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
            summary = "Get psychologist feedbacks",
            description = "Get all feedbacks for a psychologist from completed appointments"
    )
    @GetMapping("/{psychologistId}/feedbacks")
    public ResponseEntity<Page<AppointmentFeedbackResponse>> getPsychologistFeedbacks(
            @PathVariable String psychologistId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<AppointmentFeedbackResponse> feedbacks = psychologistService.getPsychologistFeedbacks(psychologistId, page, size);
        return ResponseEntity.ok(feedbacks);
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



    @Operation(summary = "Create time slots from default templates")
    @PostMapping("/{psychologistId}/timeslots/batch")
    public ResponseEntity<List<TimeSlotResponse>> createTimeSlotsFromDefaults(
            @PathVariable String psychologistId,
            @RequestBody @Valid TimeSlotBatchCreateRequest request) {

        List<TimeSlotResponse> responses = psychologistService.createTimeSlotsFromDefaults(
                psychologistId,
                request.getSlotDate(),
                request.getDefaultSlotIds()
        );

        return ResponseEntity.ok(responses);
    }


    @Operation(summary = "Lấy danh sách time slots")
    @GetMapping("/{psychologistId}/timeslots")
    public ResponseEntity<List<TimeSlotResponse>> getTimeSlots(
            @PathVariable String psychologistId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        List<TimeSlotResponse> slots = psychologistService.getPsychologistTimeSlots(psychologistId, date);
        return ResponseEntity.ok(slots);

    }

    @Operation(summary = "Get default time slots")
    @GetMapping("/default-time-slots")
    public ResponseEntity<List<DefaultTimeSlotResponse>> getDefaultTimeSlots() {
        List<DefaultTimeSlotResponse> slots = psychologistService.getDefaultTimeSlots();
        return ResponseEntity.ok(slots);
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

    @Operation(
            summary = "Get psychologist average rating",
            description = "Calculate average rating for a psychologist"
    )
    @GetMapping("/{psychologistId}/average-rating")
    public ResponseEntity<Double> getAverageRating(@PathVariable String psychologistId) {
        double averageRating = psychologistService.calculateAverageRating(psychologistId);
        return ResponseEntity.ok(averageRating);
    }

    @Operation(
            summary = "Get approved leave requests",
            description = "Returns a list of approved leave requests for a psychologist.")
    @GetMapping("/{psychologistId}/leave-requests/approved")
    public ResponseEntity<List<LeaveResponse>> getApprovedLeaveRequests(
            @PathVariable String psychologistId
    ) {
        List<LeaveResponse> requests = psychologistService.getApprovedLeaveRequestsByPsychologist(psychologistId);
        return ResponseEntity.ok(requests);
    }



}