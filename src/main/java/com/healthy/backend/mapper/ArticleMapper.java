package com.healthy.backend.mapper;

import com.healthy.backend.dto.article.ArticleResponse;
import com.healthy.backend.entity.Article;
import com.healthy.backend.entity.Tags;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ArticleMapper {
    public ArticleResponse mapToResponse(Article article) {
        return ArticleResponse.builder()
                .articleId(article.getArticleID())
                .authorName(article.getAuthor().getFullName())
                .tags(article.getArticleTag().stream().map(Tags::getTagName).map(String::toUpperCase).collect(Collectors.toSet()))
                .content(article.getContent())
                .title(article.getTitle())
                .likes(article.getLikes())
                .build();
    }
}