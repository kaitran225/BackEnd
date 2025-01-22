package com.healthy.BackEnd.Controller;

import com.healthy.BackEnd.Service.AuthenticationService;
import com.healthy.BackEnd.Service.UserService;
import com.healthy.BackEnd.Service.ProgramService;
import com.healthy.BackEnd.dto.LoginDTO;
import com.healthy.BackEnd.entity.Appointments;
import com.healthy.BackEnd.entity.Programs;
import com.healthy.BackEnd.entity.SurveyResults;
import com.healthy.BackEnd.entity.Users;
import com.healthy.BackEnd.exception.ResourceNotFoundException;
import com.healthy.BackEnd.repository.AppointmentRepository;
import com.healthy.BackEnd.repository.SurveyResultRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
public class UserController {
    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    UserService userService;

    @Autowired
    ProgramService programService;

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

    // Working but not tested
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        boolean isEmpty = userService.isEmpty();
        if(isEmpty) return ResponseEntity.status(500).body("No users found");
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // Working but not tested
    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable String userId) {
        if(!userService.isUserExist(userId)) return ResponseEntity.status(500).body("User not found with id: " + userId);
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    // Working but not tested
    @GetMapping("/users/{userId}/programs")
    public ResponseEntity<?> getProgramsByUserId(@PathVariable String userId) {
        try {
            List<Programs> programs = programService.getProgramsByUserId(userId);
            return ResponseEntity.ok(programs);  // Return 200 OK with programs
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    // Not done and not working
    @GetMapping("/users/{userId}/appointments")
    public List<Appointments> getAppointmentsByUserId(@PathVariable String userId) {
        return appointmentRepository.findByStudentID(userId);
    }

    // Not done and not working
    @GetMapping("/users/{userId}/surveys")
    public List<SurveyResults> getSurveyResultsByUserId(@PathVariable String userId) {
        return surveyResultRepository.findByStudentID(userId);
    }

    // Not done and not working
    @PutMapping("/users/{userId}/edit")
    public ResponseEntity<?> updateUser(@PathVariable String userId, @RequestBody Users updatedUser) {
        if (!userService.isUserExist(userId)) {
            return ResponseEntity.status(404).body("User not found with id: " + userId);
        }

        Users existingUser = userService.getUserById(userId);
        
        // Check for changes before updating
        if (!existingUser.getFullName().equals(updatedUser.getFullName()) ||
            !existingUser.getEmail().equals(updatedUser.getEmail()) ||
            !existingUser.getPhoneNumber().equals(updatedUser.getPhoneNumber())) {
            
            existingUser.setFullName(updatedUser.getFullName());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setPhoneNumber(updatedUser.getPhoneNumber());

            Users updatedUserEntity = userService.editUser(existingUser);
            return ResponseEntity.ok(updatedUserEntity);
        }

        return ResponseEntity.status(400).body("No changes detected to update.");
    }
}
