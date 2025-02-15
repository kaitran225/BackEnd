package com.healthy.backend.controller;

import com.healthy.backend.dto.appointment.AppointmentFeedbackRequest;
import com.healthy.backend.dto.appointment.AppointmentReportRequest;
import com.healthy.backend.dto.appointment.AppointmentRequest;
import com.healthy.backend.dto.appointment.AppointmentResponse;
import com.healthy.backend.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/appointments")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Appointments Controller", description = "Appointments related APIs.")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @Operation(
            summary = "Get all appointments",
            description = "Returns a list of all appointments."
    )
    @GetMapping("/")
    public ResponseEntity<?> getAllPsychologistDTO() {
        List<AppointmentResponse> appointmentResponse = appointmentService.getAllAppointments();
        if (!appointmentResponse.isEmpty()) {
            return ResponseEntity.ok(appointmentResponse);
        }
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get appointment by ID",
            description = "Returns the appointment with the specified ID."
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/{id}")
    public ResponseEntity<?> getAppointmentById(@PathVariable String id) {
        AppointmentResponse appointmentResponse = appointmentService.getAppointmentById(id);
        return ResponseEntity.ok(appointmentResponse);
    }

    @Operation(
            summary = "Book an appointment",
            description = "Creates a new appointment."
    )
    @PostMapping("/book")
    public ResponseEntity<AppointmentResponse> bookAppointment(@RequestBody AppointmentRequest request) {
        AppointmentResponse response = appointmentService.bookAppointment(request);
        return ResponseEntity.ok(response);
    }


    @PutMapping("/{appointmentId}/cancel")
    public String cancelAppointment(@PathVariable String appointmentId) {
        return "Appointment cancelled successfully";
    }

    @Operation(
            deprecated = true,
            summary = "Request update of an appointment",
            description = "Requests an update of an appointment."
    )
    @PutMapping("/{appointmentId}/update-request")
    public String requestUpdateAppointment(@PathVariable String appointmentId) {
        return "Appointment update requested";
    }

    @Operation(summary = "Check in to an appointment")
    @PostMapping("/{appointmentId}/check-in")
    public ResponseEntity<AppointmentResponse> checkIn(@PathVariable String appointmentId) {
        AppointmentResponse response = appointmentService.checkIn(appointmentId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Check out from an appointment")
    @PostMapping("/{appointmentId}/check-out")
    public ResponseEntity<AppointmentResponse> checkOut(@PathVariable String appointmentId) {
        AppointmentResponse response = appointmentService.checkOut(appointmentId);
        return ResponseEntity.ok(response);
    }

    @Operation(
            deprecated = true,
            summary = "Give feedback on an appointment",
            description = "Gives feedback on an appointment."
    )
    @PostMapping("/{appointmentId}/feedback")
    public String giveFeedback(@PathVariable String appointmentId, @RequestBody AppointmentFeedbackRequest feedback) {
        return "Feedback submitted successfully";
    }

    @Operation(
            deprecated = true,
            summary = "Request appointment record",
            description = "Requests the record of an appointment."
    )
    @GetMapping("/{appointmentId}/record")
    public String requestRecord(@PathVariable String appointmentId) {
        return "Appointment record requested";
    }

    @Operation(
            deprecated = true,
            summary = "Get appointment result",
            description = "Returns the result of an appointment."
    )
    @GetMapping("/{appointmentId}/result")
    public String requestResult(@PathVariable String appointmentId) {
        return "Result requested";
    }

    // Manager or Psychologist Endpoints


    @Operation(
            deprecated = true,
            summary = "Update an appointment",
            description = "Updates an existing appointment."
    )
    @PutMapping("/{appointmentId}")
    public String updateAppointment(@PathVariable String appointmentId, @RequestBody AppointmentRequest request) {
        return "Appointment updated successfully";
    }

    @Operation(
            deprecated = true,
            summary = "Cancel an appointment by manager",
            description = "Cancels an appointment by manager."
    )
    @DeleteMapping("/{appointmentId}")
    public String cancelAppointmentByManager(@PathVariable String appointmentId) {
        return "Appointment cancelled by manager";
    }

    @Operation(
            deprecated = true,
            summary = "Make a report for an appointment",
            description = "Creates a report for an appointment."
    )
    @PostMapping("/{appointmentId}/report")
    public String makeReport(@PathVariable String appointmentId, @RequestBody AppointmentReportRequest report) {
        return "Report created successfully";
    }

    @Operation(
            deprecated = true,
            summary = "Get all appointments",
            description = "Returns a list of all appointments."
    )
    @GetMapping
    public List<String> getAllAppointments() {
        return List.of("List of all appointments");
    }

    @Operation(
            deprecated = true,
            summary = "Get appointment details",
            description = "Returns detailed information about an appointment."
    )
    @GetMapping("/{appointmentId}/details")
    public String getAppointmentDetails(@PathVariable String appointmentId) {
        return "Detailed appointment information";
    }

    @Operation(
            deprecated = true,
            summary = "Get available slots",
            description = "Returns available slots for a specific date."
    )
    @GetMapping("/available-slots")
    public String getAvailableSlots(@RequestParam String date) {
        return "Available slots for " + date;
    }

    @Operation(
            deprecated = true,
            summary = "Get virtual meeting link",
            description = "Returns the virtual meeting link for an appointment."
    )
    @GetMapping("/{appointmentId}/meeting-link")
    public String getMeetingLink(@PathVariable String appointmentId) {
        return "Virtual meeting link for appointment";
    }
}

