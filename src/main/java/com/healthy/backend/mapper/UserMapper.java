package com.healthy.backend.mapper;

import com.healthy.backend.dto.appointment.AppointmentResponse;
import com.healthy.backend.dto.psychologist.PsychologistResponse;
import com.healthy.backend.dto.student.StudentResponse;
import com.healthy.backend.dto.user.UsersResponse;
import com.healthy.backend.entity.Psychologists;
import com.healthy.backend.entity.Users;

import java.util.List;

public class UserMapper {
    public UsersResponse buildUserResponse(
            Users user,
            PsychologistResponse psychologistResponse,
            StudentResponse studentResponse,
            List<AppointmentResponse> appointmentResponse,
            List<StudentResponse> children
    ) {
        return UsersResponse.builder()
                .userId(user.getUserId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .gender(user.getGender().toString())
                .role(user.getRole().toString())
                .psychologistInfo(psychologistResponse)
                .studentInfo(studentResponse)
                .children(children)
                .appointmentsRecord(appointmentResponse)
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
