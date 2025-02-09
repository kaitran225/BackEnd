package com.healthy.backend.controller;

import com.healthy.backend.dto.appointment.AppointmentResponse;
import com.healthy.backend.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
@RequiredArgsConstructor
@Tag(name = "Appointments", description = "Appointments related APIs.")
public class AppointmentController {
    @Autowired
    private AppointmentService appointmentService;

    @Operation(
            summary = "Get all appointments",
            description = "Returns a list of all appointments."
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/appointments")
    public ResponseEntity<?> getAllPsychologistDTO() {
        List<AppointmentResponse> appointmentResponse = appointmentService.getAllAppointmentDTO();
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
    @GetMapping("/appointments/{id}")
    public ResponseEntity<?> getAppointmentById(@PathVariable String id) {
        AppointmentResponse appointmentResponse = appointmentService.getAppointmentById(id);
        return ResponseEntity.ok(appointmentResponse);
    }
}

