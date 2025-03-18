package com.healthy.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Article")
public class Article {

    @Id
    @Column(name = "ArticleID", length = 36)
    private String articleID;

    @Column(name = "Title", length = 100)
    private String title;

    @Column(name = "CreatedAt", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "Content", columnDefinition = "TEXT")
    private String content;

    @Builder.Default
    private int likes = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AuthorID", referencedColumnName = "UserID", nullable = false)
    private Users author;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "ArticleTags",
            joinColumns = @JoinColumn(name = "ArticleId", referencedColumnName = "ArticleID"),
            inverseJoinColumns = @JoinColumn(name = "TagId", referencedColumnName = "TagID")
    )
    @Builder.Default
    private Set<Tags> articleTag = new HashSet<>();


    public Article(String articleID, String title, String content, HashSet<Tags> articleTag, Users author) {
        this.articleID = articleID;
        this.title = title;
        this.content = content;
        this.articleTag = articleTag;
        this.author = author;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
} 