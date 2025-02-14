package com.healthy.backend.controller;

import com.healthy.backend.dto.psychologist.PsychologistRequest;
import com.healthy.backend.dto.psychologist.PsychologistResponse;
import com.healthy.backend.dto.timeslot.PsychologistAvailabilityResponse;
import com.healthy.backend.dto.timeslot.TimeSlotResponse;
import com.healthy.backend.entity.TimeSlots;
import com.healthy.backend.mapper.TimeSlotMapper;
import com.healthy.backend.service.PsychologistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    private final PsychologistService psychologistService;
    @Autowired
    TimeSlotMapper timeSlotMapper;

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
            deprecated = true,
            summary = "Get psychologist appointments",
            description = "Returns a list of appointments for a psychologist."
    )
    @GetMapping("/{id}/appointments")
    public List<String> getPsychologistAppointments(@PathVariable String id) {
        return List.of("List of appointments for psychologist " + id);
    }

    @Operation(
            deprecated = true,
            summary = "Assign a student to a psychologist",
            description = "Assigns a student to a psychologist."
    )
    @PostMapping("/{id}/assign-student/{studentId}")
    public String assignStudent(@PathVariable String id, @PathVariable String studentId) {
        return "Student " + studentId + " assigned to psychologist " + id;
    }

    @Operation(
            deprecated = true,
            summary = "Get assigned students",
            description = "Returns a list of students assigned to a psychologist."
    )
    @GetMapping("/{id}/students")
    public List<String> getAssignedStudents(@PathVariable String id) {
        return List.of("List of students assigned to psychologist " + id);
    }

    @Operation(
            deprecated = true,
            summary = "Add session notes",
            description = "Adds session notes for an appointment."
    )
    @PostMapping("/{id}/notes/{appointmentId}")
    public String addSessionNotes(@PathVariable String id, @PathVariable String appointmentId, @RequestBody String notes) {
        return "Session notes added for appointment " + appointmentId;
    }

    @Operation(
            deprecated = true,
            summary = "Get session notes",
            description = "Returns session notes for an appointment."
    )
    @GetMapping("/{id}/notes/{appointmentId}")
    public String getSessionNotes(@PathVariable String id, @PathVariable String appointmentId) {
        return "Session notes for appointment " + appointmentId;
    }

    @Operation(
            deprecated = true,
            summary = "Submit assessment report",
            description = "Submits an assessment report for an appointment."
    )
    @PostMapping("/{id}/reports/{appointmentId}")
    public String submitAssessmentReport(@PathVariable String id, @PathVariable String appointmentId, @RequestBody String report) {
        return "Assessment report submitted for appointment " + appointmentId;
    }

    @Operation(
            deprecated = true,
            summary = "Get assessment report",
            description = "Returns an assessment report for an appointment."
    )
    @GetMapping("/{id}/reports/{appointmentId}")
    public String getAssessmentReport(@PathVariable String id, @PathVariable String appointmentId) {
        return "Assessment report for appointment " + appointmentId;
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
            deprecated = true,
            summary = "Get psychologist dashboard",
            description = "Returns dashboard details for a psychologist."
    )
    @GetMapping("/{id}/dashboard")
    public String getPsychologistDashboard(@PathVariable String id) {
        return "Dashboard details for psychologist " + id;
    }

    @Operation(
            deprecated = true,
            summary = "Get psychologist specializations",
            description = "Returns a list of psychologist specializations."
    )
    @GetMapping("/specializations")
    public List<String> getPsychologistSpecializations() {
        return List.of("List of psychologist specializations");
    }

    @Operation(
            deprecated = true,
            summary = "Get psychologist availability status",
            description = "Returns availability status for psychologists."
    )
    @GetMapping("/status")
    public String getPsychologistsStatus() {
        return "Psychologists' availability status";
    }

    @Operation(

            summary = "Get psychologist time slots",
            description = "Returns time slots for a psychologist."
    )
//    @GetMapping("/{id}/timeslots")
//    public List<String> getPsychologistTimeSlots(@PathVariable String id) {
//        return List.of("Available timeslots for psychologist " + id);
//    }


    @GetMapping("/{id}/timeslots")
    public ResponseEntity<List<TimeSlotResponse>> getAvailableTimeSlots(
            @PathVariable String id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        List<TimeSlots> timeSlots = psychologistService.createDefaultTimeSlots(date, id);

        // Chuyển đổi sang DTO
        List<TimeSlotResponse> response = timeSlots.stream()
                .filter(slot -> slot.getStatus() == TimeSlots.Status.Available)
                .map(timeSlotMapper::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
}



