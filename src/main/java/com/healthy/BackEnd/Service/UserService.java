package com.healthy.BackEnd.Service;

import com.healthy.BackEnd.entity.Users;
import com.healthy.BackEnd.exception.ResourceNotFoundException;
import com.healthy.BackEnd.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public boolean isEmpty() {
        return userRepository.findAll().isEmpty();
    }
    public boolean isUserExist(String id) {
        return userRepository.existsById(id);
    }
    public List<Users> getAllUsers() {
        if (isEmpty()) {
            throw new ResourceNotFoundException("No users found");
        }
        return userRepository.findAll();
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
}
