//package com.healthy.BackEnd.Controller;
//
//import com.healthy.BackEnd.dto.auth.*;
//import com.healthy.BackEnd.Service.AuthenticationService;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/auth")
//@RequiredArgsConstructor
//public class AuthenticationController {
//
//    private final AuthenticationService authService;
//
//    @PostMapping("/register")
//    public ResponseEntity<AuthenticationResponse> register(
//            @RequestBody RegisterRequest request
//    ) {
//        return ResponseEntity.ok(authService.register(request));
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<AuthenticationResponse> authenticate(
//            @RequestBody AuthenticationRequest request
//    ) {
//        return ResponseEntity.ok(authService.authenticate(request));
//    }
//
//    @PostMapping("/refresh-token")
//    public ResponseEntity<AuthenticationResponse> refreshToken(
//            HttpServletRequest request,
//            HttpServletResponse response
//    ) {
//        return ResponseEntity.ok(authService.refreshToken(request, response));
//    }
//
//    @PostMapping("/forgot-password")
//    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
//        authService.initiatePasswordReset(email);
//        return ResponseEntity.ok("Password reset email sent if account exists");
//    }
//
//    @PostMapping("/reset-password")
//    public ResponseEntity<String> resetPassword(
//            @RequestParam String token,
//            @RequestParam String newPassword
//    ) {
//        authService.resetPassword(token, newPassword);
//        return ResponseEntity.ok("Password successfully reset");
//    }
//
//    @PostMapping("/logout")
//    public ResponseEntity<String> logout(HttpServletRequest request) {
//        authService.logout(request);
//        return ResponseEntity.ok("Successfully logged out");
//    }
//}