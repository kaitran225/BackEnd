package com.healthy.backend.repository;

import com.healthy.backend.entity.TimeSlots;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeSlotRepository extends JpaRepository<TimeSlots, String> {
} 