package com.healthy.BackEnd.repository;

import com.healthy.BackEnd.entity.UserLogs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLogRepository extends JpaRepository<UserLogs, String> {
} 