package com.healthy.backend.dto.article;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleResponse {
    private String articleId;
    private String title;
    private String content;
    private Set<String> tags;
    private String authorName;
    private int likes;
}