package com.healthy.backend.service;

import com.healthy.backend.dto.auth.request.*;
import com.healthy.backend.dto.auth.response.AuthenticationResponse;
import com.healthy.backend.dto.auth.response.VerificationResponse;
import com.healthy.backend.dto.user.UsersResponse;
import com.healthy.backend.entity.*;
import com.healthy.backend.enums.Role;
import com.healthy.backend.enums.UserType;
import com.healthy.backend.exception.InvalidTokenException;
import com.healthy.backend.exception.ResourceNotFoundException;
import com.healthy.backend.mapper.*;
import com.healthy.backend.repository.*;
import com.healthy.backend.security.JwtService;
import com.healthy.backend.security.TokenService;
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

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ParentRepository parentRepository;
    private final StudentRepository studentRepository;
    private final ResetTokenRepository resetTokenRepository;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PsychologistRepository psychologistsRepository;
    private final AuthenticationRepository authenticationRepository;

    private final GeneralService __;
    private final JwtService jwtService;
    private final TokenService tokenService;
    private final EmailService emailService;

    private final UserMapper usermapper;
    private final ParentMapper parentmapper;
    private final StudentMapper studentmapper;
    private final PsychologistsMapper psychologistsmapper;
    private final AuthenticationMapper authenticationMapper;

    @Value("${jwt.refresh-token.expiration}")
    private long refreshTokenDuration;

    @Value("${app.url}")
    private String siteURL;

    public AuthenticationResponse register(RegisterRequest request) {
        if (Role.valueOf(request.getRole()).equals(Role.PSYCHOLOGIST)) {
            return registerPsychologist((PsychologistRegisterRequest) request);
        }
        if (Role.valueOf(request.getRole()).equals(Role.PARENT)) {
            return registerParent((ParentRegisterRequest) request);
        }
        if (Role.valueOf(request.getRole()).equals(Role.STUDENT)) {
            return registerStudent((StudentRegisterRequest) request);
        }
        return registerUser(request);
    }

    private AuthenticationResponse registerPsychologist(PsychologistRegisterRequest request) {
        AuthenticationResponse response = registerUser(request);
        String psychologistId = __.generatePsychologistID();
        psychologistsRepository.save(
                psychologistsmapper.buildPsychologistEntity(request, userRepository.findById(response.getUserId()).orElseThrow(),
                        psychologistId)
        );
        response.setPsychologistId(psychologistId);
        return response;
    }

    public AuthenticationResponse registerPsychologist(PsychologistRegisterRequest request, HttpServletRequest httpServletRequest) {
        if(!tokenService.validateRole(httpServletRequest, Role.MANAGER)) {
            throw new RuntimeException("You don't have permission to register psychologist");
        };
        AuthenticationResponse response = registerUser(request);
        String psychologistId = __.generatePsychologistID();
        psychologistsRepository.save(
                psychologistsmapper.buildPsychologistEntity(request, userRepository.findById(response.getUserId()).orElseThrow(),
                        psychologistId)
        );
        response.setPsychologistId(psychologistId);
        return response;
    }

    public AuthenticationResponse registerParent(ParentRegisterRequest request) {
        AuthenticationResponse response = registerUser(request);

        List<Students> children = studentRepository.findAllById(request.getChildrenDetails().getStudentIds());
        String parentId = __.generateParentID();
        parentRepository.save(
                parentmapper.buildParentEntity(request, userRepository.findById(response.getUserId()).orElseThrow(),
                        parentId, children)
        );
        response.setParentId(parentId);
        return response;
    }

    public AuthenticationResponse registerStudent(StudentRegisterRequest request) {
        AuthenticationResponse response = registerUser(request);
        String studentId = __.generateStudentID();
        studentRepository.save(
                studentmapper.buildStudentEntity(request,
                        userRepository.findById(response.getUserId()).orElseThrow(),
                        studentId)
        );
        response.setStudentId(studentId);
        return response;
    }

    // Register user entity
    private AuthenticationResponse registerUser(RegisterRequest request) {
        String normalizedEmail = normalizeEmailAndPhone(request.getEmail());
        String normalizedPhone = normalizeEmailAndPhone(request.getPhoneNumber());
        validateUniqueUser(normalizedEmail, normalizedPhone);

        String token = jwtService.generateVerificationToken(normalizedEmail);
        String encodedPassword = passwordEncoder.encode(request.getPassword().trim());
        String hashedID = __(normalizedEmail);

        Users savedUser = userRepository.save(
                buildUserEntity(request, token, encodedPassword, hashedID)
        );

        if (isWhitelistedDomain(savedUser.getEmail())) {
            savedUser.setVerified(true);
            userRepository.save(savedUser);
            return buildAuthResponse(savedUser);
        }

        sendVerificationEmailIfNeeded(normalizedEmail, token, request.getFullName());
        return buildAuthResponse(savedUser);
    }

    // Shared helper methods
    private String normalizeEmailAndPhone(String input) {
        return input.trim().toLowerCase();
    }

    private void validateUniqueUser(String email, String phone) {
        if (authenticationRepository.existsByPhoneNumber(phone)) {
            throw new RuntimeException("This phone number is already in use for another account");
        }
        if (authenticationRepository.existsByEmail(email)) {
            throw new RuntimeException("This email is already in use for another account");
        }
    }

    private Users buildUserEntity(RegisterRequest request, String token, String encodedPassword, String hashedID) {
        return usermapper.buildUserEntity(request, token, __.generateUserID(), encodedPassword, hashedID);
    }

    private boolean isWhitelistedDomain(String email) {
        return email.contains("example");
    }

    private void sendVerificationEmailIfNeeded(String email, String token, String name) {
        String verificationUrl = buildVerificationUrl(token);
        emailService.sendVerificationEmail(email, verificationUrl, name);
    }

    private String buildVerificationUrl(String token) {
        return UriComponentsBuilder.fromUriString(siteURL)
                .path("/api/auth/verify")
                .queryParam("token", token)
                .toUriString();
    }

    private AuthenticationResponse buildAuthResponse(Users savedUser) {
        return AuthenticationResponse.builder()
                .userId(savedUser.getUserId())
                .role(savedUser.getRole().toString())
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        String email = request.getLoginIdentifier().trim();

        if (!email.contains("@")) {
            throw new BadCredentialsException("Invalid login credentials");
        }

        Users user = authenticationRepository.findByEmail(email.toLowerCase());
        // Check if user exists

        if (user == null) {
            throw new BadCredentialsException("User not found");
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
        final String username = jwtService.extractHashedID(refreshToken);

        // Validate username
        if (username == null) {
            throw new InvalidTokenException("Invalid refresh token: unable to extract username.");
        }
        Users user = authenticationRepository.findByHashedID(username);

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

        String accessToken = jwtService.generateToken(user);

        // Update stored refresh token
        saveRefreshToken(user.getUserId(), refreshToken);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
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
        emailService.sendForgotPasswordEmail(user.getEmail(), resetLink, user.getFullName());

        return true;
    }

    // Reset password
    public boolean resetPassword(String token, String newPassword) {

        if (!jwtService.isTokenValid(token)) {
            throw new InvalidTokenException("Invalid or expired password reset token.");
        }
        final String username = jwtService.extractHashedID(token);

        // Validate username
        if (username == null) {
            throw new InvalidTokenException("Invalid refresh token: unable to extract username.");
        }
        Users user = authenticationRepository.findByHashedID(username);

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

        Users user = userRepository.findByEmail(jwtService.extractVerificationEmail(token));

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

    public UsersResponse extractTokens(HttpServletRequest request) {
        return usermapper.buildBasicUserResponse(tokenService.retrieveUser(request));
    }

    // Save refresh token to database
    private void saveRefreshToken(String userId, String refreshToken) {

        if (refreshTokenRepository.findByUserId(userId) != null) {
            refreshTokenRepository.deleteByUserId(userId);
        }

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

    private String __(String email) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(email.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash); // Full-length Base64 hash
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating hash", e);
        }
    }
}
