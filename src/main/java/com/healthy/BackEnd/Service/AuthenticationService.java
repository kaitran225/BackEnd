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
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        if (authenticationRepository.findByUsername(request.getUsername()) != null) {
            throw new RuntimeException("Username already exists");
        }
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

        Users savedUser = userRepository.save(Users.fromDTO(user));

        var accessToken = jwtService.generateToken(savedUser);
        var refreshToken = jwtService.generateRefreshToken(savedUser);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(savedUser.getUserId())
                .role(savedUser.getRole().toString())
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        UsernamePasswordAuthenticationToken authToken;
        Users user;
        if (request.getLoginIdentifier().contains("@")) {
            user = authenticationRepository.findByEmail(request.getLoginIdentifier());
        } else {
            user = authenticationRepository.findByUsername(request.getLoginIdentifier());
        }

        // Generate authToken
        authToken = new UsernamePasswordAuthenticationToken(
            request.getLoginIdentifier(),
            request.getPassword()
        );

        authenticationManager.authenticate(authToken);


        // Get user
        user = authenticationRepository.findByUsername(request.getLoginIdentifier());
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
            HttpServletRequest request
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

        Users user = authenticationRepository.findByUsername(username);
        if (!user.isPresent()) {
            throw new RuntimeException("User not found");
        }

        if (!jwtService.isTokenValid(refreshToken, user)) {
            throw new RuntimeException("Invalid refresh token");
        }

        String accessToken = jwtService.generateToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);

        // Invalidate the old refresh token
        jwtService.invalidateToken(refreshToken);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(newRefreshToken)
                .userId(user.getUserId())
                .role(user.getRole().toString())
                .build();
    }

    public void initiatePasswordReset(String email) {
        Users user = authenticationRepository.findByEmail(email);
        if (user != null) {
            // Generate password reset token
            String resetToken = UUID.randomUUID().toString();
            user.setResetToken(resetToken);
            authenticationRepository.save(user);
            emailService.sendPasswordResetEmail(user.getEmail(), resetToken);
        }
    }

    public void resetPassword(String token, String newPassword) {
        var user = authenticationRepository.findByResetToken(token);

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        authenticationRepository.save(user);
    }
}
