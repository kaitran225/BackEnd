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

    public AppointmentResponse bookAppointment(AppointmentRequest request) {
        TimeSlots timeSlot = timeSlotRepository.findByIdWithPsychologist(request.getTimeSlotId())
                .orElseThrow(() -> new ResourceNotFoundException("Timeslot not found with id: " + request.getTimeSlotId()));

        // Validate time slot
        if (timeSlot.getStatus() == TimeslotStatus.BOOKED) {
            throw new ResourceAlreadyExistsException("Time slot is not available");
        }

        // Validate student and psychologist
        Students student = studentRepository.findByUserID(request.getUserId());
        if (student == null) {
            throw new ResourceNotFoundException("Student not found with ID: " + request.getUserId());
        }

        Psychologists psychologist = psychologistRepository.findById(timeSlot.getPsychologist().getPsychologistID())
                .orElseThrow(() -> new ResourceNotFoundException("Psychologist not found with ID: " + timeSlot.getPsychologist().getPsychologistID()));

        // Create new appointment
        Appointments appointment = new Appointments();
        appointment.setAppointmentID(__.generateAppointmentId());
        appointment.setTimeSlotsID(timeSlot.getTimeSlotsID());
        appointment.setStudentID(student.getStudentID());
        appointment.setPsychologistID(psychologist.getPsychologistID());
        appointment.setPsychologist(psychologist);
        appointment.setStudent(student);
        appointment.setTimeSlot(timeSlot);
        appointment.setStatus(AppointmentStatus.SCHEDULED);

        // Save appointment and update time slot status
        Appointments savedAppointment = appointmentRepository.save(appointment);
        timeSlot.setStatus(TimeslotStatus.BOOKED);
        timeSlotRepository.save(timeSlot);

        // Lấy thông tin user của psychologist
        Users psychologistUser = userRepository.findByUserId(psychologist.getUserID())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Gửi email thông báo cho psychologist
        if (psychologistUser.getEmail() != null) {
            emailService.sendNotificationEmail(
                    psychologistUser.getEmail(),
                    "New Appointment Booked",
                    emailService.getNewAppointmentMailBody(
                            savedAppointment.getPsychologist().getFullNameFromUser(),
                            savedAppointment.getStudent(),
                            savedAppointment.getAppointmentID(),
                            savedAppointment.getTimeSlot()
                    )
            );
        }
        // Tạo notification cho psychologist
        notificationService.createAppointmentNotification(
                psychologistUser.getUserId(),
                "New Appointment Booked",
                "You have a new appointment with " + student.getUser().getFullName(),
                savedAppointment.getAppointmentID()
        );

        // Map sang DTO và trả về response
        return appointmentMapper.buildAppointmentResponse(
                savedAppointment,
                psychologistMapper.buildPsychologistResponse(psychologist),
                studentMapper.buildBasicStudentResponse(student)
        );
    }

    // Cancel
    public AppointmentResponse cancelAppointment(String appointmentId) {

        Appointments appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + appointmentId));
        TimeSlots timeSlot = timeSlotRepository.findById(appointment.getTimeSlotsID()).orElseThrow(
                () -> new ResourceNotFoundException("Timeslot not found with id: " + appointment.getTimeSlotsID())
        );

        if (appointment.getStatus() == AppointmentStatus.IN_PROGRESS) {
            throw new ResourceInvalidException("Can not cancel an appointment that is In Progress");
        }
        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new ResourceInvalidException("Appointment is already completed");
        }
        // Update status
        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);
        // Revert time slot back to available
        timeSlot.setStatus(TimeslotStatus.AVAILABLE);
        timeSlotRepository.save(timeSlot);

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

            // Lấy thông tin psychologist cũ và mới
            String oldPsychId = appointment.getPsychologistID();
            String newPsychId = newTimeSlot.getPsychologist().getPsychologistID();

            // Xử lý khi chuyển sang psychologist khác
            if (!oldPsychId.equals(newPsychId)) {
                Psychologists oldPsychologist = psychologistRepository.findById(oldPsychId)
                        .orElseThrow(() -> new ResourceNotFoundException("Old psychologist not found"));
                Psychologists newPsychologist = newTimeSlot.getPsychologist();

                // Lấy thông tin user
                Users oldUser = userRepository.findByUserId(oldPsychologist.getUserID()).orElseThrow(
                        () -> new ResourceNotFoundException("Old user not found")
                );
                Users newUser = userRepository.findByUserId(newPsychologist.getUserID()).orElseThrow(
                        () -> new ResourceNotFoundException("New User not found")
                );

                // Gửi thông báo cho psychologist cũ
                if (oldUser.isPresent()) {
                    emailService.sendNotificationEmail(
                            oldUser.getEmail(),
                            "New Appointment Booked",
                            emailService.getAppointmentTransferredMailBody(
                                    appointment.getPsychologist().getFullNameFromUser(),
                                    appointment.getStudent(),
                                    appointment.getAppointmentID(),
                                    appointment.getTimeSlot()
                            )
                    );

                    notificationService.createAppointmentNotification(
                            oldUser.getUserId(),
                            "Appointment Transferred",
                            "Your appointment has been transferred to another psychologist.",
                            appointmentId
                    );
                }

                // Gửi thông báo cho psychologist mới
                if (newUser.isPresent()) {
                    emailService.sendNotificationEmail(
                            newUser.getEmail(),
                            "New Appointment Booked",
                            emailService.getNewAppointmentMailBody(
                                    appointment.getPsychologist().getFullNameFromUser(),
                                    appointment.getStudent(),
                                    appointment.getAppointmentID(),
                                    appointment.getTimeSlot()
                            )
                    );
                    notificationService.createAppointmentNotification(
                            newUser.getUserId(),
                            "New Appointment",
                            "New appointment assigned to you.",
                            appointmentId
                    );
                }
            }

            // Cập nhật trạng thái time slot
            newTimeSlot.setStatus(TimeslotStatus.BOOKED);
            timeSlotRepository.save(newTimeSlot);

            if (oldTimeSlot.getStatus() == TimeslotStatus.BOOKED) {
                oldTimeSlot.setStatus(TimeslotStatus.AVAILABLE);
                timeSlotRepository.save(oldTimeSlot);
            }

            appointment.setTimeSlotsID(newTimeSlot.getTimeSlotsID());
            appointment.setPsychologistID(newPsychId);
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
}