package com.healthy.backend.controller;

import com.healthy.backend.service.AuthenticationService;
import com.healthy.backend.service.LogoutService;
import com.healthy.backend.dto.auth.AuthenticationRequest;
import com.healthy.backend.dto.auth.AuthenticationResponse;
import com.healthy.backend.dto.auth.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication management APIs")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final LogoutService logoutHandler;

    @Operation(summary = "Register new user", description = "Register a new user with the provided details")
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @Operation(summary = "Authenticate user", description = "Authenticate a user and return JWT tokens")
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @Operation(summary = "Refresh token", description = "Get a new access token using refresh token")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        try {
            AuthenticationResponse response = authenticationService.refreshToken(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired refresh token");
        }
    }

    @Operation(summary = "Initiate password reset", description = "Send password reset email to user")
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        try {
            authenticationService.initiatePasswordReset(email);
            return ResponseEntity.ok("Password reset email sent if account exists");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to send reset email: " + e.getMessage());
        }
    }

    @Operation(summary = "Reset password", description = "Reset password using token from email")
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @RequestParam String token,
            @RequestParam @Size(min = 8, message = "Password must be at least 8 characters long")
            @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",
                    message = "Password must contain at least one digit, one lowercase, one uppercase, and one special character")
            String newPassword) {
        try {
            authenticationService.resetPassword(token, newPassword);
            return ResponseEntity.ok("Password successfully reset");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to reset password: " + e.getMessage());
        }
    }

    @Operation(summary = "Logout user", description = "Logout the current user")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        logoutHandler.logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return ResponseEntity.ok("Successfully logged out");
    }
}
