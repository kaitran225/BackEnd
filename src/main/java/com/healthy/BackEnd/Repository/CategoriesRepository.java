package com.healthy.BackEnd.Repository;

import com.healthy.BackEnd.Entity.Categories;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriesRepository extends JpaRepository<Categories, String> {
} 