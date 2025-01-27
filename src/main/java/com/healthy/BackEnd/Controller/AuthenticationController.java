package com.healthy.BackEnd.Controller;

import com.healthy.BackEnd.Service.AuthenticationService;
import com.healthy.BackEnd.Service.LogoutService;
import com.healthy.BackEnd.dto.auth.AuthenticationRequest;
import com.healthy.BackEnd.dto.auth.AuthenticationResponse;
import com.healthy.BackEnd.dto.auth.RegisterRequest;
import com.healthy.BackEnd.entity.Users;
import com.healthy.BackEnd.repository.AuthenticationRepository;
import com.healthy.BackEnd.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication management APIs")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final LogoutService logoutHandler;
    private final UserRepository userRepository;

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
        return ResponseEntity.ok(authenticationService.refreshToken(request, response));
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