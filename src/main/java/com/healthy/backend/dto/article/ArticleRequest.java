package com.healthy.backend.dto.article;

import java.util.HashSet;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleRequest {
    private String title;
    private String content;
   @Builder.Default
    private HashSet<String> tags = new HashSet<>();
    private int likes;
}