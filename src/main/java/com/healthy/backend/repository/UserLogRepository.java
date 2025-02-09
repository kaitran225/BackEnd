package com.healthy.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLogRepository extends JpaRepository<UserLogs, String> {
} 