package com.healthy.backend.repository;

import com.healthy.backend.entity.Periodic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PeriodicRepository extends JpaRepository<Periodic, String> {

    Periodic findByPeriodicID(String periodicID);
}
