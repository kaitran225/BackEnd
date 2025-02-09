package com.healthy.BackEnd.Repository;

import com.healthy.BackEnd.Entity.StudentNotes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentNoteRepository extends JpaRepository<StudentNotes, String> {
} 