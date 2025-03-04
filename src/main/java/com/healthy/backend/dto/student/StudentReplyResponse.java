package com.healthy.backend.dto.student;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class StudentReplyResponse {
    private Long replyId;
    private String comment;
    private LocalDateTime createdAt;
    private String studentName;

    public StudentReplyResponse(Long replyId, String comment, LocalDateTime createdAt, String studentName) {
        this.replyId = replyId;
        this.comment = comment;
        this.createdAt = createdAt;
        this.studentName = studentName;
    }
}