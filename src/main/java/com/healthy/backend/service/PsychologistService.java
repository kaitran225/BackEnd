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

    @Autowired
    public PsychologistRepository psychologistRepository;

    @Autowired
    public AppointmentRepository appointmentRepository;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public PsychologistsMapper psychologistsMapper;

    @Autowired
    public TimeSlotRepository timeSlotRepository;

    @Autowired
    public TimeSlotMapper timeSlotMapper;

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

    public PsychologistResponse updatePsychologist(String id, PsychologistRequest request) {
        Psychologists psychologist = psychologistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No psychologist found with id " + id));

        // Cập nhật thông tin từ request vào psychologist
        if (request.getSpecialization() != null) {
            psychologist.setSpecialization(request.getSpecialization());
        }
        if (request.getYearsOfExperience() != null) {
            psychologist.setYearsOfExperience(request.getYearsOfExperience());
        }
        if (request.getStatus() != null) {
            psychologist.setStatus(Psychologists.Status.valueOf(request.getStatus()));
        }
        // Lưu thông tin cập nhật vào cơ sở dữ liệu
        psychologistRepository.save(psychologist);

        // Trả về thông tin psychologist đã cập nhật
        return convert(psychologist);
    }



    /**
     * Lấy danh sách các psychologist có time slot trống trong ngày.
//     */
//    public List<PsychologistAvailabilityResponse> getAvailablePsychologistsByDate(LocalDate date) {
//        // Lấy tất cả timeSlots có trạng thái Available
//        List<TimeSlots> availableSlots = timeSlotRepository.findBySlotDateAndStatus(date, TimeSlots.Status.Available);
//
//        // Nhóm các timeSlot theo psychologistId
//        Map<String, List<TimeSlots>> slotsByPsychologist = availableSlots.stream()
//                .collect(Collectors.groupingBy(slot -> slot.getPsychologist().getPsychologistID()));
//
//        List<PsychologistAvailabilityResponse> response = new ArrayList<>();
//
//        for (Map.Entry<String, List<TimeSlots>> entry : slotsByPsychologist.entrySet()) {
//            // Lấy psychologist từ database
//            Psychologists psychologist = psychologistRepository.findById(entry.getKey())
//                    .orElseThrow(() -> new ResourceNotFoundException("Psychologist not found"));
//
//            // Chuyển đổi psychologist sang DTO
//            var psychologistResponse = psychologistsMapper.buildPsychologistResponse(psychologist);
//
//            // Chuyển đổi danh sách time slots sang DTO
//            List<TimeSlotResponse> timeSlotResponses = entry.getValue().stream()
//                    .map(timeSlotMapper::toResponse)
//                    .collect(Collectors.toList());
//
//            // Thêm vào danh sách kết quả
//            response.add(PsychologistAvailabilityResponse.builder()
//                    .psychologist(psychologistResponse)
//                    .availableTimeSlots(timeSlotResponses)
//                    .build());
//        }
//
//        return response;
//    }

    /**
     * Tạo danh sách time slots cố định cho psychologist trong ngày.
     */
    public List<TimeSlots> createDefaultTimeSlots(LocalDate date, String psychologistId) {
        Psychologists psychologist = psychologistRepository.findById(psychologistId)
                .orElseThrow(() -> new ResourceNotFoundException("Psychologist not found"));

        // Check for existing slots on this date for this psychologist
        List<TimeSlots> existingSlots = timeSlotRepository.findBySlotDateAndPsychologist(date, psychologist);
        if (!existingSlots.isEmpty()) {
            return existingSlots; // Return existing slots if found
        }

        List<TimeSlots> timeSlots = new ArrayList<>();

        // Reset slotNumber khi bắt đầu tạo slot buổi sáng
        int slotNumber = 1;

        // Ca sáng: 8h - 11h, cách 30 phút
        LocalTime morningStart = LocalTime.of(8, 0);
        LocalTime morningEnd = LocalTime.of(11, 0);
        timeSlots.addAll(generateTimeSlots(date, morningStart, morningEnd, psychologist, slotNumber));

        // Reset slotNumber khi bắt đầu tạo slot buổi chiều
        slotNumber = timeSlots.size() + 1; // Tiếp tục từ slotNumber tiếp theo

        // Ca chiều: 13h - 17h, cách 30 phút
        LocalTime afternoonStart = LocalTime.of(13, 0);
        LocalTime afternoonEnd = LocalTime.of(17, 0);
        timeSlots.addAll(generateTimeSlots(date, afternoonStart, afternoonEnd, psychologist, slotNumber));

        // Lưu vào database chỉ khi không có slots sẵn
        return timeSlotRepository.saveAll(timeSlots);
    }

    private List<TimeSlots> generateTimeSlots(LocalDate date, LocalTime start, LocalTime end, Psychologists psychologist, int startSlotNumber) {
        List<TimeSlots> timeSlots = new ArrayList<>();
        LocalTime currentTime = start;
        int slotNumber = startSlotNumber; // Bắt đầu từ startSlotNumber

        while (currentTime.isBefore(end) && slotNumber <= 14) {
            LocalTime nextTime = currentTime.plusMinutes(30);
            timeSlots.add(new TimeSlots(date, currentTime, nextTime, psychologist, slotNumber));
            currentTime = nextTime;
            slotNumber++;
        }

        return timeSlots;
    }
}
