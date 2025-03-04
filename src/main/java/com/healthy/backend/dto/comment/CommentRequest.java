package com.healthy.backend.dto.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequest {
    @NotBlank(message = "Content không được để trống")
    private String content;

    // ID của bình luận cha nếu muốn reply vào bình luận đã có (nếu null thì là bình luận gốc)
    private Long parentCommentId;
}
