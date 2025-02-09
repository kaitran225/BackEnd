package com.healthy.backend.controller;

import com.healthy.backend.service.AuthenticationService;
import com.healthy.backend.service.LogoutService;
import com.healthy.backend.dto.auth.AuthenticationRequest;
import com.healthy.backend.dto.auth.AuthenticationResponse;
import com.healthy.backend.dto.auth.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication management APIs")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final LogoutService logoutHandler;

    @Operation(
        summary = "Register new user",
        description = "Register a new user with the provided details"
    )
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @Operation(
        summary = "Authenticate user",
        description = "Authenticate a user and return JWT tokens"
    )
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @Valid @RequestBody AuthenticationRequest request,
            HttpServletResponse response
    ) {
        AuthenticationResponse authenticationResponse = authenticationService.authenticate(request);
        return ResponseEntity.ok(authenticationResponse);
    }

    @Operation(
        summary = "Refresh token",
        description = "Get a new access token using refresh token"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        return ResponseEntity.ok(authenticationService.refreshToken(request));
    }

    @Operation(
        summary = "Initiate password reset",
        description = "Send password reset email to user"
    )
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(
            @RequestParam String email
    ) {
        authenticationService.initiatePasswordReset(email);
        return ResponseEntity.ok("Password reset email sent if account exists");
    }

    @Operation(
        summary = "Reset password",
        description = "Reset password using token from email"
    )
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @RequestParam String token,
            @RequestParam String newPassword
    ) {
        authenticationService.resetPassword(token, newPassword);
        return ResponseEntity.ok("Password successfully reset");
    }

    @Operation(
        summary = "Logout user",
        description = "Logout the current user"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        logoutHandler.logout(request,response,authentication);
        return ResponseEntity.ok("Successfully logged out");
    }
}