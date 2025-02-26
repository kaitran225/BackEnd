package com.healthy.backend.service;

import com.healthy.backend.dto.appointment.AppointmentFeedbackResponse;
import com.healthy.backend.dto.psychologist.LeaveRequest;
import com.healthy.backend.dto.psychologist.LeaveResponse;
import com.healthy.backend.dto.psychologist.PsychologistRequest;
import com.healthy.backend.dto.psychologist.PsychologistResponse;
import com.healthy.backend.dto.timeslot.TimeSlotResponse;
import com.healthy.backend.entity.*;
import com.healthy.backend.enums.AppointmentStatus;
import com.healthy.backend.enums.OnLeaveStatus;
import com.healthy.backend.enums.PsychologistStatus;
import com.healthy.backend.exception.ResourceNotFoundException;
import com.healthy.backend.mapper.PsychologistsMapper;
import com.healthy.backend.mapper.TimeSlotMapper;
import com.healthy.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PsychologistService {
    private final UserRepository userRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final DepartmentRepository departmentRepository;
    private final AppointmentRepository appointmentRepository;
    private final PsychologistRepository psychologistRepository;
    private final LeaveRequestRepository leaveRequestRepository;

    private final PsychologistsMapper psychologistsMapper;
    private final TimeSlotMapper timeSlotMapper;

    private final GeneralService __;
    private final NotificationService notificationService;

    // Get all psychologist
    public List<PsychologistResponse> getAllPsychologistDTO() {
        List<Psychologists> psychologists = psychologistRepository.findAll();
        return psychologists.stream().map(this::callMapper).toList();
    }

    // Get psychologist by specialization
    public List<PsychologistResponse> getAllPsychologistByDepartment(String departmentID) {
        if (departmentID == null || departmentID.isEmpty()) {
            throw new IllegalArgumentException("Department ID is required");
        }

        Department department = departmentRepository.findById(departmentID)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

        List<Psychologists> psychologists = psychologistRepository.findByDepartmentDepartmentID(departmentID);
        return psychologists.stream()
                .map(psychologistsMapper::buildPsychologistResponse)
                .collect(Collectors.toList());
    }

    // Get psychologist by id
    public PsychologistResponse getPsychologistById(String id) {
        Psychologists psychologist = psychologistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No psychologist found with id " + id));
    
        updatePsychologistStatusBasedOnLeaveRequests(psychologist);
    
        return callMapper(psychologist);
    }

    // Update psychologist status based on leave requests
    private void updatePsychologistStatusBasedOnLeaveRequests(Psychologists psychologist) {
        LocalDate today = LocalDate.now();
    
        List<OnLeaveRequest> approvedLeaves = leaveRequestRepository
                .findByPsychologistPsychologistIDAndStatus(
                        psychologist.getPsychologistID(),
                        OnLeaveStatus.APPROVED
                );
    
        boolean isOnLeave = approvedLeaves.stream()
                .anyMatch(leave ->
                        !today.isBefore(leave.getStartDate())
                                && !today.isAfter(leave.getEndDate()));
    
        if (isOnLeave) {
            psychologist.setStatus(PsychologistStatus.ON_LEAVE);
        } else {
            psychologist.setStatus(PsychologistStatus.ACTIVE);
        }
    
        psychologistRepository.save(psychologist);
    }

    // Update psychologist
    public PsychologistResponse updatePsychologist(String id, PsychologistRequest request) {
        Psychologists psychologist = psychologistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No psychologist found with id " + id));
        if (request.getDepartmentID() == null
                && request.getYearsOfExperience() == null
                ) {
            throw new IllegalArgumentException("No fields to update");
        }
        // Cập nhật các trường
        if (request.getDepartmentID() != null
                && !request.getDepartmentID().equals(psychologist.getDepartment().getName())) {
            if (!departmentRepository.existsById(request.getDepartmentID())) {
                throw new ResourceNotFoundException("Department not found");
            }
            psychologist.setDepartment(departmentRepository.findById(request.getDepartmentID()).orElseThrow());
        }
        if (request.getYearsOfExperience() != null
                && !request.getYearsOfExperience().equals(psychologist.getYearsOfExperience())) {
            psychologist.setYearsOfExperience(request.getYearsOfExperience());
        }

        psychologistRepository.save(psychologist);
        return callMapper(psychologist);
    }
    // Get available time slots
    public List<TimeSlotResponse> getTimeSlots(LocalDate date, String psychologistId) {
        Psychologists psychologist = psychologistRepository.findById(psychologistId)
                .orElseThrow(() -> new ResourceNotFoundException("Psychologist not found"));

        List<OnLeaveRequest> leaves = leaveRequestRepository
                .findByPsychologistPsychologistIDAndStatusAndDateRange(
                        psychologistId,
                        OnLeaveStatus.APPROVED,
                        date
                );

        if (!leaves.isEmpty()) {

            return Collections.emptyList();        }


        List<TimeSlots> timeSlots = timeSlotRepository.findBySlotDateAndPsychologist(date, psychologist);
        return timeSlotMapper.buildResponse(timeSlots);
    }

    // Create default time slots
    public List<TimeSlotResponse> createDefaultTimeSlots(LocalDate date, String psychologistId) {
        Psychologists psychologist = psychologistRepository.findById(psychologistId)
                .orElseThrow(() -> new ResourceNotFoundException("Psychologist not found"));

        List<TimeSlots> existingSlots = timeSlotRepository.findBySlotDateAndPsychologist(date, psychologist);
        if (!existingSlots.isEmpty()) {
            throw new IllegalStateException("Time slots already exist for this date");
        }
        List<TimeSlots> timeSlots = new ArrayList<>(generateTimeSlots(date, psychologist));
        timeSlotRepository.saveAll(timeSlots);
        return timeSlotMapper.buildResponse(timeSlots);
    }

    // Generate time slots
    private List<TimeSlots> generateTimeSlots(LocalDate date, Psychologists psychologist) {

        LocalTime start = LocalTime.of(8, 0); // Morning shift: 8h - 11h
        LocalTime noonBreakStart = LocalTime.of(11, 30); //
        LocalTime noonBreakEnd = LocalTime.of(12, 30); // Afternoon shift: 13h - 17h
        LocalTime end = LocalTime.of(17, 0);

        List<TimeSlots> timeSlots = new ArrayList<>();
        LocalTime currentTime = start;
        int index = 0;

        while (currentTime.isBefore(end)) {
            if (currentTime.isAfter(noonBreakStart.minusMinutes(1))
                    && currentTime.isBefore(noonBreakEnd)) {
                currentTime = noonBreakEnd;
                continue;
            }
            LocalTime nextTime = currentTime.plusMinutes(30);
            timeSlots.add(new TimeSlots(date, currentTime, nextTime, psychologist, index++));
            currentTime = nextTime;
        }
        return timeSlots;
    }

    // Call psychologistResponse Mapper
    private PsychologistResponse callMapper(Psychologists psychologist) {
        return psychologistsMapper.buildPsychologistResponse(psychologist,
                appointmentRepository.findByPsychologistID(psychologist.getPsychologistID()),
                userRepository.findById(psychologist.getUserID()).orElseThrow());
    }

    // Check if status is valid
    private boolean isValidStatus(String status) {
        try {
            PsychologistStatus.valueOf(status);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public LeaveResponse createLeaveRequest(LeaveRequest request) {
        // Validate date range
        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new IllegalArgumentException("Start date must be before end date");
        }

        // Check if the leave duration exceeds 7 days
        long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate());
        if (daysBetween > 7) {
            throw new IllegalArgumentException("Leave duration cannot exceed 7 days");
        }

        // Check psychologist exists and is active
        Psychologists psychologist = psychologistRepository.findById(request.getPsychologistId())
                .orElseThrow(() -> new ResourceNotFoundException("Psychologist not found"));

        if (psychologist.getStatus() != PsychologistStatus.ACTIVE) {
            throw new IllegalStateException("Psychologist must be active to request leave");
        }

        // Check for overlapping approved leaves
        List<OnLeaveRequest> overlappingLeaves = leaveRequestRepository
                .findByPsychologistAndStatusAndDateRange(
                        psychologist,
                        request.getStartDate(),
                        request.getEndDate()
                );

        if (!overlappingLeaves.isEmpty()) {
            throw new IllegalStateException("Existing approved/pending leave in this period");
        }

        // Create and save request
       // Tạo và lưu yêu cầu nghỉ phép
    OnLeaveRequest onRequest = psychologistsMapper.createPendingOnLeaveRequestEntity(request,
    __.generateLeaveRequestID(), psychologist);

// Kiểm tra nếu yêu cầu đã hết hạn
        if (LocalDate.now().isAfter(request.getStartDate())) {
                onRequest.setStatus(OnLeaveStatus.EXPIRED);
            }

        OnLeaveRequest saved = leaveRequestRepository.save(onRequest);

// Cập nhật trạng thái của nhà tâm lý học
        updatePsychologistStatusBasedOnLeaveRequests(psychologist);

// Gửi thông báo
          notificationService.createOnLeaveNotification(
            psychologist.getUserID(), 
        "Leave Request Created", 
        "Your leave request has been created.", 
        saved.getLeaveRequestID()
);

           return psychologistsMapper.buildLeaveResponse(saved);
}

    public List<LeaveResponse> getPendingRequests() {
        return leaveRequestRepository.findByStatus(OnLeaveStatus.PENDING)
                .stream()
                .map(psychologistsMapper::buildLeaveResponse)
                .collect(Collectors.toList());
    }

    public List<LeaveResponse> getAllLeaveRequests() {
        return leaveRequestRepository.findAll()
                .stream()
                .map(psychologistsMapper::buildLeaveResponse)
                .collect(Collectors.toList());
    }

    public LeaveResponse processLeaveRequest(String requestId, boolean approve) {
        OnLeaveRequest request = leaveRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found"));

        if (approve) {
            request.setStatus(OnLeaveStatus.APPROVED);
            Psychologists psychologist = request.getPsychologist();
            psychologist.setStatus(PsychologistStatus.ON_LEAVE);
            psychologistRepository.save(psychologist);
        } else {
            request.setStatus(OnLeaveStatus.REJECTED);
        }

        OnLeaveRequest updated = leaveRequestRepository.save(request);

        // Notify psychologist
        notificationService.createOnLeaveNotification(
            request.getPsychologist().getUserID(), 
            "Leave Request Processed", 
            "Your leave request has been " + (approve ? "approved" : "rejected") + ".", 
            requestId 
        );

        return psychologistsMapper.buildLeaveResponse(updated);
    }

    public List<LeaveResponse> getLeaveRequestsByPsychologist(String psychologistId) {
        List<OnLeaveRequest> requests = leaveRequestRepository.findByPsychologistPsychologistID(psychologistId);
        return requests.stream()
                .map(psychologistsMapper::buildLeaveResponse)
                .collect(Collectors.toList());
    }

    public void updatePsychologistStatusBasedOnLeaveRequests() {
        LocalDate today = LocalDate.now();

        List<Psychologists> psychologists = psychologistRepository.findAll();

        for (Psychologists psychologist : psychologists) {
            List<OnLeaveRequest> approvedLeaves = leaveRequestRepository
                    .findByPsychologistPsychologistIDAndStatus(
                            psychologist.getPsychologistID(),
                            OnLeaveStatus.APPROVED
                    );

            boolean isOnLeave = approvedLeaves.stream()
                    .anyMatch(leave ->
                            !today.isBefore(leave.getStartDate())
                                    && !today.isAfter(leave.getEndDate()));

            if (isOnLeave) {
                psychologist.setStatus(PsychologistStatus.ON_LEAVE);
            } else {
                psychologist.setStatus(PsychologistStatus.ACTIVE);
            }

            psychologistRepository.save(psychologist);
        }
    }

    @EventListener(ApplicationReadyEvent.class)
    public void updatePsychologistStatusOnStartup() {

        this.updatePsychologistStatusBasedOnLeaveRequests();
    }

    public LeaveResponse cancelLeave(String psychologistId, String leaveId) {
        Psychologists psychologist = psychologistRepository.findById(psychologistId)
                .orElseThrow(() -> new ResourceNotFoundException("Psychologist not found"));
    
        OnLeaveRequest leaveRequest = leaveRequestRepository.findById(leaveId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found"));
    
        if (leaveRequest.getStatus() != OnLeaveStatus.PENDING) {
            throw new IllegalStateException("Leave request is not pending");
        }
    
        leaveRequest.setStatus(OnLeaveStatus.CANCELLED);
        leaveRequestRepository.save(leaveRequest);
    
        psychologist.setStatus(PsychologistStatus.ACTIVE);
        psychologistRepository.save(psychologist);
    
        notificationService.createOnLeaveNotification(
            psychologist.getUserID(),
            "Leave Request Cancelled",
            "Your leave request has been cancelled.",
            leaveId
        );
    
        return psychologistsMapper.buildLeaveResponse(leaveRequest);
    }

    public PsychologistResponse onReturn(String psychologistId, String leaveId) {
        Psychologists psychologist = psychologistRepository.findById(psychologistId)
                .orElseThrow(() -> new ResourceNotFoundException("Psychologist not found"));

        OnLeaveRequest leaveRequest = leaveRequestRepository.findById(leaveId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found"));

        if (leaveRequest.getStatus() != OnLeaveStatus.APPROVED) {
            throw new IllegalStateException("Leave request is not approved");
        }

        leaveRequest.setStatus(OnLeaveStatus.REJECTED);
        leaveRequestRepository.save(leaveRequest);

        psychologist.setStatus(PsychologistStatus.ACTIVE);
        psychologistRepository.save(psychologist);
        return psychologistsMapper.buildPsychologistResponse(psychologist);
    }

    @Transactional
    public PsychologistResponse deletePsychologist(String psychologistId) {
        Psychologists psychologist = psychologistRepository.findById(psychologistId)
                .orElseThrow(() -> new ResourceNotFoundException("Psychologist not found"));
        psychologistRepository.delete(psychologist);
        return psychologistsMapper.buildPsychologistResponse(psychologist);
    }

    public Page<AppointmentFeedbackResponse> getPsychologistFeedbacks(String psychologistId, int page, int size) {
        Psychologists psychologist = psychologistRepository.findById(psychologistId)
                .orElseThrow(() -> new ResourceNotFoundException("Psychologist not found"));

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Appointments> appointmentsPage = appointmentRepository.findByPsychologistIDAndStatusAndFeedbackNotNull(
                psychologistId, AppointmentStatus.COMPLETED, pageable);

        return appointmentsPage.map(a -> new AppointmentFeedbackResponse(
                a.getTimeSlot().getSlotDate().atTime(a.getTimeSlot().getStartTime()),
                a.getStudent().getUser().getFullName(),
                a.getFeedback(),
                a.getRating()
        ));
    }

    public double calculateAverageRating(String psychologistId) {
        Psychologists psychologist = psychologistRepository.findById(psychologistId)
                .orElseThrow(() -> new ResourceNotFoundException("Psychologist not found"));

        List<Appointments> appointments = appointmentRepository.findByPsychologistIDAndStatusAndFeedbackNotNull(
                psychologistId, AppointmentStatus.COMPLETED);

        if (appointments.isEmpty()) {
            return 0.0; // Trả về 0 nếu không có feedback nào
        }

        double totalRating = appointments.stream()
                .mapToInt(Appointments::getRating)
                .sum();

        return totalRating / appointments.size();
    }

    public List<LeaveResponse> getApprovedLeaveRequestsByPsychologist(String psychologistId) {
        List<OnLeaveRequest> requests = leaveRequestRepository.findByPsychologistPsychologistIDAndStatus(
                psychologistId, OnLeaveStatus.APPROVED);
        return requests.stream()
                .map(psychologistsMapper::buildLeaveResponse)
                .collect(Collectors.toList());
    }
}
