package com.healthy.backend.repository;

import com.healthy.backend.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, String> {

    @Query("SELECT a.articleID FROM Article a ORDER BY a.articleID DESC LIMIT 1")
    String findLastArticleId();
} 