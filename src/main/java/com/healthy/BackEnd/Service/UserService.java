package com.healthy.BackEnd.Service;

import com.healthy.BackEnd.entity.Psychologists;
import com.healthy.BackEnd.entity.StudentNotes;
import com.healthy.BackEnd.entity.Students;
import com.healthy.BackEnd.entity.Users;
import com.healthy.BackEnd.exception.ResourceNotFoundException;
import com.healthy.BackEnd.repository.PsychologistRepository;
import com.healthy.BackEnd.repository.StudentRepository;
import com.healthy.BackEnd.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.healthy.BackEnd.dto.UserDTO;

@Service
public class UserService {
    
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PsychologistRepository psychologistRepository;

    public boolean isEmpty() {
        return userRepository.findAll().isEmpty();
    }
    public boolean isUserExist(String id) {
        return userRepository.existsById(id);
    }
    public List<UserDTO> getAllUsers() {
        if (isEmpty()) {
            throw new ResourceNotFoundException("No users found");
        }
        return userRepository.findAll().stream()
        .map(this::convertToDTO)
        .toList();
    }

    public Users getUserById(String id) {
        if (!isUserExist(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        return userRepository.findById(id).orElse(null);
    }

    public Users editUser(Users user) {
        Users existingUser = userRepository.findById(user.getUserId())
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + user.getUserId()));
        existingUser.setFullName(user.getFullName());
        existingUser.setEmail(user.getEmail());
        existingUser.setPhoneNumber(user.getPhoneNumber());
        existingUser.setRole(user.getRole());
        existingUser.setUsername(user.getUsername());
        existingUser.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(existingUser);
    }

    public void deleteUser(String id) {
        if (!isUserExist(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }


    public UserDTO convertToDTO(Users user) {
        if (user.getRole() == Users.UserRole.STUDENT) {
            Students student = studentRepository.findByUserID(user.getUserId());
            return UserDTO.builder()
                    .userId(user.getUserId())
                    .fullName(user.getFullName())
                    .email(user.getEmail())
                    .gender(user.getGender().toString())
                    .phone(user.getPhoneNumber())
                    .role(user.getRole().toString())
                    .createdAt(user.getCreatedAt())
                    .updatedAt(user.getUpdatedAt())
                    .anxietyScore(student.getAnxietyScore())
                    .depressionScore(student.getDepressionScore())
                    .stressScore(student.getStressScore())
                    .build();
        }
        if (user.getRole() == Users.UserRole.PSYCHOLOGIST) {
            Psychologists psychologist = psychologistRepository.findByUserID(user.getUserId());
            return UserDTO.builder()
                    .userId(user.getUserId())
                    .fullName(user.getFullName())
                    .email(user.getEmail())
                    .phone(user.getPhoneNumber())
                    .role(user.getRole().toString())
                    .gender(user.getGender().toString())
                    .createdAt(user.getCreatedAt())
                    .updatedAt(user.getUpdatedAt())
                    .specialization(psychologist.getSpecialization())
                    .yearsOfExperience(psychologist.getYearsOfExperience())
                    .build();
        }
        return UserDTO.builder()
                .userId(user.getUserId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhoneNumber())
                .gender(user.getGender().toString())
                .role(user.getRole().toString())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
