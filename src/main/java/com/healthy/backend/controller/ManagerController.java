package com.healthy.backend.controller;

import com.healthy.backend.dto.manager.*;
import com.healthy.backend.enums.Role;
import com.healthy.backend.repository.PsychologistKPIRepository;
import com.healthy.backend.security.TokenService;
import com.healthy.backend.service.ManagerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/managers")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Manager Controller", description = "Manager specific APIs")
public class ManagerController {
    private final ManagerService managerService;
    private final TokenService tokenService;
    private final PsychologistKPIRepository kpiRepository;

    // Endpoint to get appointment statistics by status
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @Operation(
            summary = "Get appointment statistics by status",
            description = ""
    )
    @GetMapping("/stats/appointments")
    public AppointmentStatsResponse getAppointmentStats(
            HttpServletRequest httpRequest
    ) {
        if (!tokenService.validateRole(httpRequest, Role.MANAGER)) {
            throw new IllegalArgumentException("Unauthorized access get Appointments ");
        }

        return managerService.getAppointmentStats();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/stats/psychologists")
    public List<PsychologistStatsResponse> getPsychologistStats(
            HttpServletRequest httpRequest
    ) {
        if (!tokenService.validateRole(httpRequest, Role.MANAGER)) {
            throw new IllegalArgumentException("Unauthorized access get Appointments ");
        }
        return managerService.getPsychologistStats();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping("/kpi")
    public ResponseEntity<KpiResponse> setKpi(
            @RequestParam String psychologistId,
            @RequestParam int month,
            @RequestParam int year,
            @RequestParam int targetSlots,
            HttpServletRequest httpServlet) {
        if (!tokenService.validateRole(httpServlet, Role.MANAGER)) {
            throw new IllegalArgumentException("Unauthorized access");
        }

        managerService.setKpiForPsychologist(psychologistId, month, year, targetSlots);

        KpiResponse response = new KpiResponse(
                psychologistId,
                month,
                year,
                targetSlots,
                "KPI set successfully for psychologist " + psychologistId
        );

        return ResponseEntity.ok(response);
    }


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping("/notification-schedule")
    public ResponseEntity<NotificationScheduleResponse> setNotificationSchedule(
            @RequestParam String notificationTime,
            @RequestParam DayOfWeek notificationDay,
            HttpServletRequest httpRequest) {
        if (!tokenService.validateRole(httpRequest, Role.MANAGER)) {
            throw new IllegalArgumentException("Unauthorized access");
        }

        LocalTime time = LocalTime.parse(notificationTime);
        managerService.setNotificationSchedule(time, notificationDay);

        NotificationScheduleResponse response = new NotificationScheduleResponse(
                time,
                notificationDay,
                "Notification schedule set successfully"
        );

        return ResponseEntity.ok(response);
    }


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/dashboard")
    public ResponseEntity<ManagerDashboardResponse> getManagerDashboard(
            @RequestParam(required = false) String filter, // week/month/year
            @RequestParam(required = false) Integer value, // week number/month number/year
            HttpServletRequest httpRequest
    ) {
        if (!tokenService.validateRole(httpRequest, Role.MANAGER)) {
            throw new IllegalArgumentException("Unauthorized access");
        }

        return ResponseEntity.ok(managerService.getDashboardStats(filter, value));
    }

}