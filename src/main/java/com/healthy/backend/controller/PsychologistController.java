package com.healthy.backend.controller;

import com.healthy.backend.dto.timeslot.*;
import com.healthy.backend.entity.Users;
import com.healthy.backend.exception.OperationFailedException;
import com.healthy.backend.repository.StudentRepository;
import com.healthy.backend.security.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.format.annotation.DateTimeFormat;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import com.healthy.backend.dto.psychologist.*;
import org.springframework.http.ResponseEntity;
import com.healthy.backend.mapper.TimeSlotMapper;
import org.springframework.web.bind.annotation.*;
import com.healthy.backend.service.AppointmentService;
import com.healthy.backend.service.PsychologistService;

import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/psychologists")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Psychologist Controller", description = "Psychologist related APIs")
public class PsychologistController {

    private final TimeSlotMapper timeSlotMapper;
    private final AppointmentService appointmentService;
    private final PsychologistService psychologistService;
    private final TokenService tokenService;
    private final StudentRepository studentRepository;

    @Operation(summary = "Get all psychologists")
    @GetMapping("/all")
    public ResponseEntity<List<PsychologistResponse>> getAllPsychologist(HttpServletRequest request) {
        if (!tokenService.isManager(request)) {
            throw new OperationFailedException("Unauthorized access, only Managers can view psychologists");
        }
        List<PsychologistResponse> psychologistResponse = psychologistService.getAllPsychologistDTO();
        return !psychologistResponse.isEmpty() ? ResponseEntity.ok(psychologistResponse) : ResponseEntity.noContent().build();
    }


    @Operation(summary = "Get psychologist by ID")
    @GetMapping({""})
    public ResponseEntity<PsychologistResponse> getPsychologistById(
            @RequestParam(required = false) String psychologistId,
            HttpServletRequest request) {
        Users user = tokenService.retrieveUser(request);
        String actualId = psychologistId;


        if (tokenService.isManager(request) && psychologistId == null) {
            throw new IllegalArgumentException("Psychologist ID is required for managers");
        }
        if (tokenService.isStudent(request)) { // Fix
            throw new IllegalArgumentException("Unauthorized access, Student can not view psychologists ");
        }
        if (psychologistId != null && !psychologistId.isEmpty()) {
            // Kiểm tra nếu Psychologist cố tình truyền ID khác
            PsychologistResponse current = psychologistService.getPsychologistByUserId(user.getUserId());
            if (!current.getPsychologistId().equals(psychologistId)) {
                throw new OperationFailedException("You can only view your own profile");
            }
        }
        // Tự động lấy ID từ token nếu không truyền
        actualId = psychologistService.getPsychologistByUserId(user.getUserId()).getPsychologistId();
        return ResponseEntity.ok(psychologistService.getPsychologistById(actualId));
    }


    @Operation(summary = "Update psychologist details")
    @PutMapping({"/detail"})
    public ResponseEntity<PsychologistResponse> updatePsychologist(
            @RequestParam(required = false) String psychologistId,
            @RequestBody @Valid PsychologistRequest request,
            HttpServletRequest httpRequest) {

        Users currentUser = tokenService.retrieveUser(httpRequest);

        if (tokenService.isManager(httpRequest) && psychologistId == null) {
            throw new IllegalArgumentException("Psychologist ID is required for managers");
        }

        if (tokenService.isManager(httpRequest)) {
            throw new IllegalArgumentException("Student not can update psychologist");
        }

        if (psychologistId == null) {
            psychologistId = tokenService.getRoleID(currentUser);
        } else {
            String actualId = tokenService.getRoleID(currentUser);
            if (!psychologistId.equals(actualId)) {
                throw new OperationFailedException("Unauthorized access,You can only update your own profile");
            }
        }

        return ResponseEntity.ok(psychologistService.updatePsychologist(
                psychologistId,
                request,
                currentUser.getUserId()
        ));
    }

    @Operation(
            summary = "Get all psychologists",
            description = "Returns a list of all registered psychologists filtered by specialization."
    )
    @GetMapping("/specializations")
    public ResponseEntity<List<PsychologistResponse>> getAllPsychologistByDepartment(
            @RequestParam(required = false) String department) { // Thêm parameter
        List<PsychologistResponse> psychologistResponse = psychologistService.getAllPsychologistByDepartment(department);
        if (!psychologistResponse.isEmpty()) {
            return ResponseEntity.ok(psychologistResponse);
        }
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get all departments",
            description = "Returns a list of all departments.")
    @GetMapping("/departments")
    public ResponseEntity<List<DepartmentResponse>> getDepartments() {
        List<DepartmentResponse> appointmentResponse = appointmentService.getAllDepartments();
        if (!appointmentResponse.isEmpty()) {
            return ResponseEntity.ok(appointmentResponse);
        }
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Create time slots from default templates")
    @PostMapping("/timeslots/batch")
    public ResponseEntity<List<TimeSlotResponse>> createTimeSlotsFromDefaults(
            @RequestParam(required = false) String psychologistId,
            @RequestBody @Valid TimeSlotBatchCreateRequest request,
            HttpServletRequest httpRequest) {

        Users currentUser = tokenService.retrieveUser(httpRequest);

        if (tokenService.isStudent(httpRequest)) {
            throw new IllegalArgumentException("Unauthorized access, Student can not create timeSlot ");
        }

        if (psychologistId == null) {
            psychologistId = tokenService.getRoleID(currentUser);
        }

        if (tokenService.isPsychologist(httpRequest)) {
            String actualId =  tokenService.getRoleID(currentUser);
            if (!psychologistId.equals(actualId)) {
                throw new OperationFailedException("Unauthorized to create slots for other psychologists");
            }
        }

        List<TimeSlotResponse> responses = psychologistService.createTimeSlotsFromDefaults(
                psychologistId,
                request.getSlotDate(),
                request.getDefaultSlotIds()
        );
        return ResponseEntity.ok(responses);
    }


    @Operation(summary = "Get time slots")
    @GetMapping("/timeslots")
    public ResponseEntity<List<TimeSlotResponse>> getTimeSlots(
            @RequestParam(required = false) String psychologistId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            HttpServletRequest request) {
        String studentId = null;

        if (tokenService.isStudent(request)) {
            studentId = tokenService.getRoleID(tokenService.retrieveUser(request));
        }

        List<TimeSlotResponse> slots = psychologistService.getPsychologistTimeSlots(psychologistId, date, studentId);
        return ResponseEntity.ok(slots);
    }

    @Operation(summary = "Get default time slots")
    @GetMapping("/default-time-slots")
    public ResponseEntity<List<DefaultTimeSlotResponse>> getDefaultTimeSlots() {
        List<DefaultTimeSlotResponse> slots = psychologistService.getDefaultTimeSlots();
        return ResponseEntity.ok(slots);
    }
}