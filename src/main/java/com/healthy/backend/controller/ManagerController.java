// Thêm ManagerController
package com.healthy.backend.controller;

import com.healthy.backend.dto.psychologist.LeaveResponse;
import com.healthy.backend.service.PsychologistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/managers")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Manager Controller", description = "Manager specific APIs")
public class ManagerController {

    private final PsychologistService psychologistService;

    @Operation(summary = "Get pending leave requests")
    @GetMapping("/leave-requests")
    public ResponseEntity<List<LeaveResponse>> getPendingRequests() {
        return ResponseEntity.ok(psychologistService.getPendingRequests());
    }

    @Operation(summary = "Process leave request")
    @PutMapping("/leave-requests/{requestId}")
    public ResponseEntity<LeaveResponse> processLeaveRequest(
            @PathVariable String requestId,
            @RequestParam boolean approve
    ) {
        return ResponseEntity.ok(psychologistService.processLeaveRequest(requestId, approve));
    }
}