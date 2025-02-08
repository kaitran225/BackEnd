package com.healthy.BackEnd.Repository;

import com.healthy.BackEnd.Entity.UserLogs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLogRepository extends JpaRepository<UserLogs, String> {
} 