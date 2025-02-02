package com.healthy.BackEnd.Controller;
import java.util.List;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.eclipse.angus.mail.iap.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.healthy.BackEnd.DTO.AppointmentDTO;
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
        List<AppointmentDTO> appointmentDTO = appointmentService.getAllAppointmentDTO();
        if(!appointmentDTO.isEmpty()) {
            return ResponseEntity.ok(appointmentDTO);
        }
            return ResponseEntity.noContent().build();

    }

    @SecurityRequirement(name="Bearer Authentication")
    @GetMapping("/appointments/{id}")
    public ResponseEntity<?> getAppointmentById(@PathVariable String id) {
        AppointmentDTO appointmentDTO = appointmentService.getAppointmentById(id);
        return ResponseEntity.ok(appointmentDTO);
    }

}

