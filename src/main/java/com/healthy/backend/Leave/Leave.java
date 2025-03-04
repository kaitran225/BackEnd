
/// ////////////////////////Service///////////////////////////////////////////////////
// Update psychologist status based on leave requests
//    private void updatePsychologistStatusBasedOnLeaveRequests(Psychologists psychologist) {
//        LocalDate today = LocalDate.now();
//
//        List<OnLeaveRequest> approvedLeaves = leaveRequestRepository
//                .findByPsychologistPsychologistIDAndStatus(
//                        psychologist.getPsychologistID(),
//                        OnLeaveStatus.APPROVED
//                );
//
//        boolean isOnLeave = approvedLeaves.stream()
//                .anyMatch(leave ->
//                        !today.isBefore(leave.getStartDate())
//                                && !today.isAfter(leave.getEndDate()));
//
//        if (isOnLeave) {
//            psychologist.setStatus(PsychologistStatus.ON_LEAVE);
//        } else {
//            psychologist.setStatus(PsychologistStatus.ACTIVE);
//        }
//
//        psychologistRepository.save(psychologist);
//    }


//    public LeaveResponse createLeaveRequest(LeaveRequest request) {
//        // Validate date range
//        if (request.getStartDate().isAfter(request.getEndDate())) {
//            throw new IllegalArgumentException("Start date must be before end date");
//        }
//
//        // Check if the leave duration exceeds 7 days
//        long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate());
//        if (daysBetween > 7) {
//            throw new IllegalArgumentException("Leave duration cannot exceed 7 days");
//        }
//
//        // Check psychologist exists and is active
//        Psychologists psychologist = psychologistRepository.findById(request.getPsychologistId())
//                .orElseThrow(() -> new ResourceNotFoundException("Psychologist not found"));
//
//        if (psychologist.getStatus() != PsychologistStatus.ACTIVE) {
//            throw new IllegalStateException("Psychologist must be active to request leave");
//        }
//
//        // Check for overlapping approved leaves
//        List<OnLeaveRequest> overlappingLeaves = leaveRequestRepository
//                .findByPsychologistAndStatusAndDateRange(
//                        psychologist,
//                        request.getStartDate(),
//                        request.getEndDate()
//                );
//
//        if (!overlappingLeaves.isEmpty()) {
//            throw new IllegalStateException("Existing approved/pending leave in this period");
//        }
//
//        // Create and save request
//       // Tạo và lưu yêu cầu nghỉ phép
//    OnLeaveRequest onRequest = psychologistsMapper.createPendingOnLeaveRequestEntity(request,
//    __.generateLeaveRequestID(), psychologist);
//
//// Kiểm tra nếu yêu cầu đã hết hạn
//        if (LocalDate.now().isAfter(request.getStartDate())) {
//                onRequest.setStatus(OnLeaveStatus.EXPIRED);
//            }
//
//        OnLeaveRequest saved = leaveRequestRepository.save(onRequest);
//
//// Cập nhật trạng thái của nhà tâm lý học
//        updatePsychologistStatusBasedOnLeaveRequests(psychologist);
//
//// Gửi thông báo
//        // Notify psychologist
//        notificationService.createOnLeaveNotification(
//                psychologist.getUserID(),
//                "Leave Request Created",
//                "Your leave request has been created.",
//                saved.getLeaveRequestID()
//        );
//
//        // Notify all managers
//        List<Users> managers = userRepository.findByRole(Role.MANAGER);
//        if (!managers.isEmpty()) {
//            Users psychUser = userRepository.findById(psychologist.getUserID())
//                    .orElseThrow(() -> new ResourceNotFoundException("User not found for psychologist"));
//
//            String message = String.format(
//                    "Psychologist %s has requested leave from %s to %s. Please review.",
//                    psychUser.getFullName(),
//                    saved.getStartDate(),
//                    saved.getEndDate()
//            );
//
//            for (Users manager : managers) {
//                notificationService.createOnLeaveNotification(
//                        manager.getUserId(),
//                        "New Leave Request for Approval",
//                        message,
//                        saved.getLeaveRequestID()
//                );
//            }
//        }
//
//        return psychologistsMapper.buildLeaveResponse(saved);
//}

//    public List<LeaveResponse> getPendingRequests() {
//        return leaveRequestRepository.findByStatus(OnLeaveStatus.PENDING)
//                .stream()
//                .map(psychologistsMapper::buildLeaveResponse)
//                .collect(Collectors.toList());
//    }
//
//    public List<LeaveResponse> getAllLeaveRequests() {
//        return leaveRequestRepository.findAll()
//                .stream()
//                .map(psychologistsMapper::buildLeaveResponse)
//                .collect(Collectors.toList());
//    }

//    public LeaveResponse processLeaveRequest(String requestId, boolean approve) {
//        OnLeaveRequest request = leaveRequestRepository.findById(requestId)
//                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found"));
//
//        if (approve) {
//            request.setStatus(OnLeaveStatus.APPROVED);
//            Psychologists psychologist = request.getPsychologist();
//            psychologist.setStatus(PsychologistStatus.ON_LEAVE);
//            psychologistRepository.save(psychologist);
//        } else {
//            request.setStatus(OnLeaveStatus.REJECTED);
//        }
//
//        OnLeaveRequest updated = leaveRequestRepository.save(request);
//
//        // Notify psychologist
//        notificationService.createOnLeaveNotification(
//            request.getPsychologist().getUserID(),
//            "Leave Request Processed",
//            "Your leave request has been " + (approve ? "approved" : "rejected") + ".",
//            requestId
//        );
//
//        return psychologistsMapper.buildLeaveResponse(updated);
//    }

//    public List<LeaveResponse> getLeaveRequestsByPsychologist(String psychologistId) {
//        List<OnLeaveRequest> requests = leaveRequestRepository.findByPsychologistPsychologistID(psychologistId);
//        return requests.stream()
//                .map(psychologistsMapper::buildLeaveResponse)
//                .collect(Collectors.toList());
//    }

//    public void updatePsychologistStatusBasedOnLeaveRequests() {
//        LocalDate today = LocalDate.now();
//
//        List<Psychologists> psychologists = psychologistRepository.findAll();
//
//        for (Psychologists psychologist : psychologists) {
//            List<OnLeaveRequest> approvedLeaves = leaveRequestRepository
//                    .findByPsychologistPsychologistIDAndStatus(
//                            psychologist.getPsychologistID(),
//                            OnLeaveStatus.APPROVED
//                    );
//
//            boolean isOnLeave = approvedLeaves.stream()
//                    .anyMatch(leave ->
//                            !today.isBefore(leave.getStartDate())
//                                    && !today.isAfter(leave.getEndDate()));
//
//            if (isOnLeave) {
//                psychologist.setStatus(PsychologistStatus.ON_LEAVE);
//            } else {
//                psychologist.setStatus(PsychologistStatus.ACTIVE);
//            }
//
//            psychologistRepository.save(psychologist);
//        }
//    }


//    @EventListener(ApplicationReadyEvent.class)
//    public void updatePsychologistStatusOnStartup() {
//
//        this.updatePsychologistStatusBasedOnLeaveRequests();
//    }

//    public LeaveResponse cancelLeave(String psychologistId, String leaveId) {
//        Psychologists psychologist = psychologistRepository.findById(psychologistId)
//                .orElseThrow(() -> new ResourceNotFoundException("Psychologist not found"));
//
//        OnLeaveRequest leaveRequest = leaveRequestRepository.findById(leaveId)
//                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found"));
//
//        if (leaveRequest.getStatus() != OnLeaveStatus.PENDING) {
//            throw new IllegalStateException("Leave request is not pending");
//        }
//
//        leaveRequest.setStatus(OnLeaveStatus.CANCELLED);
//        leaveRequestRepository.save(leaveRequest);
//
//        psychologist.setStatus(PsychologistStatus.ACTIVE);
//        psychologistRepository.save(psychologist);
//
//        notificationService.createOnLeaveNotification(
//            psychologist.getUserID(),
//            "Leave Request Cancelled",
//            "Your leave request has been cancelled.",
//            leaveId
//        );
//
//        return psychologistsMapper.buildLeaveResponse(leaveRequest);
//    }

//    public PsychologistResponse onReturn(String psychologistId, String leaveId) {
//        Psychologists psychologist = psychologistRepository.findById(psychologistId)
//                .orElseThrow(() -> new ResourceNotFoundException("Psychologist not found"));
//
//        OnLeaveRequest leaveRequest = leaveRequestRepository.findById(leaveId)
//                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found"));
//
//        if (leaveRequest.getStatus() != OnLeaveStatus.APPROVED) {
//            throw new IllegalStateException("Leave request is not approved");
//        }
//
//        leaveRequest.setStatus(OnLeaveStatus.REJECTED);
//        leaveRequestRepository.save(leaveRequest);
//
//        psychologist.setStatus(PsychologistStatus.ACTIVE);
//        psychologistRepository.save(psychologist);
//        return psychologistsMapper.buildPsychologistResponse(psychologist);
//    }
//    public List<LeaveResponse> getApprovedLeaveRequestsByPsychologist(String psychologistId) {
//        List<OnLeaveRequest> requests = leaveRequestRepository.findByPsychologistPsychologistIDAndStatus(
//                psychologistId, OnLeaveStatus.APPROVED);
//        return requests.stream()
//                .map(psychologistsMapper::buildLeaveResponse)
//                .collect(Collectors.toList());
//    }
/// //////////////////////////////////Repository ////////////////////
//
//import com.healthy.backend.entity.OnLeaveRequest;
//import com.healthy.backend.entity.Psychologists;
//import com.healthy.backend.enums.OnLeaveStatus;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//
//public interface LeaveRequestRepository extends JpaRepository<OnLeaveRequest, String> {
//    List<OnLeaveRequest> findByStatus(OnLeaveStatus status);
//    List<OnLeaveRequest> findByPsychologistPsychologistID(String psychologistId);
//
//    @Query("SELECT lr FROM OnLeaveRequest lr " +
//            "WHERE lr.psychologist.psychologistID = :psychologistId " +
//            "AND lr.status = :status " +
//            "AND lr.startDate <= :date " +
//            "AND lr.endDate >= :date")
//    List<OnLeaveRequest> findByPsychologistPsychologistIDAndStatusAndDateRange(
//            @Param("psychologistId") String psychologistId,
//            @Param("status") OnLeaveStatus status,
//            @Param("date") LocalDate date
//    );
//
//    @Query("SELECT l FROM OnLeaveRequest l WHERE " +
//            "l.psychologist = :psychologist AND " +
//            "l.status IN ('APPROVED', 'PENDING') AND " +
//            "(l.startDate <= :endDate AND l.endDate >= :startDate)")
//    List<OnLeaveRequest> findByPsychologistAndStatusAndDateRange(
//            @Param("psychologist") Psychologists psychologist,
//            @Param("startDate") LocalDate startDate,
//            @Param("endDate") LocalDate endDate
//    );
//
//    List<OnLeaveRequest> findByPsychologistPsychologistIDAndStatus(
//            @Param("psychologistId") String psychologistId,
//            @Param("status") OnLeaveStatus status
//    );
//
//    @Query("SELECT l.leaveRequestID FROM OnLeaveRequest l ORDER BY l.leaveRequestID DESC LIMIT 1")
//    String findLastLeaveRequestId();
//
//}
/// ////////////////////ENTITY////////////////////////
//package com.healthy.backend.entity;
//
//import com.healthy.backend.enums.OnLeaveStatus;
//import jakarta.persistence.*;
//import lombok.*;
//import org.hibernate.annotations.CreationTimestamp;
//
//import java.time.LocalDate;
//import java.util.Date;
//
//@Entity
//@Getter
//@Setter
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//@Table(name = "OnLeaveRequest")
//public class OnLeaveRequest {
//    @Id
//    @Column(name = "LeaveRequestID", length = 36)
//    private String leaveRequestID;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "PsychologistID", nullable = false)
//    private Psychologists psychologist;
//
//    @Column(nullable = false)
//    private LocalDate startDate;
//
//    @Column(nullable = false)
//    private LocalDate endDate;
//
//    @Column(nullable = false)
//    private String reason;
//
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    @Builder.Default
//    private OnLeaveStatus status = OnLeaveStatus.PENDING;
//
//    @CreationTimestamp
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date createdAt;
//}
/// ////////////////////////DTO///////////////////////
//
//import com.fasterxml.jackson.annotation.JsonInclude;
//import io.swagger.v3.oas.annotations.media.Schema;
//import jakarta.validation.constraints.FutureOrPresent;
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.NotNull;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDate;
//
//@Data
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
//public class LeaveRequest {
//
//    @Schema(hidden = true)
//    private String psychologistId;
//
//    @Schema(example = "2025-03-01")
//    @NotNull(message = "Start date is required")
//    @FutureOrPresent(message = "Start date must be in present or future")
//    private LocalDate startDate;
//
//    @Schema(example = "2025-12-31")
//    @NotNull(message = "End date is required")
//    @FutureOrPresent(message = "End date must be in present or future")
//    private LocalDate endDate;
//
//    @Schema(example = "Reason")
//    @NotBlank(message = "Reason is required")
//    private String reason;
//
//
//import com.fasterxml.jackson.annotation.JsonInclude;
//import io.swagger.v3.oas.annotations.media.ArraySchema;
//import io.swagger.v3.oas.annotations.media.Schema;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDate;
//import java.util.Date;
//
//@Data
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
//public class LeaveResponse {
//    @Schema(example = "OLR001")
//    private String requestId;
//    @Schema(example = "PSY001")
//    private String psychologistName;
//    @Schema(example = "2025-01-01")
//    private LocalDate startDate;
//    @Schema(example = "2025-12-31")
//    private LocalDate endDate;
//    @Schema(example = "Reason")
//    private String reason;
//    @Schema(example = "Approved")
//    private String status;
//    @Schema(example = "Psychology")
//    private String department;
//    @Schema(example = "2025-01-01")
//    private Date createdAt;
//}
/// /////////////////////controller////////

//    @Operation(
//            summary = "Create leave request",
//            description = "Creates a new leave request for a psychologist." )
//    @PostMapping("/{psychologistId}/leave-requests")
//    public ResponseEntity<LeaveResponse> createLeaveRequest(
//            @RequestParam(required = false) String psychologistId,
//            @RequestBody @Valid LeaveRequest dto,
//            HttpServletRequest request) {
//        Users currentUser = tokenService.retrieveUser(request);
//
//
//        if (tokenService.validateRole(request, Role.STUDENT) ) {
//            throw new IllegalArgumentException("Student not can create leave request");
//        }
//
//        if (psychologistId == null) {
//            psychologistId = psychologistService.getPsychologistIdByUserId(
//                    tokenService.retrieveUser(request).getUserId()
//            );
//        }
//
//        if (tokenService.validateRole(request, Role.PSYCHOLOGIST)) {
//            // Kiểm tra Psychologist chỉ được tạo slot cho chính mình
//            String actualId = psychologistService.getPsychologistIdByUserId(currentUser.getUserId());
//            if (!psychologistId.equals(actualId)) {
//                throw new OperationFailedException("Unauthorized to create leave for other psychologists");
//            }
//        }
//
//        // Gán ID vào DTO
//        dto.setPsychologistId(psychologistId);
//
//        LeaveResponse response = psychologistService.createLeaveRequest(dto);
//        return ResponseEntity.ok(response);
//    }

//    @Operation(
//            summary = "Get leave-requests",
//            description = "Returns a list of leave requests for a psychologist." )
//    @GetMapping("/{psychologistId}/leave-requests")
//    public ResponseEntity<List<LeaveResponse>> getMyLeaveRequests(
//            @RequestParam(required = false) String psychologistId,
//    HttpServletRequest request ) {
//        Users currentUser = tokenService.retrieveUser(request);
//
//
//        if (tokenService.validateRole(request, Role.STUDENT) ) {
//            throw new IllegalArgumentException("Student not can create leave request");
//        }
//
//        if (tokenService.validateRole(request, Role.MANAGER) && psychologistId == null) {
//            throw new IllegalArgumentException("Psychologist ID is required for managers");
//        }
//        if (psychologistId == null) {
//            psychologistId = psychologistService.getPsychologistIdByUserId(
//                    tokenService.retrieveUser(request).getUserId()
//            );
//        }
//        if (tokenService.validateRole(request, Role.PSYCHOLOGIST)) {
//            // Kiểm tra Psychologist chỉ được tạo slot cho chính mình
//            String actualId = psychologistService.getPsychologistIdByUserId(currentUser.getUserId());
//            if (!psychologistId.equals(actualId)) {
//                throw new OperationFailedException("Unauthorized to view leave for other psychologists");
//            }
//        }
//
//        List<LeaveResponse> requests = psychologistService.getLeaveRequestsByPsychologist(psychologistId);
//        return ResponseEntity.ok(requests);
//    }


//    @Operation(
//            summary = "Cancel leave request",
//            description = "Requests a cancel update for a psychologist." )
//    @PutMapping("/{psychologistId}/leave-requests/{onLeaveId}/cancel")
//    public ResponseEntity<LeaveResponse> cancelLeave(
//            @RequestParam(required = false) String psychologistId,
//            @RequestParam() String onLeaveId,
//            HttpServletRequest request) {
//        Users currentUser = tokenService.retrieveUser(request);
//        if (tokenService.validateRole(request, Role.STUDENT) ) {
//            throw new IllegalArgumentException("Student not can  Cancelleave request");
//        }
//        if (tokenService.validateRole(request, Role.MANAGER) && psychologistId == null) {
//            throw new IllegalArgumentException("Psychologist ID is required for managers");
//        }
//        if (psychologistId == null) {
//            psychologistId = psychologistService.getPsychologistIdByUserId(
//                    tokenService.retrieveUser(request).getUserId()
//            );
//        }
//        if (tokenService.validateRole(request, Role.PSYCHOLOGIST)) {
//            // Kiểm tra Psychologist chỉ được tạo slot cho chính mình
//            String actualId = psychologistService.getPsychologistIdByUserId(currentUser.getUserId());
//            if (!psychologistId.equals(actualId)) {
//                throw new OperationFailedException("Unauthorized to cancelLeave for other psychologists");
//            }
//        }
//
//
//        return ResponseEntity.ok(psychologistService.cancelLeave(psychologistId, onLeaveId));
//    }

//    @Operation(
//
//            summary = "Request return",
//            description = "Requests a return for a psychologist." )
//    @PutMapping("/{psychologistId}/leave-requests/{onLeaveId}/return")
//    public ResponseEntity<PsychologistResponse> onReturn(
//            @RequestParam(required = false) String psychologistId,
//            @RequestParam(required = false) String onLeaveId) {
//        return ResponseEntity.ok(psychologistService.onReturn(psychologistId, onLeaveId));
//    }

//    @Operation(
//            summary = "Get approved leave requests",
//            description = "Returns a list of approved leave requests for a psychologist.")
//    @GetMapping("/{psychologistId}/leave-requests/approved")
//    public ResponseEntity<List<LeaveResponse>> getApprovedLeaveRequests(
//            @RequestParam(required = false) String psychologistId,
//            HttpServletRequest request
//    ) {
//        Users currentUser = tokenService.retrieveUser(request);
//        if (tokenService.validateRole(request, Role.STUDENT) ) {
//            throw new IllegalArgumentException("Student not can view status Leave ");
//        }
//        if (tokenService.validateRole(request, Role.MANAGER) && psychologistId == null) {
//            throw new IllegalArgumentException("Psychologist ID is required for managers");
//        }
//        if (psychologistId == null) {
//            psychologistId = psychologistService.getPsychologistIdByUserId(
//                    tokenService.retrieveUser(request).getUserId()
//            );
//        }
//        if (tokenService.validateRole(request, Role.PSYCHOLOGIST)) {
//            String actualId = psychologistService.getPsychologistIdByUserId(currentUser.getUserId());
//            if (!psychologistId.equals(actualId)) {
//                throw new OperationFailedException("Unauthorized to GetApprovedLeaveRequest for other psychologists");
//            }
//        }
//        List<LeaveResponse> requests = psychologistService.getApprovedLeaveRequestsByPsychologist(psychologistId);
//        return ResponseEntity.ok(requests);
//    }


