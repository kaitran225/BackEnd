package com.healthy.backend.repository;

import com.healthy.backend.entity.Tags;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tags, String> {
}
