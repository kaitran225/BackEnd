package com.healthy.BackEnd.repository;

import com.healthy.BackEnd.entity.StudentNotes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentNoteRepository extends JpaRepository<StudentNotes, String> {
} 