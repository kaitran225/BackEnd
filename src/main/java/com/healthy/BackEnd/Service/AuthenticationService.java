package com.healthy.BackEnd.Service;

import com.healthy.BackEnd.dto.UserDTO;
import com.healthy.BackEnd.dto.auth.AuthenticationRequest;
import com.healthy.BackEnd.dto.auth.AuthenticationResponse;
import com.healthy.BackEnd.dto.auth.RegisterRequest;
import com.healthy.BackEnd.entity.Users;
import com.healthy.BackEnd.repository.AuthenticationRepository;
import com.healthy.BackEnd.repository.UserRepository;
import com.healthy.BackEnd.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.http.HttpResponse;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationRepository authenticationRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final UserRepository userRepository;

    public AuthenticationResponse register(RegisterRequest request) {
        // Check if username already exists
        if (authenticationRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        // Create new user
        UserDTO user = UserDTO.builder()
                .userId(UUID.randomUUID().toString())
                .username(request.getUsername())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phone(request.getPhoneNumber())
                .role(String.valueOf(Users.UserRole.valueOf(request.getRole().toUpperCase())))
                .gender(String.valueOf(Users.Gender.valueOf(request.getGender())))
                .build();

        // Save user
        Users savedUser = userRepository.save(Users.fromDTO(user));

        // Generate tokens
        var accessToken = jwtService.generateToken(savedUser);
        var refreshToken = jwtService.generateRefreshToken(savedUser);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(savedUser.getUserId())
                .role(savedUser.getRole().toString())
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request )  {
        // Authenticate user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // Get user
        var user = authenticationRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Generate tokens
        var accessToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);


        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getUserId())
                .role(user.getRole().toString())
                .build();
    }

    public AuthenticationResponse refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid refresh token");
        }

        final String refreshToken = authHeader.substring(7);
        final String username = jwtService.extractUsername(refreshToken);

        if (username == null) {
            throw new RuntimeException("Invalid refresh token");
        }

        var user = authenticationRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        if (!jwtService.isTokenValid(refreshToken, user.get())) {
            throw new RuntimeException("Invalid refresh token");
        }

        var accessToken = jwtService.generateToken(user.get());

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.get().getUserId())
                .role(user.get().getRole().toString())
                .build();
    }

    public void initiatePasswordReset(String email) {
        var user = authenticationRepository.findByEmail(email)
                .orElse(null);
        if (user != null) {
            // Generate password reset token
            String resetToken = UUID.randomUUID().toString();
            user.setResetToken(resetToken);
            authenticationRepository.save(user);
            emailService.sendPasswordResetEmail(user.getEmail(), resetToken);
        }
    }

    public void resetPassword(String token, String newPassword) {
        var user = authenticationRepository.findByResetToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid reset token"));

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        authenticationRepository.save(user);
    }
}
