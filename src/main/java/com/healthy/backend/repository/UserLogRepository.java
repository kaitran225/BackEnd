package com.healthy.backend.repository;

import com.healthy.backend.entity.UserLogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLogRepository extends JpaRepository<UserLogs, String> {
} 