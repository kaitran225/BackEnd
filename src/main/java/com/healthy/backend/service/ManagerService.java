package com.healthy.backend.service;

import com.healthy.backend.dto.manager.AppointmentStatsResponse;
import com.healthy.backend.dto.manager.PsychologistStatsResponse;
import com.healthy.backend.entity.Appointments;
import com.healthy.backend.entity.NotificationSchedule;
import com.healthy.backend.entity.PsychologistKPI;
import com.healthy.backend.entity.Psychologists;
import com.healthy.backend.enums.AppointmentStatus;
import com.healthy.backend.repository.AppointmentRepository;
import com.healthy.backend.repository.NotificationScheduleRepository;
import com.healthy.backend.repository.PsychologistKPIRepository;
import com.healthy.backend.repository.PsychologistRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ManagerService {

    private final AppointmentRepository appointmentRepository;
    private final PsychologistRepository psychologistRepository;
    private final PsychologistKPIRepository kpiRepository;
    private final NotificationScheduleRepository notificationScheduleRepository;
    private  final GeneralService generalService;
    private LocalDate lastNotificationDate = null;

    private final  NotificationService notificationService;
    @Scheduled(fixedRate = 1000)
    public void sendWeeklyKpiReminders() {
        NotificationSchedule schedule = notificationScheduleRepository.findFirstByOrderByNotificationTimeAsc();

        if (schedule == null) {
            return; // Nếu chưa thiết lập thời gian, không gửi thông báo
        }

        LocalTime now = LocalTime.now();
        DayOfWeek today = LocalDate.now().getDayOfWeek();

        if (lastNotificationDate != null && lastNotificationDate.equals(LocalDate.now())) {
            return;
        }

        // Kiểm tra nếu thời gian hiện tại khớp với thời gian đã thiết lập
        if (today.equals(schedule.getNotificationDay())
                && now.getHour() == schedule.getNotificationTime().getHour()
                && now.getMinute() == schedule.getNotificationTime().getMinute()) {

            LocalDate todayDate = LocalDate.now();
            int currentMonth = todayDate.getMonthValue();
            int currentYear = todayDate.getYear();

            List<Psychologists> psychologists = psychologistRepository.findAll();

            for (Psychologists psychologist : psychologists) {
                PsychologistKPI kpi = kpiRepository.findByPsychologistIdAndMonthAndYear(
                        psychologist.getPsychologistID(),
                        currentMonth,
                        currentYear
                );

                if (kpi != null && kpi.getTargetSlots() > kpi.getAchievedSlots()) {
                    int remaining = kpi.getTargetSlots() - kpi.getAchievedSlots();
                    String message = String.format(
                            "Bạn cần đạt thêm %d slot trong tháng %d để hoàn thành chỉ tiêu KPI.",
                            remaining,
                            currentMonth
                    );

                    notificationService.createAppointmentNotification(
                            psychologist.getUserID(),
                            "Nhắc nhở KPI hàng tuần",
                            message,
                            null
                    );
                }
            }
            lastNotificationDate = LocalDate.now();
        }
    }

    @Transactional
    public void setKpiForPsychologist(String psychologistId, int month, int year, int targetSlots) {
        PsychologistKPI kpi = kpiRepository.findByPsychologistIdAndMonthAndYear(psychologistId, month, year);

        if (kpi == null) {
            kpi = new PsychologistKPI();
            kpi.setId(psychologistId + "-" + month + "-" + year);
            kpi.setPsychologistId(psychologistId);
            kpi.setMonth(month);
            kpi.setYear(year);
        }

        kpi.setTargetSlots(targetSlots);
        kpiRepository.save(kpi);
    }

    @Transactional
    public void setNotificationSchedule(LocalTime notificationTime, DayOfWeek notificationDay) {
        NotificationSchedule schedule = notificationScheduleRepository.findFirstByOrderByNotificationTimeAsc();

        if (schedule == null) {
            schedule = new NotificationSchedule();
            schedule.setId("notification-schedule-1");
        }

        schedule.setNotificationTime(notificationTime);
        schedule.setNotificationDay(notificationDay);
        notificationScheduleRepository.save(schedule);
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
                    .collect(Collectors.toList());

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

}