package com.healthy.BackEnd.repository;

import com.healthy.BackEnd.entity.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notifications, String> {
} 