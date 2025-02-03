package com.healthy.BackEnd.Repository;

import com.healthy.BackEnd.Entity.TimeSlots;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeSlotRepository extends JpaRepository<TimeSlots, String> {
} 