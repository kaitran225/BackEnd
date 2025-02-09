package com.healthy.BackEnd.Repository;

import com.healthy.BackEnd.Entity.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notifications, String> {
} 