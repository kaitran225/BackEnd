package com.healthy.backend.repository;

import com.healthy.backend.entity.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notifications, String> {
} 