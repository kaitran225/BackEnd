package com.healthy.backend.controller;

import com.healthy.backend.dto.appointment.*;
import com.healthy.backend.dto.psychologist.DepartmentResponse;
import com.healthy.backend.exception.OperationFailedException;
import com.healthy.backend.service.AppointmentService;
import com.healthy.backend.service.NotificationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;


@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/appointments")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Appointments Controller", description = "Appointments related APIs.")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final NotificationService notificationService;

    @Operation(
            summary = "Get all appointments",
            description = "Returns a list of all appointments."
    )
    @GetMapping("/")
    public ResponseEntity<List<AppointmentResponse>> getAllPsychologist() {
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
    @GetMapping("/{appointmentId}")
    public ResponseEntity<AppointmentResponse> getAppointmentById(@PathVariable String appointmentId) {
        AppointmentResponse appointmentResponse = appointmentService.getAppointmentById(appointmentId);
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

    @Operation(
            summary = "Request cancel of an appointment",
            description = "Requests an cancel update of an appointment."
    )
    @PutMapping("/{appointmentId}/cancel/{userId}")
    public ResponseEntity<AppointmentResponse> cancelAppointment
            (@PathVariable String appointmentId,
             @PathVariable String userId) {
        AppointmentResponse response = appointmentService.cancelAppointment(appointmentId, userId);
        if (response != null) {
        

            return ResponseEntity.ok(response);
        }
        throw new OperationFailedException("Failed to cancel appointment");
    }



    @Operation(summary = "Check in to an appointment")
    @PostMapping("/{appointmentId}/check-in")
    public ResponseEntity<AppointmentResponse> checkIn(@PathVariable String appointmentId) {
        AppointmentResponse response = appointmentService.checkIn(appointmentId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Check out from an appointment")
    @PostMapping("/{appointmentId}/check-out")
    public ResponseEntity<AppointmentResponse> checkOut(
            @PathVariable String appointmentId,
            @RequestBody CheckOutRequest request) {

        AppointmentResponse response = appointmentService.checkOut(appointmentId, request.getPsychologistNote());
        
     

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Submit feedback for an appointment",
            description = "Allows a student to submit feedback and rating for a completed appointment."
    )
    @PostMapping("/{appointmentId}/feedback")
    public ResponseEntity<AppointmentResponse> submitFeedback(
            @PathVariable String appointmentId,
            @RequestBody @Valid AppointmentFeedbackRequest feedback) {
        AppointmentResponse response = appointmentService.submitFeedback(appointmentId, feedback);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Update an appointment",
            description = "Updates time slot, notes, or status of an appointment."
    )
    @PutMapping("/{appointmentId}")
    public ResponseEntity<AppointmentResponse> updateAppointment(@PathVariable String appointmentId,
                                    @RequestBody AppointmentUpdateRequest request) {

        AppointmentResponse response = appointmentService.updateAppointment(appointmentId, request);
        if (response != null) {
            return ResponseEntity.ok(response);
        }

        throw new OperationFailedException("Failed to update appointment");
    }

}

