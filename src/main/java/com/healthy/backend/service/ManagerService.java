package com.healthy.backend.service;

import com.healthy.backend.dto.manager.AppointmentStatsResponse;
import com.healthy.backend.dto.manager.PsychologistStatsResponse;
import com.healthy.backend.entity.Appointments;
import com.healthy.backend.entity.Psychologists;
import com.healthy.backend.enums.AppointmentStatus;
import com.healthy.backend.repository.AppointmentRepository;
import com.healthy.backend.repository.PsychologistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ManagerService {

    private final AppointmentRepository appointmentRepository;
    private final PsychologistRepository psychologistRepository;

    // Method to get appointment statistics
    public AppointmentStatsResponse getAppointmentStats() {
        List<Appointments> appointments = appointmentRepository.findAll();
        Map<String, Long> appointmentCounts = appointments.stream()
                .collect(Collectors.groupingBy(appointment -> appointment.getStatus().name(), Collectors.counting()));

        AppointmentStatsResponse response = new AppointmentStatsResponse();
        response.setAppointmentCounts(appointmentCounts);
        return response;
    }

    // Method to get psychologist statistics
    public List<PsychologistStatsResponse> getPsychologistStats() {
        List<Psychologists> psychologists = psychologistRepository.findAll();

        return psychologists.stream().map(psychologist -> {
            long appointmentCount = appointmentRepository.countByPsychologistID(psychologist.getPsychologistID());
            double averageRating = calculateAverageRating(psychologist.getPsychologistID());

            PsychologistStatsResponse statsResponse = new PsychologistStatsResponse();
            statsResponse.setPsychologistId(psychologist.getPsychologistID());
            statsResponse.setFullName(psychologist.getFullNameFromUser ());
            statsResponse.setAverageRating(averageRating);
            statsResponse.setAppointmentCount(appointmentCount);
            return statsResponse;
        }).collect(Collectors.toList());
    }

    // Helper method to calculate average rating
    private double calculateAverageRating(String psychologistId) {
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