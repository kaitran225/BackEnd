package com.healthy.backend.service;

import com.healthy.backend.dto.auth.AuthenticationRequest;
import com.healthy.backend.dto.auth.AuthenticationResponse;
import com.healthy.backend.dto.auth.RegisterRequest;
import com.healthy.backend.dto.user.UsersRequest;
import com.healthy.backend.dto.user.UsersResponse;
import com.healthy.backend.entity.RefreshToken;
import com.healthy.backend.entity.Users;
import com.healthy.backend.repository.AuthenticationRepository;
import com.healthy.backend.repository.RefreshTokenRepository;
import com.healthy.backend.repository.UserRepository;
import com.healthy.backend.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    
    private final AuthenticationRepository authenticationRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    private final JwtService jwtService;
    private final EmailService emailService;

    @Value("${jwt.refresh-token.expiration}")
    private long refreshTokenDuration;


    // Register new user
    public AuthenticationResponse register(RegisterRequest request) {
        if (authenticationRepository.findByUsername(request.getUsername()) != null) {
            throw new RuntimeException("Username already exists");
        }
        Users savedUser = userRepository.save(buildUserEntity(request));

        String accessToken = jwtService.generateToken(savedUser);
        String refreshToken = jwtService.generateRefreshToken(savedUser);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(savedUser.getUserId())
                .role(savedUser.getRole().toString())
                .build();
    }

    // Authentication
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        UsernamePasswordAuthenticationToken authToken;
        Users user;
        String username;

        if (request.getLoginIdentifier().contains("@")) {
            user = authenticationRepository.findByEmail(request.getLoginIdentifier());
        } else {
            user = authenticationRepository.findByUsername(request.getLoginIdentifier());
        }
        // Generate authToken
        assert user != null;
        username = user.getUsername();
        authToken = new UsernamePasswordAuthenticationToken(
                username,
                request.getPassword()
        );

        authenticationManager.authenticate(authToken);

        // Generate tokens
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        // Save refresh token to database
        saveRefreshToken(user.getUserId(), refreshToken);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getUserId())
                .role(user.getRole().toString())
                .build();
    }

    // Refresh token
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

        // Verify refresh token exists in database
        RefreshToken storedToken = refreshTokenRepository.findByHashedToken(hashToken(refreshToken));

        if (storedToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(storedToken);
            throw new RuntimeException("Refresh token expired");
        }

        // Generate new tokens
        String accessToken = jwtService.generateToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);

        // Update stored refresh token
        saveRefreshToken(user.getUserId(), newRefreshToken);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(newRefreshToken)
                .userId(user.getUserId())
                .role(user.getRole().toString())
                .build();
    }

    public void initiatePasswordReset(String email) {
        Users user = authenticationRepository.findByEmail(email);
        if (user == null) {
            // Don't reveal if email exists or not
            return;
        }

        // Generate password reset token
        String resetToken = UUID.randomUUID().toString();
        user.setResetToken(resetToken);
        authenticationRepository.save(user);

        // Send reset email
        String resetLink = "https://your-frontend-url/reset-password?token=" + resetToken;
        emailService.sendPasswordResetEmail(user.getEmail(), resetLink);
    }

    public void resetPassword(String token, String newPassword) {
        Users user = authenticationRepository.findByResetToken(token);
        if (user == null || !user.isResetTokenValid(token)) {
            throw new RuntimeException("Invalid or expired reset token");
        }

        // Update password and clear reset token
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        user.clearResetToken();

        // Save user
        authenticationRepository.save(user);

        // Optionally invalidate all refresh tokens for this user
        refreshTokenRepository.deleteByUserId(user.getUserId());
    }

    // Save refresh token to database
    private void saveRefreshToken(String userId, String refreshToken) {

        // Check if refresh token already exists
        if (!refreshTokenRepository.findByUserId(userId).isEmpty()) {
            // Delete any existing refresh tokens for this user if they exist
            refreshTokenRepository.deleteByUserId(userId);
        }

        // Create new refresh token
        RefreshToken token = new RefreshToken();
        token.setUserId(userId);
        token.setHashedToken(hashToken(refreshToken));
        token.setExpiresAt(LocalDateTime.now().plus(Duration.ofMillis(refreshTokenDuration)));

        refreshTokenRepository.save(token);
    }

    // Use a secure hashing algorithm
    private String hashToken(String token) {
        return passwordEncoder.encode(token);
    }

    // Convert User entity to UserResponse
    private Users buildUserEntity(RegisterRequest requestRequest) {
        return Users.builder()
                .userId(getUserLastCode())
                .username(requestRequest.getUsername())
                .passwordHash(passwordEncoder.encode(requestRequest.getPassword()))
                .fullName(requestRequest.getFullName())
                .email(requestRequest.getEmail())
                .phoneNumber(requestRequest.getPhoneNumber())
                .role(Users.UserRole.valueOf(requestRequest.getRole()))
                .gender(Users.Gender.valueOf(requestRequest.getGender()))
                .build();
    }

    // Generate user ID
    private String getUserLastCode() {
        if(userRepository.findAll().isEmpty()){
            return "US001";
        }
        String lastCode = userRepository.findLastUserId();
        if (lastCode == null || lastCode.length() < 3) {
            throw new IllegalArgumentException("Invalid last participation code");
        }
        String prefix = lastCode.substring(0, 2);
        int number = Integer.parseInt(lastCode.substring(2));
        return prefix + String.format("%03d", number + 1);
    }
}
