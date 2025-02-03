package com.healthy.BackEnd.Repository;

import com.healthy.BackEnd.Entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Blog, String> {
} 