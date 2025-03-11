package com.healthy.backend.mapper;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.healthy.backend.dto.article.ArticleResponse;
import com.healthy.backend.entity.Article;
import com.healthy.backend.entity.Tags;

@Component
public class ArticleMapper {
    public ArticleResponse mapToResponse(Article article) {
        return ArticleResponse.builder()
            .articleId(article.getArticleID())
            .authorName(article.getAuthor().getFullName())
            .tags(article.getArticleTag().stream().map(Tags :: getTagName).map(String :: toUpperCase).collect(Collectors.toSet()))
            .content(article.getContent())
            .title(article.getTitle())
            .likes(article.getLikes())
            .build();
    }
}