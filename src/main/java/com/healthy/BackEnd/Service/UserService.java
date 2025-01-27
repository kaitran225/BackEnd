package com.healthy.BackEnd.Service;

import com.healthy.BackEnd.dto.StudentDTO;
import com.healthy.BackEnd.entity.Parents;
import com.healthy.BackEnd.entity.Psychologists;
import com.healthy.BackEnd.entity.Students;
import com.healthy.BackEnd.entity.Users;
import com.healthy.BackEnd.exception.ResourceNotFoundException;
import com.healthy.BackEnd.repository.ParentRepository;
import com.healthy.BackEnd.repository.PsychologistRepository;
import com.healthy.BackEnd.repository.StudentRepository;
import com.healthy.BackEnd.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    @Autowired
    private ParentRepository parentRepository;

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
        if(user.getRole() == Users.UserRole.PARENT) {
            Parents  parents = parentRepository.findByUserID(user.getUserId());
            List<Students> studentsList = parents.getStudents();
            StudentService studentService = new StudentService();
            List<StudentDTO> childrenList = studentsList.stream().map(studentService::convertToChildDTO).toList();
            return UserDTO.builder()
                    .userId(user.getUserId())
                    .fullName(user.getFullName())
                    .email(user.getEmail())
                    .phone(user.getPhoneNumber())
                    .gender(user.getGender().toString())
                    .children(childrenList)
                    .role(user.getRole().toString())
                    .createdAt(user.getCreatedAt())
                    .updatedAt(user.getUpdatedAt())
                    .build();
        }
        if (user.getRole() == Users.UserRole.STUDENT) {
            Students student = studentRepository.findByUserID(user.getUserId());
            StudentService studentService = new StudentService();
            return UserDTO.builder()
                    .userId(user.getUserId())
                    .fullName(user.getFullName())
                    .email(user.getEmail())
                    .gender(user.getGender().toString())
                    .phone(user.getPhoneNumber())
                    .role(user.getRole().toString())
                    .createdAt(user.getCreatedAt())
                    .updatedAt(user.getUpdatedAt())
                    .studentInfo(studentService.convertToDTO(student))
                    .build();
        }
        if (user.getRole() == Users.UserRole.PSYCHOLOGIST) {
            Psychologists psychologist = psychologistRepository.findByUserID(user.getUserId());
            PsychologistService psychologistService = new PsychologistService();
            return UserDTO.builder()
                    .userId(user.getUserId())
                    .fullName(user.getFullName())
                    .email(user.getEmail())
                    .phone(user.getPhoneNumber())
                    .role(user.getRole().toString())
                    .gender(user.getGender().toString())
                    .createdAt(user.getCreatedAt())
                    .updatedAt(user.getUpdatedAt())
                    .psychologistInfo(psychologistService.convertToDTO(psychologist))
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
