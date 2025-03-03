package com.healthy.backend.mapper;

import com.healthy.backend.dto.appointment.AppointmentResponse;
import com.healthy.backend.dto.auth.request.PsychologistRegisterRequest;
//import com.healthy.backend.dto.psychologist.LeaveRequest;
//import com.healthy.backend.dto.psychologist.LeaveResponse;
import com.healthy.backend.dto.psychologist.PsychologistRequest;
import com.healthy.backend.dto.psychologist.PsychologistResponse;
import com.healthy.backend.dto.student.StudentResponse;
import com.healthy.backend.dto.user.UsersResponse;
import com.healthy.backend.entity.Appointments;
//import com.healthy.backend.entity.OnLeaveRequest;
import com.healthy.backend.entity.Psychologists;
import com.healthy.backend.entity.Users;
import com.healthy.backend.enums.OnLeaveStatus;
import com.healthy.backend.enums.PsychologistStatus;
import com.healthy.backend.exception.ResourceNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PsychologistsMapper {

    public Psychologists buildPsychologistEntity(PsychologistRegisterRequest request, Users user, String psychologistId) {
        return Psychologists.builder()
                .psychologistID(psychologistId)
                .status(PsychologistStatus.ACTIVE)
                .yearsOfExperience(request.getPsychologistDetails().getYearsOfExperience())
                .departmentID(request.getPsychologistDetails().getDepartmentID())
                .user(user)
                .build();
    }

    public PsychologistResponse buildPsychologistResponse(Psychologists psychologist
            , UsersResponse usersResponse) {
        return PsychologistResponse.builder()
                .psychologistId(psychologist.getPsychologistID())
                .status(psychologist.getStatus().name())
                .departmentName(psychologist.getDepartment().getName())
                .yearsOfExperience(psychologist.getYearsOfExperience())
                .info(usersResponse)
                .build();
    }

    public PsychologistResponse buildPsychologistResponse(Psychologists psychologist) {
        return PsychologistResponse.builder()
                .name(psychologist.getFullNameFromUser())
                .psychologistId(psychologist.getPsychologistID())
                .status(psychologist.getStatus().name())
                .departmentName(psychologist.getDepartment().getName())
                .yearsOfExperience(psychologist.getYearsOfExperience())
                .build();
    }

//    public LeaveResponse buildLeaveResponse(OnLeaveRequest request) {
//        return LeaveResponse.builder()
//                .requestId(request.getLeaveRequestID())
//                .psychologistName(request.getPsychologist().getFullNameFromUser())
//                .startDate(request.getStartDate())
//                .endDate(request.getEndDate())
//                .reason(request.getReason())
//                .status(request.getStatus().name())
//                .department(request.getPsychologist().getDepartment().getName())
//                .createdAt(request.getCreatedAt())
//                .build();
//    }

//    public OnLeaveRequest createPendingOnLeaveRequestEntity(LeaveRequest request, String newID,
//                                                            Psychologists psychologist) {
//        return OnLeaveRequest.builder()
//                .leaveRequestID(newID)
//                .psychologist(psychologist)
//                .startDate(request.getStartDate())
//                .endDate(request.getEndDate())
//                .reason(request.getReason())
//                .status(OnLeaveStatus.PENDING)
//                .build();
//    }

    public PsychologistResponse buildPsychologistResponse(Psychologists psychologist,
                                                          List<Appointments> appointments,
                                                          Users users) {
        return PsychologistResponse.builder()
                .psychologistId(psychologist.getPsychologistID())
                .status(psychologist.getStatus().name())
                .departmentName(psychologist.getDepartment().getName())
                .yearsOfExperience(psychologist.getYearsOfExperience())
                .info(buildUserResponse(users))
                .appointment(buildAppointmentResponses(appointments))
                .build();
    }

    private UsersResponse buildUserResponse(Users users) {
        return UsersResponse.builder()
                .fullName(users.getFullName())
                .username(users.getUsername())
                .phoneNumber(users.getPhoneNumber())
                .email(users.getEmail())
                .gender(users.getGender().toString())
                .build();
    }

    private List<AppointmentResponse> buildAppointmentResponses(List<Appointments> appointments) {
        return appointments.stream()
                .map(this::buildAppointmentResponse)
                .collect(Collectors.toList());
    }

    private AppointmentResponse buildAppointmentResponse(Appointments appointment) {
        return AppointmentResponse.builder()
                .appointmentID(appointment.getAppointmentID())
                .CreatedAt(appointment.getCreatedAt())
                .Status(appointment.getStatus().name())
                .studentResponse(buildStudentResponse(appointment))
                .studentNotes(appointment.getStudentNote())
                .timeSlotID(appointment.getTimeSlotsID())
                .UpdatedAt(appointment.getUpdatedAt())
                .build();
    }

    private StudentResponse buildStudentResponse(Appointments appointment) {
        return StudentResponse.builder()
                .studentId(appointment.getStudentID())
                .grade(appointment.getStudent().getGrade())
                .className(appointment.getStudent().getClassName())
                .schoolName(appointment.getStudent().getSchoolName())
                .depressionScore(appointment.getStudent().getDepressionScore())
                .anxietyScore(appointment.getStudent().getAnxietyScore())
                .stressScore(appointment.getStudent().getStressScore())
                .build();
    }
}
