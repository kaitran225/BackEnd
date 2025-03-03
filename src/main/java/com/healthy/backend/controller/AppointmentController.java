package com.healthy.backend.controller;

import com.healthy.backend.dto.appointment.*;
import com.healthy.backend.entity.Appointments;
import com.healthy.backend.entity.Psychologists;
import com.healthy.backend.entity.Users;
import com.healthy.backend.enums.Role;
import com.healthy.backend.exception.OperationFailedException;
import com.healthy.backend.exception.ResourceNotFoundException;
import com.healthy.backend.repository.AppointmentRepository;
import com.healthy.backend.repository.PsychologistRepository;
import com.healthy.backend.repository.UserRepository;
import com.healthy.backend.security.TokenService;
import com.healthy.backend.service.AppointmentService;
import com.healthy.backend.service.NotificationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/appointments")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Appointments Controller", description = "Appointments related APIs.")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final NotificationService notificationService;
    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final PsychologistRepository psychologistRepository;
    private final AppointmentRepository appointmentRepository;

    @Operation(summary = "Book an appointment")
    @PostMapping("/book")
    public ResponseEntity<AppointmentResponse> bookAppointment(

            @RequestParam(required = false)  String UserId,
            @RequestBody AppointmentRequest request,
            HttpServletRequest httpRequest) {

        Users currentUser = tokenService.retrieveUser(httpRequest);
        if (tokenService.validateRole(httpRequest, Role.PSYCHOLOGIST)) {
            throw new OperationFailedException("Only students can book appointments");
        }

        if (UserId == null) {
            UserId = userRepository.findById(
                    tokenService.retrieveUser(httpRequest).getUserId()
            ).get().getUserId();
        }

        if (tokenService.validateRole(httpRequest, Role.STUDENT)) {
            String actualId = userRepository.findById(currentUser.getUserId()).get().getUserId();
            if (!UserId.equals(actualId)) {
                throw new OperationFailedException("Unauthorized to book appointment for orther student");
            }
        }
        request.setUserId(UserId);
        AppointmentResponse response = appointmentService.bookAppointment(request);
        return ResponseEntity.ok(response);
    }



    // Hủy lịch hẹn
    @Operation(summary = "Request cancel of an appointment")
    @PutMapping("/{appointmentId}/cancel")
    public ResponseEntity<AppointmentResponse> cancelAppointment(
            @PathVariable String appointmentId,
            HttpServletRequest request) {

        Users currentUser = tokenService.retrieveUser(request);

        AppointmentResponse response = appointmentService.cancelAppointment(
                appointmentId,
                currentUser.getUserId()
        );
        return ResponseEntity.ok(response);
    }



    // Check-in - chỉ Psychologist
    @Operation(summary = "Check in to an appointment")
    @PostMapping("/{appointmentId}/check-in")
    public ResponseEntity<AppointmentResponse> checkIn(
            @PathVariable String appointmentId,
            HttpServletRequest httpRequest) {

        Appointments appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment with ID " + appointmentId + " not found"));

        Users currentUser = tokenService.retrieveUser(httpRequest);

        if (tokenService.validateRole(httpRequest, Role.STUDENT)) {
            throw new OperationFailedException("Only psychologists can check in");
        }

        if (currentUser.getRole() == Role.PSYCHOLOGIST && !appointment.getPsychologist().getUserID().equals(currentUser.getUserId())) {
            throw new OperationFailedException("Unauthorized to access this appointment");
        }

        Psychologists psychologist = psychologistRepository.findByUserID(currentUser.getUserId());


        AppointmentResponse response = appointmentService.checkIn(appointmentId,psychologist.getPsychologistID());
        return ResponseEntity.ok(response);
    }



    @Operation(
            summary = "Get all appointments",
            description = "Returns a list of all appointments."
    )
    @GetMapping("/")
    public ResponseEntity<List<AppointmentResponse>>  getAllPsychologist(
            HttpServletRequest httpRequest
    ) {
        if (!tokenService.validateRole(httpRequest, Role.MANAGER)) {
            throw new OperationFailedException("Only MANAGER can view All appointments");
        }

        List<AppointmentResponse> appointmentResponse = appointmentService.getAllAppointments();

        if (!appointmentResponse.isEmpty()) {
            return ResponseEntity.ok(appointmentResponse);
        }
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get appointment by ID",
            description = "Returns the appointment with the specified ID."
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/{appointmentId}")
    public ResponseEntity<AppointmentResponse> getAppointmentById(
            @PathVariable String appointmentId,
            HttpServletRequest httpRequest
    ) {
        Users currentUser = tokenService.retrieveUser(httpRequest);

        Appointments appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment with ID " + appointmentId + " not found"));


        if (currentUser.getRole() == Role.STUDENT && !appointment.getStudent().getUserID().equals(currentUser.getUserId())) {
            throw new OperationFailedException("Unauthorized to access this appointment");
        }

        if (currentUser.getRole() == Role.PSYCHOLOGIST && !appointment.getPsychologist().getUserID().equals(currentUser.getUserId())) {
            throw new OperationFailedException("Unauthorized to access this appointment");
        }

        // Nếu user là Manager hoặc Admin, không cần kiểm tra (có thể tùy chỉnh theo yêu cầu)
        if (currentUser.getRole() == Role.MANAGER ) {
            AppointmentResponse response = appointmentService.getAppointmentById(appointmentId);
            return ResponseEntity.ok(response);
        }

        // Nếu tất cả điều kiện đều thỏa mãn, trả về thông tin chi tiết của Appointment
        AppointmentResponse appointmentResponse = appointmentService.getAppointmentById(appointmentId);
        return ResponseEntity.ok(appointmentResponse);
    }



    @Operation(summary = "Check out from an appointment")
    @PostMapping("/{appointmentId}/check-out")
    public ResponseEntity<AppointmentResponse> checkOut(
            @PathVariable String appointmentId,
            @RequestBody CheckOutRequest request ,
            HttpServletRequest httpRequest) {


        Appointments appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment with ID " + appointmentId + " not found"));


        Users currentUser = tokenService.retrieveUser(httpRequest);

        if (tokenService.validateRole(httpRequest, Role.STUDENT)) {
            throw new OperationFailedException("Only psychologists can check out");
        }
        if (currentUser.getRole() == Role.PSYCHOLOGIST && !appointment.getPsychologist().getUserID().equals(currentUser.getUserId())) {
            throw new OperationFailedException("Unauthorized to access this appointment");
        }

        AppointmentResponse response = appointmentService.checkOut(appointmentId, request.getPsychologistNote());
        
     

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Submit feedback for an appointment",
            description = "Allows a student to submit feedback and rating for a completed appointment."
    )
    @PostMapping("/{appointmentId}/feedback")
    public ResponseEntity<AppointmentResponse> submitFeedback(
            @PathVariable String appointmentId,
            @RequestBody @Valid AppointmentFeedbackRequest feedback,
            HttpServletRequest httpRequest) {

        Users currentUser = tokenService.retrieveUser(httpRequest);

        Appointments appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment with ID " + appointmentId + " not found"));

        if (tokenService.validateRole(httpRequest, Role.PSYCHOLOGIST)) {
            throw new OperationFailedException("Only Students can submit feedback");
        }

        if (currentUser.getRole() == Role.STUDENT && !appointment.getStudent().getUserID().equals(currentUser.getUserId())) {
            throw new OperationFailedException("Unauthorized to access this appointment");
        }
        AppointmentResponse response = appointmentService.submitFeedback(appointmentId, feedback);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Update an appointment",
            description = "Updates time slot, notes of an appointment."
    )
    @PutMapping("/{appointmentId}")
    public ResponseEntity<AppointmentResponse> updateAppointment(
            @PathVariable String appointmentId,
            @RequestBody AppointmentUpdateRequest request,
            HttpServletRequest httpRequest) {

        Users currentUser = tokenService.retrieveUser(httpRequest);

        AppointmentResponse response = appointmentService.updateAppointment(
                appointmentId, request, currentUser.getUserId()
        );

        if (response != null) {
            return ResponseEntity.ok(response);
        }

        throw new OperationFailedException("Failed to update appointment");
    }



}

