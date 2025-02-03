package com.healthy.BackEnd.Controller;
import java.util.List;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.healthy.BackEnd.DTO.Appointment.AppointmentResponse;
import com.healthy.BackEnd.Service.AppointmentService;

@RestController
@RequestMapping("/api")
@CrossOrigin
@RequiredArgsConstructor
public class AppointmentController {
    @Autowired
    private AppointmentService appointmentService;

    @SecurityRequirement(name="Bearer Authentication")
    @GetMapping("/appointments")
    public ResponseEntity<?> getAllPsychologistDTO() {
        List<AppointmentResponse> appointmentResponse = appointmentService.getAllAppointmentDTO();
        if(!appointmentResponse.isEmpty()) {
            return ResponseEntity.ok(appointmentResponse);
        }
            return ResponseEntity.noContent().build();
    }

    @SecurityRequirement(name="Bearer Authentication")
    @GetMapping("/appointments/{id}")
    public ResponseEntity<?> getAppointmentById(@PathVariable String id) {
        AppointmentResponse appointmentResponse = appointmentService.getAppointmentById(id);
        return ResponseEntity.ok(appointmentResponse);
    }
}

