package com.healthy.BackEnd.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Categories")
public class Categories {
    
    @Id
    @Column(name = "CategoryID", length = 36, nullable = false)
    private String categoryID;

    @Enumerated(EnumType.STRING)
    @Column(name = "CategoryName", nullable = false, unique = true, columnDefinition = "ENUM('Stress', 'Anxiety', 'Depression')")
    private CategoryName categoryName;

    public enum CategoryName {
        Stress,
        Anxiety,
        Depression
    }
} 