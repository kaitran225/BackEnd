package com.healthy.backend.entity;

import com.healthy.backend.enums.ProgramTags;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Tags")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Tags {

    @Id
    @Column(name = "TagID", length = 36, nullable = false)
    private String tagId;

    @Column(name = "TagName", length = 100, nullable = false)
    private String tagName;

    @ManyToMany(mappedBy = "tags")
    private Set<Programs> programs;

    @ManyToMany(mappedBy = "articleTag", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Article> articles = new HashSet<>();


    public Tags(String tagId, String tagName) {
        this.tagId = tagId;
        this.tagName = tagName;
    }

    public Tags(String tagId, ProgramTags tagName) {
        this.tagId = tagId;
        this.tagName = tagName.toString().replace("_", " ");
    }
}