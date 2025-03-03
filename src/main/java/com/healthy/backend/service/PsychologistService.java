package com.healthy.backend.service;

import com.healthy.backend.dto.appointment.AppointmentFeedbackResponse;
//import com.healthy.backend.dto.psychologist.LeaveRequest;
//import com.healthy.backend.dto.psychologist.LeaveResponse;
import com.healthy.backend.dto.psychologist.PsychologistRequest;
import com.healthy.backend.dto.psychologist.PsychologistResponse;
import com.healthy.backend.dto.timeslot.DefaultTimeSlotResponse;
import com.healthy.backend.dto.timeslot.TimeSlotCreateRequest;
import com.healthy.backend.dto.timeslot.TimeSlotResponse;
import com.healthy.backend.entity.*;
import com.healthy.backend.enums.*;
import com.healthy.backend.exception.OperationFailedException;
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
 //   private final LeaveRequestRepository leaveRequestRepository;

    private final PsychologistsMapper psychologistsMapper;
    private final TimeSlotMapper timeSlotMapper;

    private final GeneralService __;
    private final NotificationService notificationService;

    private final DefaultTimeSlotRepository defaultTimeSlotRepository;

    public PsychologistResponse getPsychologistByUserId(String userId) {
        Psychologists psychologist = psychologistRepository.findByUserID(userId);
        if (psychologist == null) {
           throw  new ResourceNotFoundException("Psychologist not found for user");
        }

        return callMapper(psychologist);
    }
    public String getPsychologistIdByUserId(String userId) {
        Psychologists psychologist = psychologistRepository.findByUserID(userId);
        if (psychologist == null) {
            throw  new ResourceNotFoundException("Psychologist not found for user");
        }

        return psychologist.getPsychologistID();

    }

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

       // updatePsychologistStatusBasedOnLeaveRequests(psychologist);

        return callMapper(psychologist);
    }

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

    public PsychologistResponse updatePsychologist(String id, PsychologistRequest request, String currentUserId) {
        // Tìm psychologist cần cập nhật
        Psychologists psychologist = psychologistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No psychologist found with id " + id));

        // Kiểm tra quyền: chỉ psychologist đó hoặc Manager được cập nhật
        if (!psychologist.getUserID().equals(currentUserId)) {
            throw new OperationFailedException("Unauthorized update");
        }

        // Kiểm tra nếu không có trường nào để cập nhật
        if (request.getDepartmentID() == null
                && request.getYearsOfExperience() == null) {
            throw new IllegalArgumentException("No fields to update");
        }

        // Cập nhật department nếu có
        if (request.getDepartmentID() != null
                && !request.getDepartmentID().equals(psychologist.getDepartment().getName())) {
            if (!departmentRepository.existsById(request.getDepartmentID())) {
                throw new ResourceNotFoundException("Department not found");
            }
            psychologist.setDepartment(departmentRepository.findById(request.getDepartmentID()).orElseThrow());
        }

        // Cập nhật yearsOfExperience nếu có
        if (request.getYearsOfExperience() != null
                && !request.getYearsOfExperience().equals(psychologist.getYearsOfExperience())) {
            psychologist.setYearsOfExperience(request.getYearsOfExperience());
        }

        // Lưu thay đổi
        psychologistRepository.save(psychologist);

        // Trả về response
        return callMapper(psychologist);
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


    @EventListener(ApplicationReadyEvent.class)
    public void initDefaultSlots() {
        if (defaultTimeSlotRepository.count() == 0) {
            List<DefaultTimeSlot> slots = new ArrayList<>();

            // Morning slots 8:00-11:00
            LocalTime time = LocalTime.of(8, 0);
            for (int i = 0; time.isBefore(LocalTime.of(11, 0)); i++) {
                slots.add(new DefaultTimeSlot(
                        "MORNING-" + String.format("%02d", i),
                        time,
                        time.plusMinutes(30),
                        "Morning"
                ));
                time = time.plusMinutes(30);
            }

            // Afternoon slots 13:00-17:00
            time = LocalTime.of(13, 0);
            for (int i = 0; time.isBefore(LocalTime.of(17, 0)); i++) {
                slots.add(new DefaultTimeSlot(
                        "AFTERNOON-" + String.format("%02d", i),
                        time,
                        time.plusMinutes(30),
                        "Afternoon"
                ));
                time = time.plusMinutes(30);
            }

            defaultTimeSlotRepository.saveAll(slots);
        }
    }

    public List<DefaultTimeSlotResponse> getDefaultTimeSlots() {
        return defaultTimeSlotRepository.findAll().stream()
                .map(s -> new DefaultTimeSlotResponse(
                        s.getSlotId(),
                        s.getStartTime(),
                        s.getEndTime(),
                        s.getPeriod()
                ))
                .toList();
    }

    @Transactional
    public List<TimeSlotResponse> createTimeSlotsFromDefaults(
            String psychologistId,
            LocalDate slotDate,
            List<String> defaultSlotIds
    ) {
        Psychologists psychologist = psychologistRepository.findById(psychologistId)
                .orElseThrow(() -> new ResourceNotFoundException("Psychologist not found"));

        List<DefaultTimeSlot> defaultSlots = defaultTimeSlotRepository.findAllById(defaultSlotIds);

        if (defaultSlots.size() != defaultSlotIds.size()) {
            throw new ResourceNotFoundException("Some default slots not found");
        }

        List<TimeSlots> newSlots = new ArrayList<>();

        for (DefaultTimeSlot defaultSlot : defaultSlots) {
            // Check existing slots
            boolean exists = timeSlotRepository.existsByPsychologistAndSlotDateAndStartTimeAndEndTime(
                    psychologist,
                    slotDate,
                    defaultSlot.getStartTime(),
                    defaultSlot.getEndTime()
            );

            if (!exists) {
                TimeSlots slot = new TimeSlots();
                slot.setSlotDate(slotDate);
                slot.setStartTime(defaultSlot.getStartTime());
                slot.setEndTime(defaultSlot.getEndTime());
                slot.setPsychologist(psychologist);
                slot.setMaxCapacity(3); // Default capacity
                slot.setStatus(TimeslotStatus.AVAILABLE);
                slot.setTimeSlotsID(generateSlotId(psychologistId, slotDate, defaultSlot.getSlotId()));

                newSlots.add(slot);
            }
        }

        timeSlotRepository.saveAll(newSlots);

        return newSlots.stream()
                .map(timeSlotMapper::toResponse)
                .toList();
    }

    private String generateSlotId(String psychologistId, LocalDate date, String defaultSlotId) {
        return "TS-" + psychologistId + "-" + date.toString() + "-" + defaultSlotId;
    }




    public List<TimeSlotResponse> getPsychologistTimeSlots(
            String psychologistId,
            LocalDate date,
            String studentId) {

        List<TimeSlots> slots;
        if (date != null) {
            slots = timeSlotRepository.findByPsychologistIdAndDate(psychologistId, date);
        } else {
            slots = timeSlotRepository.findByPsychologistId(psychologistId);
        }

        return slots.stream()
                .map(slot -> {
                    TimeSlotResponse response = timeSlotMapper.toResponse(slot);

                    // Kiểm tra xem student đã đặt slot này chưa
                    boolean isBooked = studentId != null &&
                            appointmentRepository.existsByStudentIDAndTimeSlotsID(studentId, slot.getTimeSlotsID());
                    response.setBooked(isBooked);

                    return response;
                })
                .collect(Collectors.toList());
    }

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
            return 0.0;
        }

        double totalRating = appointments.stream()
                .mapToInt(Appointments::getRating)
                .sum();

        return totalRating / appointments.size();
    }

//    public List<LeaveResponse> getApprovedLeaveRequestsByPsychologist(String psychologistId) {
//        List<OnLeaveRequest> requests = leaveRequestRepository.findByPsychologistPsychologistIDAndStatus(
//                psychologistId, OnLeaveStatus.APPROVED);
//        return requests.stream()
//                .map(psychologistsMapper::buildLeaveResponse)
//                .collect(Collectors.toList());
//    }
}
