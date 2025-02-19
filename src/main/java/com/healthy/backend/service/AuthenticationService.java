package com.healthy.backend.service;

import com.healthy.backend.dto.auth.*;
import com.healthy.backend.entity.*;
import com.healthy.backend.exception.InvalidTokenException;
import com.healthy.backend.exception.ResourceAlreadyExistsException;
import com.healthy.backend.exception.ResourceNotFoundException;
import com.healthy.backend.mapper.AuthenticationMapper;
import com.healthy.backend.mapper.StudentMapper;
import com.healthy.backend.mapper.UserMapper;
import com.healthy.backend.repository.*;
import com.healthy.backend.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationRepository authenticationRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final PsychologistRepository psychologistsRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final ResetTokenRepository resetTokenRepository;

    private final JwtService jwtService;
    private final EmailService emailService;
    private final UserMapper usermapper;
    private final StudentMapper studentmapper;
    private final AuthenticationMapper authenticationMapper;

    @Value("${jwt.refresh-token.expiration}")
    private long refreshTokenDuration;

    @Value("${app.url}")
    private String siteURL;

    // Register new user
    public AuthenticationResponse register(RegisterRequest request) {

        // Normalize inputs
        String normalizedEmail = request.getEmail().trim().toLowerCase();
        String normalizedUsername = request.getUsername().trim();
        String normalizedPhone = request.getPhoneNumber().trim();

        if (authenticationRepository.findByPhoneNumber(normalizedPhone) != null) {
            throw new RuntimeException("This phone number is already in use for another account");
        }

        if (authenticationRepository.findByUsername(normalizedUsername) != null) {
            throw new RuntimeException("This username is already taken");
        }

        if (authenticationRepository.findByEmail(normalizedEmail) != null) {
            throw new RuntimeException("This email is already in use for another account");
        }

        // Generate verification token and encode password
        String token = jwtService.generateVerificationToken(normalizedEmail);
        String encodedPassword = passwordEncoder.encode(request.getPassword().trim());

        Users savedUser = userRepository.save(
                usermapper.buildUserEntity(request, token, getUserLastCode(), encodedPassword));

        String verificationUrl = UriComponentsBuilder.fromUriString(siteURL)
                .path("/api/auth/verify")
                .queryParam("token", token)
                .toUriString();

        // Skip email verification for certain domains (e.g., example.com)
        if (savedUser.getEmail().contains("example")) {
            return AuthenticationResponse.builder()
                    .userId(savedUser.getUserId())
                    .role(savedUser.getRole().toString())
                    .build();
        }

        emailService.sendVerificationEmail(
                normalizedEmail,
                "Click the link to verify your email: " + verificationUrl,
                "Verify Your Account"
        );

        return AuthenticationResponse.builder()
                .userId(savedUser.getUserId())
                .role(savedUser.getRole().toString())
                .build();
    }
    public AuthenticationResponse registerStudent(StudentRegisterRequest request) {

        // Normalize inputs
        String normalizedEmail = request.getEmail().trim().toLowerCase();
        String normalizedUsername = request.getUsername().trim();
        String normalizedPhone = request.getPhoneNumber().trim();

        if (authenticationRepository.findByPhoneNumber(normalizedPhone) != null) {
            throw new RuntimeException("This phone number is already in use for another account");
        }

        if (authenticationRepository.findByUsername(normalizedUsername) != null) {
            throw new RuntimeException("This username is already taken");
        }

        if (authenticationRepository.findByEmail(normalizedEmail) != null) {
            throw new RuntimeException("This email is already in use for another account");
        }

        // Generate verification token and encode password
        String token = jwtService.generateVerificationToken(normalizedEmail);
        String encodedPassword = passwordEncoder.encode(request.getPassword().trim());

        Users savedUser = userRepository.save(
                usermapper.buildUserStudentEntity(request, token, getUserLastCode(), encodedPassword));

        Students savedStudent = studentRepository.save(studentmapper.buildStudentEntity(request, savedUser, getStudentLastCode()));

        String verificationUrl = UriComponentsBuilder.fromUriString(siteURL)
                .path("/api/auth/verify")
                .queryParam("token", token)
                .toUriString();

        // Skip email verification for certain domains (e.g., example.com)
        if (savedUser.getEmail().contains("example")) {
            return AuthenticationResponse.builder()
                    .userId(savedUser.getUserId())
                    .studentId(savedStudent.getStudentID())
                    .role(savedUser.getRole().toString())
                    .build();
        }

        emailService.sendVerificationEmail(
                normalizedEmail,
                "Click the link to verify your email: " + verificationUrl,
                "Verify Your Account"
        );

        return AuthenticationResponse.builder()
                .userId(savedUser.getUserId())
                .role(savedUser.getRole().toString())
                .build();
    }

    // Authentication
    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        String loginIdentifier = request.getLoginIdentifier().trim();

        Users user = loginIdentifier.contains("@")
                ? authenticationRepository.findByEmail(loginIdentifier.toLowerCase())
                : authenticationRepository.findByUsername(loginIdentifier);

        // Check if user exists
        if (user == null) {
            throw new BadCredentialsException("Invalid login credentials");
        }

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                user.getUsername(), request.getPassword()
        );

        authenticationManager.authenticate(authToken);

        // Generate tokens
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        // Save refresh token to database
        saveRefreshToken(user.getUserId(), refreshToken);

        // Return response based on role
        return switch (user.getRole()) {
            case STUDENT -> authenticationMapper.toAuthenticationResponse(
                    user, studentRepository.findByUserID(user.getUserId()), accessToken, refreshToken
            );
            case PSYCHOLOGIST -> authenticationMapper.toAuthenticationResponse(
                    user, psychologistsRepository.findByUserID(user.getUserId()), accessToken, refreshToken
            );
            default -> authenticationMapper.toAuthenticationResponse(user, accessToken, refreshToken);
        };
    }

    // Refresh token
    public AuthenticationResponse refreshToken(HttpServletRequest request) {
        // Extract Authorization header safely
        String authHeader = Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                .orElseGet(() -> request.getHeader(HttpHeaders.WWW_AUTHENTICATE));

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new InvalidTokenException("Invalid refresh token: missing or malformed.");
        }

        final String refreshToken = authHeader.substring(7);

        // Validate token before extracting username
        if (!jwtService.isTokenValid(refreshToken)) {
            throw new InvalidTokenException("Invalid or expired refresh token.");
        }
        final String username = jwtService.extractUsername(refreshToken);

        // Validate username
        if (username == null) {
            throw new InvalidTokenException("Invalid refresh token: unable to extract username.");
        }
        Users user = authenticationRepository.findByUsername(username);

        // Check if user exists
        if (user == null) {
            throw new ResourceNotFoundException("User not found.");
        }

        // Verify refresh token existence
        RefreshToken storedToken = refreshTokenRepository.findByUserId(user.getUserId());
        if (storedToken == null) {
            throw new InvalidTokenException("Refresh token not found.");
        }

        // Check if refresh token is matched
        if (!_check(refreshToken, storedToken.getHashedToken())) {
            throw new InvalidTokenException("Refresh token is not matched.");
        }

        // Check if refresh token is expired
        LocalDateTime expiresAt = storedToken.getExpiresAt();
        if (expiresAt == null || expiresAt.isBefore(LocalDateTime.now())) {
            refreshTokenRepository.deleteByUserId(user.getUserId());
            throw new InvalidTokenException("Refresh token has expired.");
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


    // Initiate password reset
    public boolean initiatePasswordReset(String email) {

        String normalizedEmail = email.toLowerCase().trim();
        Users user = authenticationRepository.findByEmail(normalizedEmail);

        if (user == null) {
            return false;
        }

        // Generate a secure reset token
        String resetToken = jwtService.generateToken(user);
        String hashedToken = hashToken(resetToken);

        // Remove any existing reset tokens for this user
        resetTokenRepository.deleteByUserId(user.getUserId());

        ResetToken tokenEntity = ResetToken.builder()
                .userId(user.getUserId())
                .hashedToken(hashedToken)
                .expiresAt(LocalDateTime.now().plusMinutes(30)) // Set the expiration time 30 minutes from now
                .build();

        resetTokenRepository.save(tokenEntity);

        // Generate password reset link
        String resetLink = UriComponentsBuilder.fromUriString(siteURL)
                .path("/resetPassword.html")
                .queryParam("token", resetToken)
                .toUriString();

        // Send password reset email
        emailService.sendForgotPasswordEmail(user.getEmail(), resetLink);

        return true;
    }


    // Reset password
    public boolean resetPassword(String token, String newPassword) {

        if (!jwtService.isTokenValid(token)) {
            throw new InvalidTokenException("Invalid or expired password reset token.");
        }
        final String username = jwtService.extractUsername(token);

        // Validate username
        if (username == null) {
            throw new InvalidTokenException("Invalid refresh token: unable to extract username.");
        }
        Users user = authenticationRepository.findByUsername(username);

        // Check if user exists
        if (user == null) {
            throw new ResourceNotFoundException("User not found.");
        }

        // Verify reset token existence
        ResetToken storedToken = resetTokenRepository.findByUserId(user.getUserId());
        if (storedToken == null) {
            throw new InvalidTokenException("Refresh token not found.");
        }

        // Check if the reset token is matched
        if (!_check(token, storedToken.getHashedToken())) {
            throw new InvalidTokenException("Invalid or expired password reset token.");
        }

        // Check if reset token is expired
        LocalDateTime expiresAt = storedToken.getExpiresAt();
        if (expiresAt == null || expiresAt.isBefore(LocalDateTime.now())) {
            resetTokenRepository.deleteByUserId(user.getUserId());
            throw new InvalidTokenException("Password reset token has expired.");
        }

        // Encode and update the new password
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(LocalDateTime.now());
        authenticationRepository.save(user);

        // Delete the used reset token
        resetTokenRepository.delete(storedToken);

        return true;
    }


    // Verify email
    public VerificationResponse verifyUser(String token) {

        if (!jwtService.isTokenValid(token)) {
            throw new RuntimeException("Invalid or expired token");
        }

        Users user = userRepository.findByEmail(jwtService.extractEmail(token));

        user.setVerified(true);
        userRepository.save(user);

        return new VerificationResponse("Email verified successfully. You can now log in.", token, true);
    }

    // Check if verification token is valid
    public boolean isVerificationTokenValid(String token) {
        return authenticationRepository.findByVerificationToken(token) != null;
    }

    // Check if verification token is expired
    public boolean isVerificationTokenExpired(String token) {
        return authenticationRepository.findByVerificationToken(token).getTokenExpiration().isAfter(LocalDateTime.now());
    }

    // Save refresh token to database
    private void saveRefreshToken(String userId, String refreshToken) {

        // Check if refresh token already exists
        if (refreshTokenRepository.findByUserId(userId) != null) {
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

    private boolean _check(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    // Generate user ID
    private String getUserLastCode() {
        if (userRepository.findAll().isEmpty()) {
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
    private String getStudentLastCode() {
        if (userRepository.findAll().isEmpty()) {
            return "S001";
        }
        String lastCode = studentRepository.findLastStudentId();
        if (lastCode == null || lastCode.length() < 3) {
            throw new IllegalArgumentException("Invalid last participation code");
        }
        String prefix = lastCode.substring(0, 1);
        int number = Integer.parseInt(lastCode.substring(1));
        return prefix + String.format("%03d", number + 1);
    }
}
