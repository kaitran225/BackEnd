package com.healthy.backend.service;

import com.healthy.backend.dto.manager.AppointmentStatsResponse;
import com.healthy.backend.dto.manager.ManagerDashboardResponse;
import com.healthy.backend.dto.manager.PsychologistStatsResponse;
import com.healthy.backend.entity.*;
import com.healthy.backend.enums.AppointmentStatus;
import com.healthy.backend.enums.ParticipationStatus;
import com.healthy.backend.enums.SurveyCategory;
import com.healthy.backend.repository.*;
import com.healthy.backend.stats.AppointmentStats;
import com.healthy.backend.stats.DepartmentStats;
import com.healthy.backend.stats.ProgramStats;
import com.healthy.backend.stats.SurveyStats;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.chrono.ChronoLocalDate;
import java.time.temporal.IsoFields;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ManagerService {

    private final AppointmentRepository appointmentRepository;
    private final PsychologistRepository psychologistRepository;
    private final SurveyResultRepository surveyResultRepository;
    private final SurveyRepository surveyRepository;
    private final ProgramParticipationRepository programParticipationRepository;
    private final StudentRepository studentRepository;
    private final TagsRepository tagsRepository;

    public ManagerDashboardResponse getDashboardStats(String filter, Integer value) {
        LocalDate[] dateRange = resolveDateRange(filter, value);
        return new ManagerDashboardResponse(
                calculateSurveyStats(dateRange),
                calculateProgramStats(dateRange),
                calculateAppointmentStats(dateRange),
                calculateDepartmentStats(dateRange)
        );
    }

    private SurveyStats calculateSurveyStats(LocalDate[] dateRange) {
        Map<String, Double> rates = new HashMap<>();

        // Convert LocalDate to LocalDateTime
        LocalDateTime startDateTime = dateRange[0] != null ? dateRange[0].atStartOfDay() : null;
        LocalDateTime endDateTime = dateRange[1] != null ? dateRange[1].atTime(23, 59, 59) : null;

        // Count survey results by category within date range
        Map<String, Long> resultCountsByCategory = new HashMap<>();
        long totalResults = 0;

        // Get all surveys with their categories
        List<Surveys> allSurveys = surveyRepository.findAll();
        Map<String, SurveyCategory> surveyCategories = allSurveys.stream()
                .collect(Collectors.toMap(Surveys::getSurveyID, Surveys::getCategory));

        for (SurveyCategory category : SurveyCategory.values()) {
            resultCountsByCategory.put(category.name(), 0L);
        }
        List<SurveyResult> allResults;
        if (startDateTime != null && endDateTime != null) {
            allResults = surveyResultRepository.findAll().stream()
                    .filter(result -> !result.getCompletionDate().isBefore((startDateTime)) &&
                            !result.getCompletionDate().isAfter((endDateTime)))
                    .collect(Collectors.toList());
        } else {
            allResults = surveyResultRepository.findAll();
        }

        // Count results by category
        for (SurveyResult result : allResults) {
            String surveyId = result.getSurveyID();
            SurveyCategory category = surveyCategories.get(surveyId);

            if (category != null) {
                String categoryName = category.name();
                resultCountsByCategory.put(categoryName, resultCountsByCategory.get(categoryName) + 1);
                totalResults++;
            }
        }

        // Calculate percentages
        for (SurveyCategory category : SurveyCategory.values()) {
            String categoryName = category.name();
            long count = resultCountsByCategory.get(categoryName);
            double percentage = totalResults > 0 ? (count * 100.0) / totalResults : 0;
            rates.put(categoryName, percentage);
        }

        SurveyStats stats = new SurveyStats();
        stats.setSurveyParticipationRates(rates);
        return stats;
    }

    private ProgramStats calculateProgramStats(LocalDate[] dateRange) {
        Map<String, Double> rates = new HashMap<>();

        // Chuyển đổi LocalDate thành LocalDateTime
        LocalDateTime startDateTime = dateRange[0] != null ? dateRange[0].atStartOfDay() : null;
        LocalDateTime endDateTime = dateRange[1] != null ? dateRange[1].atTime(23, 59, 59) : null;

        // Chuyển đổi LocalDateTime thành LocalDate
        LocalDate startDate = startDateTime != null ? startDateTime.toLocalDate() : null;
        LocalDate endDate = endDateTime != null ? endDateTime.toLocalDate() : null;

        // Lấy tất cả các tags
        List<Tags> tags = tagsRepository.findAll();
        long totalStudents = studentRepository.count();

        // Tính toán tỷ lệ tham gia cho từng tag
        tags.forEach(tag -> {
            // Lấy tất cả các chương trình có tag này
            Set<Programs> programs = tag.getPrograms();

            // Tính tổng số học sinh tham gia các chương trình có tag này
            long participants = programs.stream()
                    .mapToLong(program -> programParticipationRepository.countByProgramAndStatusAndDateRange(
                            program.getProgramID(),
                            ParticipationStatus.JOINED,
                            startDate,
                            endDate
                    ))
                    .sum();

            // Tính tỷ lệ phần trăm
            double rate = totalStudents > 0 ?
                    (participants * 100.0) / totalStudents : 0;

            rates.put(tag.getTagName(), rate);
        });

        ProgramStats stats = new ProgramStats();
        stats.setProgramParticipationRates(rates);
        return stats;
    }

    // Method to get appointment statistics
    public AppointmentStatsResponse getAppointmentStats() {
        List<Appointments> appointments = appointmentRepository.findAll();
        Map<String, Long> appointmentCounts = appointments.stream()
                .collect(Collectors.groupingBy(appointment -> appointment.getStatus().name(), Collectors.counting()));

        AppointmentStatsResponse response = new AppointmentStatsResponse();
        response.setAppointmentCounts(appointmentCounts);
        return response;
    }

    public List<PsychologistStatsResponse> getPsychologistStats() {
        List<Psychologists> psychologists = psychologistRepository.findAll();
        List<Appointments> allAppointments = appointmentRepository.findAll();

        List<PsychologistStatsResponse> responses = new ArrayList<>();
        for (Psychologists psychologist : psychologists) {
            // Filter appointments belonging to the current psychologist
            List<Appointments> filteredAppointments = allAppointments.stream()
                    .filter(a -> psychologist.getPsychologistID().equals(a.getPsychologistID()))
                    .toList();

            long appointmentCount = filteredAppointments.size();
            double averageRating = filteredAppointments.stream()
                    .filter(a -> a.getRating() != null)
                    .mapToInt(Appointments::getRating)
                    .average()
                    .orElse(0.0);

            PsychologistStatsResponse statResponse = new PsychologistStatsResponse();
            statResponse.setPsychologistId(psychologist.getPsychologistID());
            statResponse.setFullName(psychologist.getFullNameFromUser());
            statResponse.setAppointmentCount(appointmentCount);
            statResponse.setAverageRating(averageRating);
            responses.add(statResponse);
        }
        return responses;
    }


    private LocalDate[] resolveDateRange(String filter, Integer value) {
        LocalDate now = LocalDate.now();
        if (filter == null) return new LocalDate[]{null, null};

        return switch (filter.toLowerCase()) {
            case "week" -> {
                int week = value != null ? value : now.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
                LocalDate start = now.with(IsoFields.WEEK_OF_WEEK_BASED_YEAR, week).with(DayOfWeek.MONDAY);
                yield new LocalDate[]{start, start.plusDays(6)};
            }
            case "month" -> {
                int month = value != null ? value : now.getMonthValue();
                YearMonth yearMonth = YearMonth.of(now.getYear(), month);
                yield new LocalDate[]{yearMonth.atDay(1), yearMonth.atEndOfMonth()};
            }
            case "year" -> {
                int year = value != null ? value : now.getYear();
                yield new LocalDate[]{LocalDate.of(year, 1, 1), LocalDate.of(year, 12, 31)};
            }
            default -> new LocalDate[]{null, null};
        };
    }


    private AppointmentStats calculateAppointmentStats(LocalDate[] dateRange) {
        Map<String, Double> distribution = new HashMap<>();

        // Chuyển đổi LocalDate thành LocalDateTime
        LocalDateTime startDateTime = dateRange[0] != null ? dateRange[0].atStartOfDay() : null;
        LocalDateTime endDateTime = dateRange[1] != null ? dateRange[1].atTime(23, 59, 59) : null;

        long total = appointmentRepository.countByDateRange(startDateTime, endDateTime);
        if (total == 0) return new AppointmentStats();

        Arrays.stream(AppointmentStatus.values()).forEach(status -> {
            long count = appointmentRepository.countByStatusAndDateRange(status, startDateTime, endDateTime);
            double percentage = (count * 100.0) / total;
            distribution.put(status.name(), percentage);
        });

        AppointmentStats stats = new AppointmentStats();
        stats.setStatusDistribution(distribution);
        return stats;
    }

    private DepartmentStats calculateDepartmentStats(LocalDate[] dateRange) {
        Map<String, Double> distribution = new HashMap<>();

        LocalDateTime startDateTime = dateRange[0] != null ? dateRange[0].atStartOfDay() : null;
        LocalDateTime endDateTime = dateRange[1] != null ? dateRange[1].atTime(23, 59, 59) : null;

        List<Appointments> completedAppointments = appointmentRepository
                .findByStatusAndDateRange(AppointmentStatus.COMPLETED, startDateTime, endDateTime);

        long totalCompleted = completedAppointments.size();

        if (totalCompleted == 0) return new DepartmentStats(distribution);

        completedAppointments.stream()
                .collect(Collectors.groupingBy(
                        appointment -> appointment.getPsychologist().getDepartment().getName(),
                        Collectors.counting()
                ))
                .forEach((departmentName, count) -> {
                    double percentage = (count * 100.0) / totalCompleted;
                    distribution.put(departmentName, percentage);
                });

        return new DepartmentStats(distribution);
    }
}