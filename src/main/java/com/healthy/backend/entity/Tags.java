package com.healthy.backend.entity;

import java.util.Set;

import com.healthy.backend.enums.ProgramTags;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Tags")
public class Tags {

    @Id
    @Column(name = "TagID", length = 36, nullable = false)
    private String tagId;

    @Column(name = "TagName", length = 100, nullable = false)
    private String tagName;

    @ManyToMany(mappedBy = "tags")
    private Set<Programs> programs;

    @ManyToMany(mappedBy= "articleTag")
    private Set<Article> articles;



    public Tags(String tagId, String tagName) {
        this.tagId = tagId;
        this.tagName = tagName;
    }

    public Tags(String tagId, ProgramTags tagName) {
        this.tagId = tagId;
        this.tagName = tagName.toString().replace("_", " ");
    }
}