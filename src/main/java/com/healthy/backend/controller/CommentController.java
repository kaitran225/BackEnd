package com.healthy.backend.controller;

import com.healthy.backend.dto.comment.CommentRequest;
import com.healthy.backend.dto.comment.CommentResponse;
import com.healthy.backend.security.TokenService;
import com.healthy.backend.service.CommentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class CommentController {

    private final CommentService commentService;
    private final TokenService tokenService;

    /**
     * Endpoint để thêm comment hoặc reply cho 1 appointment.
     * Yêu cầu client truyền appointmentId trong URL.
     */
    @PostMapping("/appointments/{appointmentId}")
    public ResponseEntity<CommentResponse> addComment(
            @PathVariable String appointmentId,
            @RequestBody @Valid CommentRequest request,
            HttpServletRequest httpRequest) {
        // Lấy thông tin người dùng từ token (đảm bảo đã đăng nhập)
        String userId = tokenService.retrieveUser(httpRequest).getUserId();
        CommentResponse response = commentService.addComment(appointmentId, request, userId);
        return ResponseEntity.ok(response);
    }

    /**
     * Lấy danh sách bình luận dạng cây (bao gồm reply) của 1 appointment.
     */
    @GetMapping("/appointments/{appointmentId}")
    public ResponseEntity<List<CommentResponse>> getCommentsForAppointment(@PathVariable String appointmentId) {
        List<CommentResponse> responses = commentService.getCommentsForAppointment(appointmentId);
        return ResponseEntity.ok(responses);
    }
}
