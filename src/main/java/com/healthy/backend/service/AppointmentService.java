package com.healthy.backend.service;

import com.healthy.backend.dto.appointment.AppointmentRequest;
import com.healthy.backend.dto.appointment.AppointmentResponse;
import com.healthy.backend.dto.appointment.AppointmentUpdateRequest;
import com.healthy.backend.dto.psychologist.DepartmentResponse;
import com.healthy.backend.entity.*;
import com.healthy.backend.entity.Enum.StatusEnum;
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

    private final AppointmentRepository appointmentRepository;

    private final StudentRepository studentRepository;

    private final PsychologistRepository psychologistRepository;

    private final TimeSlotRepository timeSlotRepository;

    private final DepartmentRepository departmentRepository;

    private final UserRepository userRepository;

    private final EmailService emailService;

    private final AppointmentMapper appointmentMapper;

    private final StudentMapper studentMapper;

    private final UserMapper userMapper;

    private final PsychologistsMapper psychologistMapper;

    private final DepartmentMapper departmentMapper;

    private final NotificationService notificationService;

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
        if (timeSlot.getStatus() != TimeSlots.Status.Available) {
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
        appointment.setAppointmentID(generateAppointmentId());
        appointment.setTimeSlotsID(timeSlot.getTimeSlotsID());
        appointment.setStudentID(student.getStudentID());
        appointment.setPsychologistID(psychologist.getPsychologistID());
        appointment.setPsychologist(psychologist);
        appointment.setStudent(student);
        appointment.setTimeSlot(timeSlot);
        appointment.setStatus(StatusEnum.Scheduled);

        // Save appointment and update time slot status
        Appointments savedAppointment = appointmentRepository.save(appointment);
        timeSlot.setStatus(TimeSlots.Status.Booked);
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

        if (appointment.getStatus() == StatusEnum.InProgress) {
            throw new ResourceInvalidException("Can not cancel an appointment that is In Progress");
        }
        if (appointment.getStatus() == StatusEnum.Completed) {
            throw new ResourceInvalidException("Appointment is already completed");
        }
        // Update status
        appointment.setStatus(StatusEnum.Cancelled);
        appointmentRepository.save(appointment);
        // Revert time slot back to available
        timeSlot.setStatus(TimeSlots.Status.Available);
        timeSlotRepository.save(timeSlot);

        return appointmentMapper.buildAppointmentResponse(appointment);
    }


    public AppointmentResponse updateAppointment(String appointmentId, AppointmentUpdateRequest request) {
        Appointments appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot find appointment with id " + appointmentId));

        // Check appointment status
        if (appointment.getStatus() != StatusEnum.Scheduled) {
            throw new ResourceInvalidException("You can only update a scheduled appointment");
        }

        if (!request.getTimeSlotId().equals(appointment.getTimeSlotsID())) {
            TimeSlots newTimeSlot = timeSlotRepository.findByIdWithPsychologist(request.getTimeSlotId())
                    .orElseThrow(() -> new ResourceNotFoundException("Cannot find time slot with id" + request.getTimeSlotId()));

            if (newTimeSlot.getStatus() == TimeSlots.Status.Booked) {
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
            newTimeSlot.setStatus(TimeSlots.Status.Booked);
            timeSlotRepository.save(newTimeSlot);

            if (oldTimeSlot.getStatus() == TimeSlots.Status.Booked) {
                oldTimeSlot.setStatus(TimeSlots.Status.Available);
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

        if (appointment.getStatus() == StatusEnum.Cancelled) {
            throw new OperationFailedException("Appointment is cancelled");
        }

        if (appointment.getStatus() == StatusEnum.Completed) {
            throw new OperationFailedException("Appointment is already completed");
        }

        if (appointment.getStatus() == StatusEnum.InProgress) {
            throw new OperationFailedException("Appointment is already in progress");
        }


        appointment.setStatus(StatusEnum.InProgress);
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
    public AppointmentResponse checkOut(String appointmentId) {
        Appointments appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + appointmentId));

        if (appointment.getStatus() == StatusEnum.Cancelled) {
            throw new OperationFailedException("Appointment is cancelled");
        }

        if (appointment.getStatus() == StatusEnum.Completed) {
            throw new OperationFailedException("Appointment is already completed");
        }

        if (appointment.getStatus() == StatusEnum.Scheduled) {
            throw new OperationFailedException("You have not checked in yet");
        }

        appointment.setStatus(StatusEnum.Completed);
        appointment.setCheckOutTime(LocalDateTime.now());
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

    // Generate appointment id
    private String generateAppointmentId() {
        String lastCode = appointmentRepository.findLastAppointmentId();
        if (lastCode == null || lastCode.length() < 3) { // APP
            throw new IllegalArgumentException("Invalid last appointment code");
        }
        String prefix = lastCode.substring(0, 3);
        int number = Integer.parseInt(lastCode.substring(3));
        return prefix + String.format("%03d", number + 1);
    }
}