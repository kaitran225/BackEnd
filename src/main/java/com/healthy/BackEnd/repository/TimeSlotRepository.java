package com.healthy.BackEnd.repository;

import com.healthy.BackEnd.entity.TimeSlots;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeSlotRepository extends JpaRepository<TimeSlots, String> {
} 