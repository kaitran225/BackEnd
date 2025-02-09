package com.healthy.backend.repository;

import com.healthy.backend.entity.StudentNotes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentNoteRepository extends JpaRepository<StudentNotes, String> {
} 