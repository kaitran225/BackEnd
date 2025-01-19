package com.healthy.BackEnd.Controller;

import com.healthy.BackEnd.Service.AuthenticationService;
import com.healthy.BackEnd.dto.LoginDTO;
import com.healthy.BackEnd.entity.Appointments;
import com.healthy.BackEnd.entity.Programs;
import com.healthy.BackEnd.entity.SurveyResults;
import com.healthy.BackEnd.entity.Users;
import com.healthy.BackEnd.exception.ResourceNotFoundException;
import com.healthy.BackEnd.repository.AppointmentRepository;
import com.healthy.BackEnd.repository.ProgramRepository;
import com.healthy.BackEnd.repository.SurveyResultRepository;
import com.healthy.BackEnd.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
public class UserController {
    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProgramRepository programRepository;

    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    SurveyResultRepository surveyResultRepository;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody Users users) {
        Users newAccount = authenticationService.register(users);
        return ResponseEntity.ok(newAccount);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginEmployee(@Valid @RequestBody LoginDTO loginDTO) {
        boolean isAuthenticated = authenticationService.login(loginDTO);

        if (isAuthenticated) {
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }

    @GetMapping("/users")
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/users/{userId}")
    public Users getUserById(@PathVariable String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }

    @GetMapping("/users/{userId}/programs")
    public List<Programs> getProgramsByUserId(@PathVariable String userId) {
        return programRepository.findByManagedByStaffID(userId);
    }

    @GetMapping("/users/{userId}/appointments")
    public List<Appointments> getAppointmentsByUserId(@PathVariable String userId) {
        return appointmentRepository.findByStudentID(userId);
    }

    @GetMapping("/users/{userId}/surveys")
    public List<SurveyResults> getSurveyResultsByUserId(@PathVariable String userId) {
        return surveyResultRepository.findByStudentID(userId);
    }

    @PutMapping("/users/{userId}/edit")
    public Users updateUser(@PathVariable String userId, @RequestBody Users updatedUser) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        user.setFullName(updatedUser.getFullName());
        user.setEmail(updatedUser.getEmail());
        user.setPhoneNumber(updatedUser.getPhoneNumber());

        return userRepository.save(user);
    }
}
