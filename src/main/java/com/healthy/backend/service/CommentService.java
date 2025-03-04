package com.healthy.backend.service;

import com.healthy.backend.dto.comment.CommentRequest;
import com.healthy.backend.dto.comment.CommentResponse;
import com.healthy.backend.entity.Appointments;
import com.healthy.backend.entity.Comment;
import com.healthy.backend.entity.Users;
import com.healthy.backend.exception.ResourceNotFoundException;
import com.healthy.backend.repository.AppointmentRepository;
import com.healthy.backend.repository.CommentRepository;
import com.healthy.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;

    /**
     * Thêm một bình luận hoặc reply cho appointment.
     * @param appointmentId ID của appointment
     * @param request Nội dung comment và thông tin reply (nếu có)
     * @param userId ID của người tạo comment
     * @return CommentResponse đã tạo
     */
    @Transactional
    public CommentResponse addComment(String appointmentId, CommentRequest request, String userId) {
        // Tìm appointment
        Appointments appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment không tồn tại"));

        // Tìm người dùng
        Users author = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User không tồn tại"));

        // Tạo comment mới
        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setAppointment(appointment);
        comment.setAuthor(author);

        // Nếu có parentCommentId => đây là reply cho bình luận đã có
        if (request.getParentCommentId() != null) {
            Comment parent = commentRepository.findById(request.getParentCommentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Bình luận cha không tồn tại"));
            comment.setParentComment(parent);
        }

        // Lưu vào database
        Comment savedComment = commentRepository.save(comment);
        return mapToResponse(savedComment);
    }

    /**
     * Lấy danh sách bình luận dạng cây (nested) cho một appointment
     * chỉ lấy các comment gốc (không có parent) sau đó build đệ quy cho reply.
     */
    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsForAppointment(String appointmentId) {
        List<Comment> comments = commentRepository.findByAppointment_AppointmentIDAndParentCommentIsNullOrderByCreatedAtAsc(appointmentId);
        return comments.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Hàm chuyển đổi entity Comment sang DTO CommentResponse, đệ quy build reply
    private CommentResponse mapToResponse(Comment comment) {
        List<CommentResponse> replyResponses = comment.getReplies().stream()
                .sorted((c1, c2) -> c1.getCreatedAt().compareTo(c2.getCreatedAt()))
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getAuthor().getFullName(),
                comment.getCreatedAt(),
                replyResponses
        );
    }
}
