package com.healthy.backend.service;

import com.healthy.backend.dto.appointment.AppointmentResponse;
import com.healthy.backend.dto.psychologist.PsychologistRequest;
import com.healthy.backend.dto.psychologist.PsychologistResponse;
import com.healthy.backend.dto.student.StudentResponse;
import com.healthy.backend.dto.timeslot.PsychologistAvailabilityResponse;
import com.healthy.backend.dto.timeslot.TimeSlotResponse;
import com.healthy.backend.dto.user.UsersRequest;
import com.healthy.backend.dto.user.UsersResponse;
import com.healthy.backend.entity.Appointments;
import com.healthy.backend.entity.Psychologists;
import com.healthy.backend.entity.TimeSlots;
import com.healthy.backend.entity.Users;
import com.healthy.backend.exception.ResourceNotFoundException;
import com.healthy.backend.mapper.PsychologistsMapper;
import com.healthy.backend.mapper.TimeSlotMapper;
import com.healthy.backend.repository.AppointmentRepository;
import com.healthy.backend.repository.PsychologistRepository;
import com.healthy.backend.repository.TimeSlotRepository;
import com.healthy.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PsychologistService {

    public final PsychologistRepository psychologistRepository;
    public final AppointmentRepository appointmentRepository;
    public final UserRepository userRepository;
    public final TimeSlotRepository timeSlotRepository;
    public final PsychologistsMapper psychologistsMapper;
    public final TimeSlotMapper timeSlotMapper;

    public List<PsychologistResponse> getAllPsychologistDTO() {
        List<Psychologists> psychologists = psychologistRepository.findAll();

        return psychologists.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    public PsychologistResponse getPsychologistById(String id) {
        Psychologists psychologist = psychologistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No psychologist found with id" + id));
        return convert(psychologist);
    }

    public PsychologistResponse convert(Psychologists psychologist) {
        List<Appointments> appointments = appointmentRepository.findByPsychologistID(psychologist.getPsychologistID());
        Users users = userRepository.findById(psychologist.getUserID())
                .orElseThrow(() -> new ResourceNotFoundException("No user found with psychologistID"));
        return
                PsychologistResponse.builder()
                        .psychologistId(psychologist.getPsychologistID())
                        .status(psychologist.getStatus().name())
                        .specialization(psychologist.getSpecialization())
                        .yearsOfExperience(psychologist.getYearsOfExperience())
                        .usersResponse(UsersResponse.builder()
                                .fullName(users.getFullName())
                                .username(users.getUsername())
                                .phoneNumber(users.getPhoneNumber())
                                .email(users.getEmail())
                                .gender(users.getGender().toString())
                                .build())
                        .appointment(
                                appointments.isEmpty()
                                        ? Collections.emptyList() : appointments.stream()
                                        .map(a -> AppointmentResponse.builder()
                                                .appointmentID(a.getAppointmentID())
                                                .CreatedAt(a.getCreatedAt())
                                                .Status(a.getStatus().name())
                                                .studentResponse(
                                                        StudentResponse.builder()
                                                                .studentId(a.getStudentID())
                                                                .grade(a.getStudent().getGrade())
                                                                .className(a.getStudent().getClassName())
                                                                .schoolName(a.getStudent().getSchoolName())
                                                                .depressionScore(a.getStudent().getDepressionScore())
                                                                .anxietyScore(a.getStudent().getAnxietyScore())
                                                                .stressScore(a.getStudent().getStressScore())
                                                                .build()
                                                )
                                                .Text(a.getNotes())
                                                .timeSlotID(a.getTimeSlotsID())
                                                .UpdatedAt(a.getUpdatedAt()).build()
                                        )
                                        .collect(Collectors.toList())
                        )
                        .build();
    }
    // Update psychologist
    public PsychologistResponse updatePsychologist(String id, PsychologistRequest request) {
        Psychologists psychologist = psychologistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No psychologist found with id " + id));

        if (request.getSpecialization() != null) {
            psychologist.setSpecialization(request.getSpecialization());
        }
        if (request.getYearsOfExperience() != null) {
            psychologist.setYearsOfExperience(request.getYearsOfExperience());
        }
        if (request.getStatus() != null) {
            psychologist.setStatus(Psychologists.Status.valueOf(request.getStatus()));
        }
        psychologistRepository.save(psychologist);

        return convert(psychologist);
    }

    // Get available time slots
    public List<TimeSlots> getTimeSlots(LocalDate date, String psychologistId) {
        Psychologists psychologist = psychologistRepository.findById(psychologistId)
                .orElseThrow(() -> new ResourceNotFoundException("Psychologist not found"));
        if (psychologist == null) {
            throw new ResourceNotFoundException("Psychologist not found");
        }
        if (psychologist.getStatus() != Psychologists.Status.Active) {
            throw new ResourceNotFoundException("Psychologist is not Active");
        }
        return timeSlotRepository.findBySlotDateAndPsychologist(date, psychologist);
    }

    // Create default time slots
    public boolean createDefaultTimeSlots(LocalDate date, String psychologistId) {
        Psychologists psychologist = psychologistRepository.findById(psychologistId)
                .orElseThrow(() -> new ResourceNotFoundException("Psychologist not found"));

        List<TimeSlots> existingSlots = timeSlotRepository.findBySlotDateAndPsychologist(date, psychologist);
        if (!existingSlots.isEmpty()) {
            return false;
        }

        LocalTime start = LocalTime.of(8, 0); // Morning shift: 8h - 11h
        LocalTime noonBreakStart = LocalTime.of(11, 30); //
        LocalTime noonBreakEnd = LocalTime.of(12, 30); // Afternoon shift: 13h - 17h
        LocalTime end = LocalTime.of(17, 0);
        List<TimeSlots> timeSlots = new ArrayList<>(generateTimeSlots(date, start, noonBreakStart, noonBreakEnd, end, psychologist));
        timeSlotRepository.saveAll(timeSlots);
        return true;
    }

    private List<TimeSlots> generateTimeSlots(
            LocalDate date, LocalTime start, LocalTime end,
            LocalTime noonBreakStart, LocalTime noonBreakEnd, Psychologists psychologist) {
        List<TimeSlots> timeSlots = new ArrayList<>();
        LocalTime currentTime = start;
        int index = 0;
        while (currentTime.isBefore(end)) {
            if (currentTime.isAfter(noonBreakStart) && currentTime.isBefore(noonBreakEnd)) {
                currentTime = currentTime.plusMinutes(30);
                continue;
            }
            LocalTime nextTime = currentTime.plusMinutes(30);
            timeSlots.add(new TimeSlots(date, currentTime, nextTime, psychologist, index++));
            currentTime = nextTime;
        }

        return timeSlots;
    }
}
