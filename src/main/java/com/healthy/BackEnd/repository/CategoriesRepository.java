package com.healthy.BackEnd.repository;

import com.healthy.BackEnd.entity.Categories;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriesRepository extends JpaRepository<Categories, String> {
} 