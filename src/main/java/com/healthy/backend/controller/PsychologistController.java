package com.healthy.backend.controller;

import com.healthy.backend.dto.psychologist.*;
import com.healthy.backend.dto.timeslot.TimeSlotResponse;
import com.healthy.backend.exception.ResourceNotFoundException;
import com.healthy.backend.mapper.TimeSlotMapper;
import com.healthy.backend.service.AppointmentService;
import com.healthy.backend.service.PsychologistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/psychologists")
@CrossOrigin
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Psychologist Controller", description = "Psychologist related APIs")
public class PsychologistController {

    private final AppointmentService appointmentService;
    private final PsychologistService psychologistService;
    private final TimeSlotMapper timeSlotMapper;

    @Operation(
            summary = "Get all psychologists",
            description = "Returns a list of all registered psychologists."
    )
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
            description = "Returns the psychologist with the specified ID."
    )
    @GetMapping("/{id}")
    public ResponseEntity<PsychologistResponse> getPsychologistById(@Valid @PathVariable String id) {
        PsychologistResponse psychologistResponse = psychologistService.getPsychologistById(id);
        if (psychologistResponse != null) {
            return ResponseEntity.ok(psychologistResponse);
        }
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Update psychologist details",
            description = "Updates a psychologist's details."
    )
    @PutMapping("/{id}")
    public ResponseEntity<PsychologistResponse> updatePsychologist(@PathVariable String id, @RequestBody @Valid PsychologistRequest request) {
        PsychologistResponse updatedPsychologist = psychologistService.
                updatePsychologist(id, request);
        return ResponseEntity.ok(updatedPsychologist);
    }

    @Operation(
            summary = "Get psychologist appointments",
            description = "Returns a list of appointments for a psychologist."
    )
    @GetMapping("/{id}/appointments")
    public List<String> getPsychologistAppointments(@PathVariable String id) {
        return List.of("List of appointments for psychologist " + id);
    }

    @Operation(
            deprecated = true,
            summary = "Get psychologist feedback",
            description = "Returns feedback for a psychologist."
    )
    @GetMapping("/{id}/feedback")
    public List<String> getFeedback(@PathVariable String id) {
        return List.of("Feedback for psychologist " + id);
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
            deprecated = true,
            hidden = true,
            summary = "Create default time slots",
            description = "Creates time slots for a psychologist on a given date."
    )
    @PostMapping("/{id}/timeslots")
    public ResponseEntity<List<TimeSlotResponse>> createTimeSlots( @Valid
            @PathVariable String id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if (date == null) {
            return ResponseEntity.badRequest().build();
        }
        if (!psychologistService.getTimeSlots(date, id).isEmpty()) {
            throw new RuntimeException("Time slots already exist");
        }
        List<TimeSlotResponse> timeSlots = psychologistService.createDefaultTimeSlots(date, id);
        if (!timeSlots.isEmpty()) {
            return ResponseEntity.ok(timeSlots);
        }
        ;
        throw new RuntimeException("Failed to create time slots");
    }

    @Operation(
            summary = "Get all departments",
            description = "Returns a list of all departments."
    )
    @GetMapping("/departments")
    public ResponseEntity<List<DepartmentResponse>> getDepartments() {
        List<DepartmentResponse> appointmentResponse = appointmentService.getAllDepartments();
        if (!appointmentResponse.isEmpty()) {
            return ResponseEntity.ok(appointmentResponse);
        }
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get available time slots",
            description = "Returns available time slots for a psychologist on a given date."
    )
    @GetMapping("/{id}/timeslots")
    public ResponseEntity<List<TimeSlotResponse>> getAvailableTimeSlots(
            @PathVariable String id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if (date == null) throw new ResourceNotFoundException("Date is required");
        if (psychologistService.getTimeSlots(date, id).isEmpty()) return createTimeSlots(id, date);
        List<TimeSlotResponse> response = psychologistService.getTimeSlots(date, id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Create leave request")
    @PostMapping("/{id}/leave-requests")
    public ResponseEntity<LeaveResponseDTO> createLeaveRequest(
            @RequestBody @Valid LeaveRequestDTO dto
    ) {
        LeaveResponseDTO response = psychologistService.createLeaveRequest(dto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get my leave requests")
    @GetMapping("/{id}/leave-requests")
    public ResponseEntity<List<LeaveResponseDTO>> getMyLeaveRequests(
            @PathVariable String id
    ) {
        List<LeaveResponseDTO> requests = psychologistService.getLeaveRequestsByPsychologist(id);
        return ResponseEntity.ok(requests);
    }


}