package com.heathly.BackEnd.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Categories")
public class Categories {
    
    @Id
    @Column(name = "CategoryID", length = 36)
    private String CategoryID;

    @Enumerated(EnumType.STRING)
    @Column(name = "CategoryName", nullable = false, unique = true, columnDefinition = "ENUM('Stress', 'Anxiety', 'Depression')")
    private CategoryName CategoryName;

    public enum CategoryName {
        Stress,
        Anxiety,
        Depression
    }
} 