package com.healthy.backend.service;

import com.healthy.backend.dto.psychologist.LeaveRequestDTO;
import com.healthy.backend.dto.psychologist.LeaveResponseDTO;
import com.healthy.backend.dto.psychologist.PsychologistRequest;
import com.healthy.backend.dto.psychologist.PsychologistResponse;
import com.healthy.backend.dto.timeslot.TimeSlotResponse;
import com.healthy.backend.entity.Department;
import com.healthy.backend.entity.LeaveRequest;
import com.healthy.backend.entity.Psychologists;
import com.healthy.backend.entity.TimeSlots;
import com.healthy.backend.exception.ResourceNotFoundException;
import com.healthy.backend.mapper.PsychologistsMapper;
import com.healthy.backend.mapper.TimeSlotMapper;
import com.healthy.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PsychologistService {
    private final PsychologistRepository psychologistRepository;
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final DepartmentRepository departmentRepository;
    private final PsychologistsMapper psychologistsMapper;
    private final TimeSlotMapper timeSlotMapper;
    private final LeaveRequestRepository leaveRequestRepository;


    // Get all psychologist
    public List<PsychologistResponse> getAllPsychologistDTO() {
        List<Psychologists> psychologists = psychologistRepository.findAll();
        return psychologists.stream().map(this::callMapper).toList();
    }

    // Get psychologist by specialization
    public List<PsychologistResponse> getAllPsychologistByDepartment(String departmentID) {

        Department department = departmentRepository.findById(departmentID)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

        if (departmentID.isEmpty()) {
            throw new ResourceNotFoundException("Specialization is required");
        }
        List<Psychologists> psychologists = psychologistRepository.findByDepartmentDepartmentID(departmentID);

        return psychologists.stream()
                .map(psychologistsMapper::buildPsychologistResponse)
                .collect(Collectors.toList());
    }

    // Get psychologist by id
    public PsychologistResponse getPsychologistById(String id) {
        Psychologists psychologist = psychologistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No psychologist found with id" + id));
        return callMapper(psychologist);
    }

    // Update psychologist
    public PsychologistResponse updatePsychologist(String id, PsychologistRequest request) {
        Psychologists psychologist = psychologistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No psychologist found with id " + id));
        if (request.getDepartmentID() == null
                && request.getYearsOfExperience() == null
                && request.getStatus() == null) {
            throw new ResourceNotFoundException("No fields to update");
        }
        // Update fields
        assert request.getDepartmentID() != null;
        if (!request.getDepartmentID().equals(psychologist.getDepartment().getName())) {
            if (!departmentRepository.existsById(request.getDepartmentID())) {
                throw new ResourceNotFoundException("Department not found");
            }
            psychologist.setDepartment(departmentRepository.findById(request.getDepartmentID()).orElseThrow());
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


        List<LeaveRequest> leaves = leaveRequestRepository
                .findByPsychologistPsychologistIDAndStatusAndDateRange(
                        psychologistId,
                        LeaveRequest.Status.Approved,
                        date
                );

        if (!leaves.isEmpty()) {
            throw new ResourceNotFoundException("Psychologist is on leave during this period");
        }

        if (psychologist == null) {
            throw new ResourceNotFoundException("Psychologist not found");
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
    public LeaveResponseDTO createLeaveRequest(LeaveRequestDTO dto) {
        // Validate date range
        if (dto.getStartDate().isAfter(dto.getEndDate())) {
            throw new IllegalArgumentException("Start date must be before end date");
        }

        // Check psychologist exists and is active
        Psychologists psychologist = psychologistRepository.findById(dto.getPsychologistId())
                .orElseThrow(() -> new ResourceNotFoundException("Psychologist not found"));

        if (psychologist.getStatus() != Psychologists.Status.Active) {
            throw new IllegalStateException("Psychologist must be active to request leave");
        }

        // Check for overlapping approved leaves
        List<LeaveRequest> overlappingLeaves = leaveRequestRepository
                .findByPsychologistAndStatusAndDateRange(
                        psychologist,
                        dto.getStartDate(),
                        dto.getEndDate()
                );

        if (!overlappingLeaves.isEmpty()) {
            throw new IllegalStateException("Existing approved leave in this period");
        }

        // Create and save request
        LeaveRequest request = LeaveRequest.builder()
                .leaveRequestID(generateLeaveRequestID())
                .psychologist(psychologist)
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .reason(dto.getReason())
                .status(LeaveRequest.Status.Pending)
                .build();

        LeaveRequest saved = leaveRequestRepository.save(request);
        return convertToDTO(saved);
    }

    public String generateLeaveRequestID() {
        Optional<String> lastLeaveRequestIDOpt = leaveRequestRepository.findLastLeaveRequestID();

        if (lastLeaveRequestIDOpt.isEmpty()) {
            return "LE001";
        }

        String lastLeaveRequestID = lastLeaveRequestIDOpt.get();

        int lastNumber = Integer.parseInt(lastLeaveRequestID.substring(2));

        // Tăng số thứ tự lên 1
        int newNumber = lastNumber + 1;

        String newLeaveRequestID = String.format("LE%03d", newNumber);

        return newLeaveRequestID;
    }

    public List<LeaveResponseDTO> getPendingRequests() {
        return leaveRequestRepository.findByStatus(LeaveRequest.Status.Pending)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }



    public LeaveResponseDTO processLeaveRequest(String requestId, boolean approve) {
        LeaveRequest request = leaveRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found"));

        if (approve) {
            request.setStatus(LeaveRequest.Status.Approved);
            Psychologists psychologist = request.getPsychologist();
            psychologist.setStatus(Psychologists.Status.OnLeave);
            psychologistRepository.save(psychologist);
        } else {
            request.setStatus(LeaveRequest.Status.Rejected);
        }

        LeaveRequest updated = leaveRequestRepository.save(request);
        return convertToDTO(updated);
    }



    private LeaveResponseDTO convertToDTO(LeaveRequest request) {
        return LeaveResponseDTO.builder()
                .requestId(request.getLeaveRequestID())
                .psychologistName(request.getPsychologist().getFullNameFromUser())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .reason(request.getReason())
                .status(request.getStatus().name())
                .department(request.getPsychologist().getDepartment().getName())
                .createdAt(request.getCreatedAt())
                .build();
    }
    public List<LeaveResponseDTO> getLeaveRequestsByPsychologist(String psychologistId) {
        List<LeaveRequest> requests = leaveRequestRepository.findByPsychologistPsychologistID(psychologistId);
        return requests.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }



    public void updatePsychologistStatusBasedOnLeaveRequests() {
        LocalDate today = LocalDate.now();

        // Lấy tất cả các psychologist có leave request approved
        List<Psychologists> psychologists = psychologistRepository.findAll();

        for (Psychologists psychologist : psychologists) {
            List<LeaveRequest> approvedLeaves = leaveRequestRepository
                    .findByPsychologistPsychologistIDAndStatus(
                            psychologist.getPsychologistID(),
                            LeaveRequest.Status.Approved
                    );

            boolean isOnLeave = approvedLeaves.stream()
                    .anyMatch(leave -> !today.isBefore(leave.getStartDate()) && !today.isAfter(leave.getEndDate()));

            if (isOnLeave) {
                psychologist.setStatus(Psychologists.Status.OnLeave);
            } else {
                psychologist.setStatus(Psychologists.Status.Active);
            }

            psychologistRepository.save(psychologist);
        }
    }




}
