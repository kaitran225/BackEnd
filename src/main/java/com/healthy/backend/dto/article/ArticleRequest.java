package com.healthy.backend.dto.article;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.HashSet;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleRequest {
    @Schema(example = "Article Title")
    private String title;
    @Schema(example = "Article Description")
    private String content;
    @Schema(
            description = "A set of tags associated with the entity.",
            example = "[\"TAG001\", \"TAG002\", \"TAG003\"]"
    )
    private HashSet<String> tags;
    @Schema(hidden = true, example = "0")
    private int likes;
}