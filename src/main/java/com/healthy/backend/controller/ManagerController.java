//// Thêm ManagerController
//package com.healthy.backend.controller;
//
//import com.healthy.backend.dto.psychologist.LeaveResponse;
//import com.healthy.backend.enums.Role;
//import com.healthy.backend.exception.OperationFailedException;
//import com.healthy.backend.security.TokenService;
//import com.healthy.backend.service.PsychologistService;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.security.SecurityRequirement;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/managers")
//@RequiredArgsConstructor
//@SecurityRequirement(name = "Bearer Authentication")
//@Tag(name = "Manager Controller", description = "Manager specific APIs")
//public class ManagerController {
//
//    private final PsychologistService psychologistService;
//    private  final TokenService tokenService;
//
//    @Operation(summary = "Get pending leave requests")
//    @GetMapping("/leave-requests")
//    public ResponseEntity<List<LeaveResponse>> getPendingRequests(
//            HttpServletRequest httpRequest
//    ) {
//        if (!tokenService.validateRole(httpRequest, Role.MANAGER)) {
//            throw new OperationFailedException("Only Manager can Get Leave Requests");
//        }
//        return ResponseEntity.ok(psychologistService.getPendingRequests());
//    }
//
//    @Operation(summary = "Process leave request")
//    @PutMapping("/leave-requests/{requestId}")
//    public ResponseEntity<LeaveResponse> processLeaveRequest(
//            @PathVariable String requestId,
//            @RequestParam boolean approve ,
//            HttpServletRequest httpRequest
//    ) {
//        if (!tokenService.validateRole(httpRequest, Role.MANAGER)) {
//            throw new OperationFailedException("Only Manager can process Leave Requests");
//        }
//
//        return ResponseEntity.ok(psychologistService.processLeaveRequest(requestId, approve));
//    }
//
//    @Operation(summary = "Get all leave requests")
//    @GetMapping("/leave-requests/all")
//    public ResponseEntity<List<LeaveResponse>> getAllLeaveRequests( HttpServletRequest httpRequest) {
//        if (!tokenService.validateRole(httpRequest, Role.MANAGER)) {
//            throw new OperationFailedException("Only Manager can get All Leave Requests");
//        }
//        return ResponseEntity.ok(psychologistService.getAllLeaveRequests());
//    }
//}