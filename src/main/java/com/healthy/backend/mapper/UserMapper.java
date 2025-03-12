package com.healthy.backend.mapper;

import com.healthy.backend.dto.appointment.AppointmentResponse;
import com.healthy.backend.dto.auth.request.RegisterRequest;
import com.healthy.backend.dto.programs.ProgramsResponse;
import com.healthy.backend.dto.psychologist.PsychologistResponse;
import com.healthy.backend.dto.student.StudentResponse;
import com.healthy.backend.dto.survey.SurveyResultsResponse;
import com.healthy.backend.dto.user.UsersResponse;
import com.healthy.backend.entity.Users;
import com.healthy.backend.enums.Gender;
import com.healthy.backend.enums.Role;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {

    public UsersResponse buildBasicUserResponse(
            Users user
    ) {
        return UsersResponse.builder()
                .userId(user.getUserId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .gender(user.getGender().toString())
                .address(user.getAddress())
                .role(user.getRole().toString())
                .active(user.isActive())
                .verified(user.isVerified())
                .deleted(user.isDeleted())
                .build();
    }

    public UsersResponse buildBasicStudentUserResponse(
            Users user, StudentResponse student
    ) {
        return UsersResponse.builder()
                .userId(user.getUserId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .gender(user.getGender().toString())
                .address(user.getAddress())
                .role(user.getRole().toString())
                .studentResponse(student)
                .active(user.isActive())
                .verified(user.isVerified())
                .deleted(user.isDeleted())
                .build();
    }


    public UsersResponse buildBasicPsychologistUserResponse(
            Users user, PsychologistResponse psychologist
    ) {
        return UsersResponse.builder()
                .userId(user.getUserId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .gender(user.getGender().toString())
                .address(user.getAddress())
                .role(user.getRole().toString())
                .psychologistResponse(psychologist)
                .active(user.isActive())
                .verified(user.isVerified())
                .deleted(user.isDeleted())
                .build();
    }

    public UsersResponse buildBasicParentUserResponse(
            Users user, List<StudentResponse> children
    ) {
        return UsersResponse.builder()
                .userId(user.getUserId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .gender(user.getGender().toString())
                .address(user.getAddress())
                .role(user.getRole().toString())
                .childrenResponse(children)
                .active(user.isActive())
                .verified(user.isVerified())
                .deleted(user.isDeleted())
                .build();
    }

    public UsersResponse buildUserDetailsResponse(
            Users user,
            PsychologistResponse psychologistResponse,
            StudentResponse studentResponse,
            List<AppointmentResponse> appointmentResponse,
            List<ProgramsResponse> programParticipationResponse,
            List<SurveyResultsResponse> surveyResultsResponse,
            List<StudentResponse> children
    ) {
        return UsersResponse.builder()
                .userId(user.getUserId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .gender(user.getGender().toString())
                .role(user.getRole().toString())
                .active(user.isActive())
                .verified(user.isVerified())
                .deleted(user.isDeleted())
                .psychologistInfo(psychologistResponse)
                .studentInfo(studentResponse)
                .children(children)
                .appointmentsRecord(appointmentResponse)
                .programsRecord(programParticipationResponse)
                .surveyResults(surveyResultsResponse)
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    // Convert User entity to UserResponse (Student)
    public Users buildUserEntity(RegisterRequest requestRequest,
                                 String token, String userId, String password, String hashedID, Role role) {
        return Users.builder()
                .userId(userId)
                .hashedID(hashedID)
                .passwordHash(password)
                .fullName(requestRequest.getFullName())
                .email(requestRequest.getEmail())
                .phoneNumber(requestRequest.getPhoneNumber())
                .role(role)
                .address(requestRequest.getAddress())
                .gender(Gender.valueOf(requestRequest.getGender()))
                .verificationToken(token)
                .build();
    }

    public Users buildUserEntity(RegisterRequest requestRequest,
                                 String token, String userID, String password, String hashedID) {
        return Users.builder()
                .userId(userID)
                .hashedID((hashedID))
                .passwordHash(password)
                .fullName(requestRequest.getFullName())
                .email(requestRequest.getEmail())
                .phoneNumber(requestRequest.getPhoneNumber())
                .role(Role.valueOf(requestRequest.getRole()))
                .address(requestRequest.getAddress())
                .gender(Gender.valueOf(requestRequest.getGender()))
                .verificationToken(token)
                .build();
    }
}
