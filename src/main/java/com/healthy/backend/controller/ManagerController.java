// Thêm ManagerController
package com.healthy.backend.controller;



import com.healthy.backend.dto.manager.AppointmentStatsResponse;
import com.healthy.backend.dto.manager.PsychologistStatsResponse;
import com.healthy.backend.service.ManagerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/managers")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Manager Controller", description = "Manager specific APIs")
public class ManagerController {
    private final ManagerService managerService;

    // Endpoint to get appointment statistics by status
    @GetMapping("/stats/appointments")
    public AppointmentStatsResponse getAppointmentStats() {
        return managerService.getAppointmentStats();
    }

    // Endpoint to get psychologist statistics (average rating and number of appointments)
    @GetMapping("/stats/psychologists")
    public List<PsychologistStatsResponse> getPsychologistStats() {
        return managerService.getPsychologistStats();
    }

}