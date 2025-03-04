package com.healthy.backend.dto.appointment;

import com.healthy.backend.dto.student.StudentReplyResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentFeedbackResponse {
    private LocalDateTime appointmentDateTime;
    private String studentName;
    private List<String> feedbackComments;
    private List<Long> feedbackIds;
    private Integer rating;
    private List<PsychologistCommentResponse> psychologistComments;

    @Data
    public static class PsychologistCommentResponse {
        private Long commentId;
        private String comment;
        private LocalDateTime createdAt;
        private List<StudentReplyResponse> studentReplies;

        public PsychologistCommentResponse(Long commentId, String comment, LocalDateTime createdAt, List<StudentReplyResponse> studentReplies) {
            this.commentId = commentId;
            this.comment = comment;
            this.createdAt = createdAt;
            this.studentReplies = studentReplies;
        }
    }
}