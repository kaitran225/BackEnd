package com.healthy.backend.service;

import com.healthy.backend.dto.appointment.AppointmentResponse;
import com.healthy.backend.dto.psychologist.PsychologistResponse;
import com.healthy.backend.dto.student.StudentResponse;
import com.healthy.backend.dto.survey.SurveyQuestionResultResponse;
import com.healthy.backend.dto.survey.SurveyResultsResponse;
import com.healthy.backend.dto.user.UsersResponse;
import com.healthy.backend.entity.*;
import com.healthy.backend.exception.ResourceNotFoundException;
import com.healthy.backend.mapper.AppointmentMapper;
import com.healthy.backend.mapper.PsychologistsMapper;
import com.healthy.backend.mapper.StudentMapper;
import com.healthy.backend.mapper.UserMapper;
import com.healthy.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final StudentRepository studentRepository;
    private final SurveyResultRepository surveyResultRepository;
    private final SurveyRepository surveyRepository;
    private final AnswersRepository answersRepository;
    private final UserRepository userRepository;
    private final PsychologistRepository psychologistRepository;
    private final ParentRepository parentRepository;
    private final AppointmentRepository appointmentRepository;

    private final UserMapper userMapper;
    private final PsychologistsMapper psychologistsMapper;
    private final StudentMapper studentMapper;
    private final AppointmentMapper appointmentMapper;

    public boolean isEmpty() {
        return userRepository.findAll().isEmpty();
    }

    public List<UsersResponse> getAllUsers() {
        if (isEmpty()) {
            throw new ResourceNotFoundException("No users found");
        }
        return userRepository.findAllUsers().stream()
                .map(this::convert)
                .toList();
    }

    public UsersResponse getUserById(String id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return convert(user);
    }

    public UsersResponse updateUser(String userId, Users updatedUser) {
        Users existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        boolean hasChanges = false;

        if (!existingUser.getFullName().equals(updatedUser.getFullName())) {
            existingUser.setFullName(updatedUser.getFullName());
            hasChanges = true;
        }
        if (!existingUser.getEmail().equals(updatedUser.getEmail())) {
            existingUser.setEmail(updatedUser.getEmail());
            hasChanges = true;
        }
        if (!existingUser.getPhoneNumber().equals(updatedUser.getPhoneNumber())) {
            existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
            hasChanges = true;
        }

        if (!hasChanges) {
            return null; // No changes detected
        }

        existingUser.setUpdatedAt(LocalDateTime.now());
        userRepository.save(existingUser);

        return convert(existingUser); // Convert entity to response DTO
    }

    public List<AppointmentResponse> getUserAppointments(String id) {
        Users user = userRepository.findById(id).orElseThrow();
        PsychologistResponse psychologistResponse = null;
        StudentResponse studentResponse = null;
        List<Appointments> appointmentsList  = null;

        if (!user.getRole().equals(Users.UserRole.STUDENT)
                && !user.getRole().equals(Users.UserRole.PSYCHOLOGIST)) {
            return null;
        }
        if(user.getRole().equals(Users.UserRole.STUDENT)){
            studentResponse = studentMapper.buildStudentResponse(
                    studentRepository.findByStudentID(id)
            );
            appointmentsList = appointmentRepository.findByStudentID(
                    studentResponse.getStudentId()
            );
        }
        if(user.getRole().equals(Users.UserRole.PSYCHOLOGIST)){
            psychologistResponse = psychologistsMapper.buildPsychologistResponse(
                    psychologistRepository.findByUserID(id)
            );
            appointmentsList = appointmentRepository
                    .findByPsychologistID(psychologistResponse.getPsychologistId());
        }


        return appointmentMapper.buildAppointmentResponseList(
                user,
                appointmentsList,
                psychologistResponse,
                studentResponse);
    }



    public void deleteUser(String id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        userRepository.delete(user);
    }







    private UsersResponse convert(Users user) {
        List<StudentResponse> childrenList = null;
        List<AppointmentResponse> appointmentsResponseList = null;
        List<Appointments> appointmentsList = null;
        List<SurveyResultsResponse> surveyResultsResponseList = null;
        PsychologistResponse psychologistResponse = null;
        StudentResponse studentResponse = null;


        if (user.getRole() == Users.UserRole.STUDENT) {
            Students students = studentRepository.findByUserID(user.getUserId());
            surveyResultsResponseList = ;
            studentResponse = studentMapper.buildStudentResponse(students, surveyResultsResponseList);
            appointmentsList = appointmentRepository.findByStudentID(students.getStudentID());
            appointmentsResponseList = appointmentMapper.buildAppointmentResponseList(
                    user,
                    appointmentsList,
                    null,
                    studentResponse
            );
        }

        if (user.getRole() == Users.UserRole.PSYCHOLOGIST) {
            Psychologists psychologists = psychologistRepository.findByUserID(user.getUserId());
            psychologistResponse = psychologistsMapper.buildPsychologistResponse(psychologists);
            appointmentsList = appointmentRepository.findByPsychologistID(psychologists.getPsychologistID());        }

        if (user.getRole() == Users.UserRole.PARENT) {
            Parents parent = parentRepository.findByUserIDWithStudents(user.getUserId());
            childrenList = parent.getStudents().stream()
                    .map(studentMapper::buildStudentResponse)
                    .collect(Collectors.toList());
        }

        return userMapper.buildUserResponse(
                user,
                psychologistResponse,
                studentResponse,
                appointmentsResponseList,
                childrenList
        );
    }
}