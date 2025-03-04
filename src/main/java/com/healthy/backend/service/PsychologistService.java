package com.healthy.backend.service;

import com.healthy.backend.dto.appointment.AppointmentFeedbackResponse;

import com.healthy.backend.dto.psychologist.PsychologistRequest;
import com.healthy.backend.dto.psychologist.PsychologistResponse;
import com.healthy.backend.dto.student.StudentReplyRequest;
import com.healthy.backend.dto.student.StudentReplyResponse;
import com.healthy.backend.dto.timeslot.DefaultTimeSlotResponse;
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
    private final PsychologistsMapper psychologistsMapper;
    private final TimeSlotMapper timeSlotMapper;

    private final GeneralService __;
    private final NotificationService notificationService;

    private final DefaultTimeSlotRepository defaultTimeSlotRepository;

    private final FeedbackRepository feedbackRepository;
    private final PsychologistCommentRepository psychologistCommentRepository;
    private final StudentReplyRepository studentReplyRepository;
    private final StudentRepository studentRepository;

    public PsychologistResponse getPsychologistByUserId(String userId) {
        Psychologists psychologist = psychologistRepository.findByUserID(userId);
        if (psychologist == null) {
            throw new ResourceNotFoundException("Psychologist not found for user");
        }

        return callMapper(psychologist);
    }

    public String getPsychologistIdByUserId(String userId) {
        Psychologists psychologist = psychologistRepository.findByUserID(userId);
        if (psychologist == null) {
            throw new ResourceNotFoundException("Psychologist not found for user");
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


    public Page<AppointmentFeedbackResponse> getPsychologistFeedbacks(String psychologistId, int page, int size) {
        Psychologists psychologist = psychologistRepository.findById(psychologistId)
                .orElseThrow(() -> new ResourceNotFoundException("Psychologist not found"));

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Appointments> appointmentsPage = appointmentRepository.findByPsychologistIDAndStatusAndFeedbacksNotNull(
                psychologistId, AppointmentStatus.COMPLETED, pageable);

        return appointmentsPage.map(appointment -> {
            // Xử lý feedbacks
            List<Feedback> feedbacks = appointment.getFeedbacks() != null
                    ? appointment.getFeedbacks()
                    : Collections.emptyList();

            List<String> feedbackComments = feedbacks.stream()
                    .map(Feedback::getComment)
                    .collect(Collectors.toList());

            List<Long> feedbackIds = feedbacks.stream()
                    .map(Feedback::getId)
                    .collect(Collectors.toList());

            // Xử lý psychologist comments và student replies
            List<PsychologistComment> psychologistComments = appointment.getPsychologistComments() != null
                    ? appointment.getPsychologistComments()
                    : Collections.emptyList();

            List<AppointmentFeedbackResponse.PsychologistCommentResponse> psychologistCommentResponses = psychologistComments.stream()
                    .map(comment -> {
                        List<StudentReply> studentReplies = comment.getStudentReplies() != null
                                ? comment.getStudentReplies()
                                : Collections.emptyList();

                        List<StudentReplyResponse> replyResponses = studentReplies.stream()
                                .map(reply -> {
                                    String studentName = reply.getStudent() != null
                                            ? reply.getStudent().getUser().getFullName()
                                            : "Unknown Student";
                                    return new StudentReplyResponse(
                                            reply.getId(),
                                            reply.getComment(),
                                            reply.getCreatedAt(),
                                            studentName
                                    );
                                })
                                .collect(Collectors.toList());

                        return new AppointmentFeedbackResponse.PsychologistCommentResponse(
                                comment.getId(),
                                comment.getComment(),
                                comment.getCreatedAt(),
                                replyResponses
                        );
                    })
                    .collect(Collectors.toList());

            // Tạo response
            return new AppointmentFeedbackResponse(
                    appointment.getTimeSlot().getSlotDate().atTime(appointment.getTimeSlot().getStartTime()),
                    appointment.getStudent().getUser().getFullName(),
                    feedbackComments,
                    feedbackIds,
                    appointment.getRating(),
                    psychologistCommentResponses
            );
        });
    }

    public double calculateAverageRating(String psychologistId) {
        Psychologists psychologist = psychologistRepository.findById(psychologistId)
                .orElseThrow(() -> new ResourceNotFoundException("Psychologist not found"));

        List<Appointments> appointments = appointmentRepository.findByPsychologistIDAndStatusAndFeedbacksNotNull(
                psychologistId, AppointmentStatus.COMPLETED);
        if (appointments.isEmpty()) {
            return 0.0;
        }

        double totalRating = appointments.stream()
                .mapToInt(Appointments::getRating)
                .sum();

        return totalRating / appointments.size();
    }





}
