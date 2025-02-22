package com.healthy.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.healthy.backend.entity.Categories;

public interface CategoriesRepository extends JpaRepository<Categories, String> {
    Categories findByCategoryName(Categories.MentalHealthCategory categoryName);
} 