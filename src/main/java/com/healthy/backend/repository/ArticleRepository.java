package com.healthy.backend.repository;

import com.healthy.backend.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, String> {

    @Query("SELECT a.articleID FROM Article a ORDER BY a.articleID DESC LIMIT 1")
    String findLastArticleId();
    // List<Article> findByCategory(String category);

    // List<Article> findByAuthorId(String authorId);

    @Query("SELECT a FROM Article a WHERE a.author.userId = :authorId")
    List<Article> findArticleByAuthor(@Param("authorId") String authorId);


    @Query("SELECT a FROM Article a WHERE LOWER(a.content) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Article> searchByKeyWordInContent(@Param("keyword") String keyword);

    @Query("SELECT a FROM Article a WHERE LOWER(a.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Article> searchByKeyWordInTitle(@Param("keyword") String keyword);

} 