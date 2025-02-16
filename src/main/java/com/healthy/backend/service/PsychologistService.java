package com.healthy.backend.service;

import com.healthy.backend.dto.psychologist.PsychologistRequest;
import com.healthy.backend.dto.psychologist.PsychologistResponse;
import com.healthy.backend.dto.timeslot.TimeSlotResponse;
import com.healthy.backend.entity.Psychologists;
import com.healthy.backend.entity.TimeSlots;
import com.healthy.backend.exception.ResourceNotFoundException;
import com.healthy.backend.mapper.PsychologistsMapper;
import com.healthy.backend.mapper.TimeSlotMapper;
import com.healthy.backend.repository.AppointmentRepository;
import com.healthy.backend.repository.PsychologistRepository;
import com.healthy.backend.repository.TimeSlotRepository;
import com.healthy.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ArrayList;

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
        return psychologists.stream().map(this::callMapper).toList();
    }

    public PsychologistResponse getPsychologistById(String id) {
        Psychologists psychologist = psychologistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No psychologist found with id" + id));
       return callMapper(psychologist);
    }

    // Update psychologist
    public PsychologistResponse updatePsychologist(String id, PsychologistRequest request) {
        Psychologists psychologist = psychologistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No psychologist found with id " + id));
        if (request.getSpecialization() == null
                && request.getYearsOfExperience() == null
                && request.getStatus() == null) {
            throw new ResourceNotFoundException("No fields to update");
        }
        // Update fields
        assert request.getSpecialization() != null;
        if (!request.getSpecialization().equals(psychologist.getSpecialization())) {
            psychologist.setSpecialization(request.getSpecialization());
        }
        assert request.getYearsOfExperience() != null;
        if (!request.getYearsOfExperience().equals(psychologist.getYearsOfExperience())) {
            psychologist.setYearsOfExperience(request.getYearsOfExperience());
        }
        assert request.getStatus() != null;
        if (!request.getStatus().equals(psychologist.getStatus().name())) {
            if (!isValidStatus(request.getStatus()))
                throw new ResourceNotFoundException("Status is not valid");
            psychologist.setStatus(Psychologists.Status.valueOf(request.getStatus()));
        }

        psychologistRepository.save(psychologist);

        return callMapper(psychologist);
    }

    // Get available time slots
    public List<TimeSlotResponse> getTimeSlots(LocalDate date, String psychologistId) {
        Psychologists psychologist = psychologistRepository.findById(psychologistId)
                .orElseThrow(() -> new ResourceNotFoundException("Psychologist not found"));
        if (psychologist == null) {
            throw new ResourceNotFoundException("Psychologist not found");
        }
        if (psychologist.getStatus() != Psychologists.Status.Active) {
            throw new ResourceNotFoundException("Psychologist is not Active");
        }
        List<TimeSlots> timeSlots = timeSlotRepository.findBySlotDateAndPsychologist(date, psychologist);
        return timeSlotMapper.buildResponse(timeSlots);
    }

    // Create default time slots
    public List<TimeSlotResponse> createDefaultTimeSlots(LocalDate date, String psychologistId) {
        Psychologists psychologist = psychologistRepository.findById(psychologistId)
                .orElseThrow(() -> new ResourceNotFoundException("Psychologist not found"));

        List<TimeSlots> existingSlots = timeSlotRepository.findBySlotDateAndPsychologist(date, psychologist);
        if (!existingSlots.isEmpty()) {
            throw new RuntimeException("Time slots already exist");
        }

        LocalTime start = LocalTime.of(8, 0); // Morning shift: 8h - 11h
        LocalTime noonBreakStart = LocalTime.of(11, 30); //
        LocalTime noonBreakEnd = LocalTime.of(12, 30); // Afternoon shift: 13h - 17h
        LocalTime end = LocalTime.of(17, 0);

        List<TimeSlots> timeSlots = new ArrayList<>(generateTimeSlots(date, start, noonBreakStart, noonBreakEnd, end, psychologist));
        timeSlotRepository.saveAll(timeSlots);
        return timeSlotMapper.buildResponse(timeSlots);
    }

    // Generate time slots
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

    // call psychologistResponse Mapper
    private PsychologistResponse callMapper(Psychologists psychologist) {
        return psychologistsMapper.buildPsychologistResponse(psychologist,
                appointmentRepository.findByPsychologistID(psychologist.getPsychologistID()),
                userRepository.findById(psychologist.getUserID()).orElseThrow());
    }

    // Check if status is valid
    private boolean isValidStatus(String status) {
        try {
            Psychologists.Status.valueOf(status);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
