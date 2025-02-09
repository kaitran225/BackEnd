package com.healthy.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentNoteRepository extends JpaRepository<StudentNotes, String> {
} 