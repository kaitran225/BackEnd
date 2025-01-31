package com.healthy.BackEnd.Controller;
import java.util.List;

import org.eclipse.angus.mail.iap.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.healthy.BackEnd.dto.AppointmentDTO;
import com.healthy.BackEnd.Service.AppointmentService;



@RestController
@RequestMapping("api/appointment")


public class AppointmentController {
    @Autowired
    AppointmentService appointmentService;

    @GetMapping
    public ResponseEntity<?> getAllPsychologistDTO() {
        List<AppointmentDTO> appointmentDTO = appointmentService.getAllAppointmentDTO();
        if(!appointmentDTO.isEmpty()) {
            return ResponseEntity.ok(appointmentDTO);
        }
            return ResponseEntity.noContent().build();

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAppointmentById(@PathVariable String id) {
        AppointmentDTO appointmentDTO = appointmentService.getAppointmentById(id);
        return ResponseEntity.ok(appointmentDTO);
    }

}

