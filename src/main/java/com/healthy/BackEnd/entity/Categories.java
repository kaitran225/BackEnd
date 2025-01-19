package com.healthy.BackEnd.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "Categories")
public class Categories {
    
    @Id
    @Column(name = "CategoryID", length = 36, nullable = false)
    private String categoryID;

    @Column(name = "CategoryName", length = 100, nullable = false)
    private String categoryName;

    public Categories(String categoryID, String categoryName) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
    }
} 