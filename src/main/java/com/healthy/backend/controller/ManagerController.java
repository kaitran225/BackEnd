
package com.healthy.backend.controller;

import com.healthy.backend.dto.manager.AppointmentStatsResponse;
import com.healthy.backend.dto.manager.PsychologistStatsResponse;
import com.healthy.backend.security.TokenService;
import com.healthy.backend.service.ManagerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/managers")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Manager Controller", description = "Manager specific APIs")
public class ManagerController {
    private final ManagerService managerService;
    private final TokenService tokenService;

    @Operation(
            summary = "Get appointments stats",
            description = "Returns a list of appointments stats."
    )
    @GetMapping("/stats/appointments")
    public AppointmentStatsResponse getAppointmentStats(
            HttpServletRequest httpRequest
    ) {
        if (!tokenService.isManager(httpRequest)) {
            throw new IllegalArgumentException("Unauthorized access get Appointments ");
        }
        return managerService.getAppointmentStats();
    }

    @Operation(
            summary = "Get psychologist stats",
            description = "Returns a list of psychologist stats."
    )
    @GetMapping("/stats/psychologists")
    public List<PsychologistStatsResponse> getPsychologistStats(
            HttpServletRequest httpRequest
    ) {
        if (!tokenService.isManager(httpRequest)) {
            throw new IllegalArgumentException("Unauthorized access get Appointments ");
        }
        return managerService.getPsychologistStats();
    }
}