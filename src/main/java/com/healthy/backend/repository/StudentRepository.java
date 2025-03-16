package com.healthy.backend.repository;

import com.healthy.backend.entity.Students;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

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

    List<Students> findByParentID(String parentId);

    @Query("SELECT s FROM Students s WHERE s.studentID IN :studentIDs")
    List<Students> findStudentsByIds(@Param("studentIDs") List<String> studentIDs);

    @Query("SELECT s FROM Students s WHERE s.studentID IN :ids")
    Set<Students> findAllByIdAsSet(Iterable<String> ids);
}