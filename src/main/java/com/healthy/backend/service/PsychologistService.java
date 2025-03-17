package com.healthy.backend.service;


import com.healthy.backend.dto.psychologist.DepartmentResponse;
import com.healthy.backend.dto.psychologist.PsychologistRequest;
import com.healthy.backend.dto.psychologist.PsychologistResponse;
import com.healthy.backend.dto.timeslot.DefaultTimeSlotResponse;
import com.healthy.backend.dto.timeslot.TimeSlotResponse;
import com.healthy.backend.entity.DefaultTimeSlot;
import com.healthy.backend.entity.Psychologists;
import com.healthy.backend.entity.TimeSlots;
import com.healthy.backend.enums.PsychologistStatus;
import com.healthy.backend.enums.TimeslotStatus;
import com.healthy.backend.exception.AuthorizeException;
import com.healthy.backend.exception.ResourceNotFoundException;
import com.healthy.backend.mapper.DepartmentMapper;
import com.healthy.backend.mapper.PsychologistsMapper;
import com.healthy.backend.mapper.TimeSlotMapper;
import com.healthy.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PsychologistService {
    private final UserRepository userRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final DepartmentRepository departmentRepository;
    private final AppointmentRepository appointmentRepository;
    private final PsychologistRepository psychologistRepository;
    private final DefaultTimeSlotRepository defaultTimeSlotRepository;

    private final PsychologistsMapper psychologistsMapper;
    private final DepartmentMapper departmentMapper;
    private final TimeSlotMapper timeSlotMapper;


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
    public List<DepartmentResponse> getAllDepartments() {
        return departmentRepository.findAll()
                .stream()
                .map(departmentMapper::buildDepartmentResponse)
                .collect(Collectors.toList());
    }

    // Get psychologist by specialization
    public List<PsychologistResponse> getAllPsychologistByDepartment(String departmentID) {
        if (departmentID == null || departmentID.isEmpty()) {
            throw new IllegalArgumentException("Department ID is required");
        }

        if (!departmentRepository.existsById(departmentID)) {
            throw new ResourceNotFoundException("Department not found");
        }

        List<Psychologists> psychologists = psychologistRepository.findByDepartmentDepartmentID(departmentID);
        return psychologists.stream()
                .map(psychologistsMapper::buildPsychologistResponse)
                .collect(Collectors.toList());
    }

    // Get psychologist by id
    public PsychologistResponse getPsychologistById(String id) {
        Psychologists psychologist = psychologistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No psychologist found with id " + id));
        return callMapper(psychologist);
    }


    public PsychologistResponse updatePsychologist(String id, PsychologistRequest request, String currentUserId) {
        Psychologists psychologist = psychologistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No psychologist found with id " + id));

        if (!psychologist.getUserID().equals(currentUserId)) {
            throw new AuthorizeException("Unauthorized update");
        }

        if (request.getDepartmentID() == null
                && request.getYearsOfExperience() == null) {
            throw new IllegalArgumentException("No fields to update");
        }
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


    // Call psychologistResponse Mapper
    private PsychologistResponse callMapper(Psychologists psychologist) {
        return psychologistsMapper.buildPsychologistResponse(psychologist,
                appointmentRepository.findByPsychologistID(psychologist.getPsychologistID()),
                userRepository.findById(psychologist.getUserID()).orElseThrow());
    }

    // Check if status is valid
    @SuppressWarnings("unused")
    private boolean isValidStatus(String status) {
        try {
            PsychologistStatus.valueOf(status);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
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
            String studentId,
            boolean isStudent) {

        List<TimeSlots> slots;

        if (psychologistId != null) {
            slots = (date != null)
                    ? timeSlotRepository.findByPsychologistIdAndDate(psychologistId, date)
                    : timeSlotRepository.findByPsychologistId(psychologistId);
        } else {
            slots = timeSlotRepository.findAll();
        }
        if(isStudent) {
            return slots.stream()
                    .filter(slot -> Set.of(TimeslotStatus.UNAVAILABLE, TimeslotStatus.PROGRAM).contains(slot.getStatus()))
                    .map(slot -> {
                        TimeSlotResponse response = timeSlotMapper.toResponse(slot);
                        response.setBooked(appointmentRepository.existsByStudentIDAndTimeSlotsID(studentId, slot.getTimeSlotsID()));
                        return response;
                    })
                    .toList();
        }
        return slots.stream()
                .map(timeSlotMapper::toResponse)
                .toList();
    }
}
