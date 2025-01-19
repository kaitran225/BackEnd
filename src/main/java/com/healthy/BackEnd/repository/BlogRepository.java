package com.healthy.BackEnd.repository;

import com.healthy.BackEnd.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Blog, String> {
} 