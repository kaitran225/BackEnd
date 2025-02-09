package com.healthy.backend.repository;

import com.healthy.backend.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Blog, String> {
} 