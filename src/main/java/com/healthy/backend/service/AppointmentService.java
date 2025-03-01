package com.healthy.backend.service;

import com.healthy.backend.dto.appointment.AppointmentFeedbackRequest;
import com.healthy.backend.dto.appointment.AppointmentRequest;
import com.healthy.backend.dto.appointment.AppointmentResponse;
import com.healthy.backend.dto.appointment.AppointmentUpdateRequest;
import com.healthy.backend.dto.psychologist.DepartmentResponse;
import com.healthy.backend.entity.*;
import com.healthy.backend.enums.AppointmentStatus;
import com.healthy.backend.enums.TimeslotStatus;
import com.healthy.backend.exception.OperationFailedException;
import com.healthy.backend.exception.ResourceAlreadyExistsException;
import com.healthy.backend.exception.ResourceInvalidException;
import com.healthy.backend.exception.ResourceNotFoundException;
import com.healthy.backend.mapper.*;
import com.healthy.backend.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final PsychologistRepository psychologistRepository;
    private final AppointmentRepository appointmentRepository;
    private final DepartmentRepository departmentRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;

    private final GeneralService __;
    private final EmailService emailService;
    private final NotificationService notificationService;

    private final PsychologistsMapper psychologistMapper;
    private final AppointmentMapper appointmentMapper;
    private final DepartmentMapper departmentMapper;
    private final StudentMapper studentMapper;
    private final UserMapper userMapper;


    public List<DepartmentResponse> getAllDepartments() {
        return departmentRepository.findAll()
                .stream()
                .map(departmentMapper::buildDepartmentResponse)
                .collect(Collectors.toList());
    }

    public List<AppointmentResponse> getAllAppointments() {
        List<Appointments> appointments = appointmentRepository.findAll();
        if (appointments.isEmpty()) {
            throw new ResourceNotFoundException("No appointments found");
        }
        return appointments.stream()
                .map(appointment ->
                        appointmentMapper.buildAppointmentResponse(
                                appointment,
                                psychologistMapper.buildPsychologistResponse(
                                        Objects.requireNonNull(psychologistRepository.findById(
                                                appointment.getPsychologistID()).orElse(null))),
                                studentMapper.buildBasicStudentResponse(
                                        Objects.requireNonNull(studentRepository.findById(
                                                appointment.getStudentID()).orElse(null)))
                        ))
                .collect(Collectors.toList());
    }

    public AppointmentResponse getAppointmentById(String id) {
        Appointments appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No appointment by id" + id + "Not found"));
        Psychologists psychologists = psychologistRepository.findById(appointment.getPsychologistID())
                .orElseThrow(() -> new ResourceNotFoundException("No psychologist found"));
        Students students = studentRepository.findById(appointment.getStudentID())
                .orElseThrow(() -> new ResourceNotFoundException("No student found"));
        return appointmentMapper.buildAppointmentResponse(
                appointment, psychologistMapper.buildPsychologistResponse(
                        psychologists,
                        userMapper.buildBasicUserResponse(psychologists.getUser())),
                studentMapper.buildStudentResponse(
                        students,
                        userMapper.buildBasicUserResponse(students.getUser()))
        );
    }

    @Transactional
    public AppointmentResponse bookAppointment(AppointmentRequest request) {
        // Validate và tìm time slot
        TimeSlots timeSlot = timeSlotRepository.findById(request.getTimeSlotId())
                .orElseThrow(() -> new ResourceNotFoundException("Time slot not found with id: " + request.getTimeSlotId()));

        // Kiểm tra capacity của time slot
        if (timeSlot.getCurrentBookings() >= timeSlot.getMaxCapacity()) {
            throw new ResourceAlreadyExistsException("Time slot is full. Maximum capacity: " + timeSlot.getMaxCapacity());
        }

        // Validate student
        Students student = studentRepository.findByUserID(request.getUserId());
        if (student == null) {
            throw new ResourceNotFoundException("Student not found" + request.getUserId());
        }

        // Validate psychologist
        Psychologists psychologist = psychologistRepository.findById(timeSlot.getPsychologist().getPsychologistID())
                .orElseThrow(() -> new ResourceNotFoundException("Psychologist not found with ID: " + timeSlot.getPsychologist().getPsychologistID()));

        // Kiểm tra xem student đã có appointment nào trong cùng time slot chưa
        boolean hasExistingAppointment = appointmentRepository.existsByStudentIDAndTimeSlotsID(
                student.getStudentID(), timeSlot.getTimeSlotsID());
        if (hasExistingAppointment) {
            throw new ResourceAlreadyExistsException("Student already has an appointment in this time slot");
        }

        // Tạo appointment mới
        Appointments appointment = new Appointments();
        appointment.setAppointmentID(__.generateAppointmentId());
        appointment.setTimeSlotsID(timeSlot.getTimeSlotsID());
        appointment.setStudentID(student.getStudentID());
        appointment.setPsychologistID(psychologist.getPsychologistID());
        appointment.setPsychologist(psychologist);
        appointment.setStudent(student);
        appointment.setTimeSlot(timeSlot);
        appointment.setStatus(AppointmentStatus.SCHEDULED);

        // Lưu appointment
        Appointments savedAppointment = appointmentRepository.save(appointment);

        // Cập nhật số lượng bookings trong time slot
        timeSlot.setCurrentBookings(timeSlot.getCurrentBookings() + 1);
        if (timeSlot.getCurrentBookings() >= timeSlot.getMaxCapacity()) {
            timeSlot.setStatus(TimeslotStatus.BOOKED);
        }
        timeSlotRepository.save(timeSlot);

        // Gửi thông báo cho psychologist
        Users psychologistUser = userRepository.findByUserId(psychologist.getUserID())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (psychologistUser.getEmail() != null) {
            emailService.sendNewAppointmentEmail(
                    psychologistUser.getEmail(),
                    psychologist.getFullNameFromUser(),
                    student,
                    savedAppointment.getAppointmentID(),
                    timeSlot,
                    "New Appointment Booked"
            );
        }

        // Tạo notification cho psychologist
        notificationService.createAppointmentNotification(
                psychologistUser.getUserId(),
                "New Appointment Booked",
                "You have a new appointment with " + student.getUser().getFullName(),
                savedAppointment.getAppointmentID()
        );

        // Trả về response
        return appointmentMapper.buildAppointmentResponse(
                savedAppointment,
                psychologistMapper.buildPsychologistResponse(psychologist),
                studentMapper.buildBasicStudentResponse(student)
        );
    }

    // Cancel
    @Transactional
    public AppointmentResponse cancelAppointment(String appointmentId, String userId) {
        // Tìm appointment
        Appointments appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + appointmentId));

        // Tìm time slot liên quan
        TimeSlots timeSlot = timeSlotRepository.findById(appointment.getTimeSlotsID())
                .orElseThrow(() -> new ResourceNotFoundException("Time slot not found with id: " + appointment.getTimeSlotsID()));

        // Validate user (student hoặc psychologist)
        Users user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        // Kiểm tra quyền hủy appointment
        if (!userId.equals(appointment.getStudent().getUserID())
                && !userId.equals(appointment.getPsychologist().getUserID())) {
            throw new ResourceInvalidException("You are not authorized to cancel this appointment");
        }

        // Kiểm tra trạng thái appointment
        if (appointment.getStatus() == AppointmentStatus.IN_PROGRESS) {
            throw new ResourceInvalidException("Cannot cancel an appointment that is In Progress");
        }
        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new ResourceInvalidException("Appointment is already completed");
        }

        // Cập nhật trạng thái appointment
        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);

        // Cập nhật lại số lượng bookings trong time slot
        timeSlot.setCurrentBookings(timeSlot.getCurrentBookings() - 1);
        if (timeSlot.getCurrentBookings() < timeSlot.getMaxCapacity()) {
            timeSlot.setStatus(TimeslotStatus.AVAILABLE);
        }
        timeSlotRepository.save(timeSlot);

        // Gửi thông báo cho cả student và psychologist
        String psychologistName = appointment.getPsychologist().getFullNameFromUser();
        String studentName = appointment.getStudent().getUser().getFullName();

        if ("Psychologist".equalsIgnoreCase(String.valueOf(user.getRole()))) {
            // Thông báo cho student
            notificationService.createAppointmentNotification(
                    appointment.getStudent().getUserID(),
                    "Appointment Canceled",
                    "Your appointment has been canceled by " + psychologistName,
                    appointmentId
            );

            // Thông báo cho psychologist
            notificationService.createAppointmentNotification(
                    appointment.getPsychologist().getUserID(),
                    "Appointment Canceled",
                    "You declined the appointment",
                    appointmentId
            );
        } else if ("Student".equalsIgnoreCase(String.valueOf(user.getRole()))) {
            // Thông báo cho psychologist
            notificationService.createAppointmentNotification(
                    appointment.getPsychologist().getUserID(),
                    "Appointment Canceled",
                    "Your appointment has been canceled by " + studentName,
                    appointmentId
            );

            // Thông báo cho student
            notificationService.createAppointmentNotification(
                    appointment.getStudent().getUserID(),
                    "Appointment Canceled",
                    "You declined the appointment",
                    appointmentId
            );
        }

        // Trả về response
        return appointmentMapper.buildAppointmentResponse(appointment);
    }

    public AppointmentResponse updateAppointment(String appointmentId, AppointmentUpdateRequest request) {
        Appointments appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot find appointment with id " + appointmentId));

        // Check appointment status
        if (appointment.getStatus() != AppointmentStatus.SCHEDULED) {
            throw new ResourceInvalidException("You can only update a scheduled appointment");
        }

        if (!request.getTimeSlotId().equals(appointment.getTimeSlotsID())) {
            TimeSlots newTimeSlot = timeSlotRepository.findByIdWithPsychologist(request.getTimeSlotId())
                    .orElseThrow(() -> new ResourceNotFoundException("Cannot find time slot with id" + request.getTimeSlotId()));

            if (newTimeSlot.getStatus() == TimeslotStatus.BOOKED) {
                throw new ResourceNotFoundException("Time slot is not available");
            }

            TimeSlots oldTimeSlot = timeSlotRepository.findById(appointment.getTimeSlotsID())
                    .orElseThrow(() -> new ResourceNotFoundException("Cannot find time slot with id" + appointment.getTimeSlotsID()));

            if (!oldTimeSlot.getPsychologist().getPsychologistID().equals(newTimeSlot.getPsychologist().getPsychologistID())) {
                handlePsychologistChange(appointment, oldTimeSlot, newTimeSlot, appointmentId);
            }

            updateTimeSlotStatus(newTimeSlot, oldTimeSlot);

            appointment.setTimeSlotsID(newTimeSlot.getTimeSlotsID());
            appointment.setPsychologistID(newTimeSlot.getPsychologist().getPsychologistID());
        }

        if (request.getNotes() != null) {
            appointment.setStudentNote(request.getNotes());
        }

        appointmentRepository.save(appointment);
        return appointmentMapper.buildAppointmentResponse(appointment);
    }

    // Check in
    public AppointmentResponse checkIn(String appointmentId) {
        Appointments appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + appointmentId));

        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new OperationFailedException("Appointment is cancelled");
        }

        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new OperationFailedException("Appointment is already completed");
        }

        if (appointment.getStatus() == AppointmentStatus.IN_PROGRESS) {
            throw new OperationFailedException("Appointment is already in progress");
        }


        appointment.setStatus(AppointmentStatus.IN_PROGRESS);
        appointment.setCheckInTime(LocalDateTime.now());
        appointmentRepository.save(appointment);

        return appointmentMapper.buildAppointmentResponse(
                appointment,
                psychologistMapper.buildPsychologistResponse(
                        Objects.requireNonNull(psychologistRepository.findById(
                                appointment.getPsychologistID()).orElse(null))),
                studentMapper.buildBasicStudentResponse(
                        Objects.requireNonNull(studentRepository.findById(
                                appointment.getStudentID()).orElse(null)))
        );
    }

    // Check out
    public AppointmentResponse checkOut(String appointmentId, String psychologistNote) {
        Appointments appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + appointmentId));

        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new OperationFailedException("Appointment is cancelled");
        }

        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new OperationFailedException("Appointment is already completed");
        }

        if (appointment.getStatus() == AppointmentStatus.SCHEDULED) {
            throw new OperationFailedException("You have not checked in yet");
        }

        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointment.setCheckOutTime(LocalDateTime.now());
        appointment.setPsychologistNote(psychologistNote);
        appointmentRepository.save(appointment);


        // Add notification for student
        notificationService.createAppointmentNotification(
                appointment.getStudent().getUserID(),
                "Appointment Check-out",
                "Your appointment has been checked out. Note: " + psychologistNote,
                appointmentId
        );

        return appointmentMapper.buildAppointmentResponse(
                appointment,
                psychologistMapper.buildPsychologistResponse(
                        Objects.requireNonNull(psychologistRepository.findById(
                                appointment.getPsychologistID()).orElse(null))),
                studentMapper.buildBasicStudentResponse(
                        Objects.requireNonNull(studentRepository.findById(
                                appointment.getStudentID()).orElse(null)))
        );
    }

    public AppointmentResponse submitFeedback(String appointmentId, AppointmentFeedbackRequest request) {
        Appointments appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        if (appointment.getStatus() != AppointmentStatus.COMPLETED) {
            throw new OperationFailedException("Only completed appointments can receive feedback");
        }

        if (appointment.getFeedback() != null && !appointment.getFeedback().isEmpty()) {
            throw new OperationFailedException("Feedback already submitted");
        }

        appointment.setFeedback(request.getFeedback());
        appointment.setRating(request.getRating()); // Lưu rating
        appointmentRepository.save(appointment);

        return appointmentMapper.buildAppointmentResponse(appointment);
    }

    private void handlePsychologistChange(Appointments appointment, TimeSlots oldTimeSlot, TimeSlots newTimeSlot, String appointmentId) {
        Psychologists oldPsychologist = oldTimeSlot.getPsychologist();
        Psychologists newPsychologist = newTimeSlot.getPsychologist();

        Users oldUser = userRepository.findByUserId(oldPsychologist.getUserID())
                .orElseThrow(() -> new ResourceNotFoundException("Old user not found"));
        Users newUser = userRepository.findByUserId(newPsychologist.getUserID())
                .orElseThrow(() -> new ResourceNotFoundException("New User not found"));

        // Send email and notification
        sendPsychologistNotification(oldUser, appointment, "Appointment Transferred", "Your appointment has been transferred to another psychologist.", appointmentId);
        sendPsychologistNotification(newUser, appointment, "New Appointment", "New appointment assigned to you.", appointmentId);
    }

    private void sendPsychologistNotification(Users user, Appointments appointment, String subject, String body, String appointmentId) {
        emailService.sendNewAppointmentEmail(
                user.getEmail(),
                appointment.getPsychologist().getFullNameFromUser(),
                appointment.getStudent(),
                appointment.getAppointmentID(),
                appointment.getTimeSlot(),
                subject
        );
        notificationService.createAppointmentNotification(
                user.getUserId(),
                subject,
                body,
                appointmentId
        );
    }

    private void updateTimeSlotStatus(TimeSlots newTimeSlot, TimeSlots oldTimeSlot) {
        newTimeSlot.setStatus(TimeslotStatus.BOOKED);
        timeSlotRepository.save(newTimeSlot);
        // If old time slot was booked, mark it as available
        if (oldTimeSlot.getStatus() == TimeslotStatus.BOOKED) {
            oldTimeSlot.setStatus(TimeslotStatus.AVAILABLE);
            timeSlotRepository.save(oldTimeSlot);
        }
    }
}