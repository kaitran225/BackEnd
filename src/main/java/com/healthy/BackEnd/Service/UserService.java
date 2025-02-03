package com.healthy.BackEnd.Service;

import com.healthy.BackEnd.DTO.Student.StudentResponse;
import com.healthy.BackEnd.DTO.User.UsersResponse;
import com.healthy.BackEnd.Entity.Parents;
import com.healthy.BackEnd.Entity.Psychologists;
import com.healthy.BackEnd.Entity.Students;
import com.healthy.BackEnd.Entity.Users;
import com.healthy.BackEnd.Exception.ResourceNotFoundException;
import com.healthy.BackEnd.Repository.ParentRepository;
import com.healthy.BackEnd.Repository.PsychologistRepository;
import com.healthy.BackEnd.Repository.StudentRepository;
import com.healthy.BackEnd.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PsychologistRepository psychologistRepository;

    @Autowired
    private ParentRepository parentRepository;

    public boolean isEmpty() {
        return userRepository.findAll().isEmpty();
    }

    public boolean isUserExist(String id) {
        return userRepository.existsById(id);
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
        if (!isUserExist(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        return convert(Objects.requireNonNull(userRepository.findById(id).orElse(null)));
    }

    public UsersResponse editUser(Users user) {
        UsersResponse existingUser = userRepository.findById(user.getUserId()).map(this::convert)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + user.getUserId()));
        existingUser.setFullName(user.getFullName());
        existingUser.setEmail(user.getEmail());
        existingUser.setPhoneNumber(user.getPhoneNumber());
        existingUser.setRole(String.valueOf(user.getRole()));
        existingUser.setUsername(user.getUsername());
        existingUser.setUpdatedAt(LocalDateTime.now());
        return existingUser;
    }

    public void deleteUser(String id) {
        if (!isUserExist(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    private UsersResponse convertParent(Users user) {
        Parents parents = parentRepository.findByUserID(user.getUserId());
        List<Students> studentsList = parents.getStudents();
        StudentService studentService = new StudentService();
        List<StudentResponse> childrenList = studentsList.stream().map(studentService::convertToChildDTO).toList();
        return UsersResponse.builder()
                .userId(user.getUserId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .gender(user.getGender().toString())
                .children(childrenList)
                .role(user.getRole().toString())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    private UsersResponse convertStudent(Users user) {
        Students student = studentRepository.findByUserID(user.getUserId());
        StudentService studentService = new StudentService();
        return UsersResponse.builder()
                .userId(user.getUserId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .gender(user.getGender().toString())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole().toString())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .studentInfo(studentService.convertToDTO(student))
                .build();
    }

    private UsersResponse convertPsychologist(Users user) {
        Psychologists psychologist = psychologistRepository.findByUserID(user.getUserId());
        PsychologistService psychologistService = new PsychologistService();
        return UsersResponse.builder()
                .userId(user.getUserId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole().toString())
                .gender(user.getGender().toString())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .psychologistInfo(psychologistService.convertToDTO(psychologist))
                .build();

    }

    public UsersResponse convert(Users user) {
        if (user.getRole() == Users.UserRole.PARENT) {
            return convertParent(user);
        }
        if (user.getRole() == Users.UserRole.STUDENT) {
            return convertStudent(user);
        }
        if (user.getRole() == Users.UserRole.PSYCHOLOGIST) {
            return convertPsychologist(user);
        }
        return UsersResponse.builder()
                .userId(user.getUserId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .gender(user.getGender().toString())
                .role(user.getRole().toString())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
