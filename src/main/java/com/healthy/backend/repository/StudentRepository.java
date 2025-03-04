package com.healthy.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.healthy.backend.entity.Students;

@Repository
public interface StudentRepository extends JpaRepository<Students, String> {

    @Query("SELECT sss.studentID FROM Students sss ORDER BY sss.studentID DESC LIMIT 1")
    String findLastStudentId();

    Students findByUserID(String userID);

    Students findByStudentID(String studentId);

    @Query("SELECT sss.studentID FROM Students sss")
    List<String> findAllStudentIds();

    @Query("SELECT COUNT(sss)  FROM Students sss")
    int countStudent();


} 