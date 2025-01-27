package com.healthy.BackEnd.repository;

import com.healthy.BackEnd.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<Users, String> {

    @Query("SELECT u FROM Users u")
    List<Users> findAllUsers();

    @Query("SELECT u FROM Users u WHERE u.role != 'ADMIN'")
    List<Users> findAllUsersExceptAdmin();

    @Query("SELECT u FROM Users u WHERE u.role = 'ADMIN'")
    List<Users> findAllAdmins();

    @Query("SELECT u FROM Users u WHERE u.role = 'STUDENT'")
    List<Users> findAllStudents();

    @Query("SELECT u FROM Users u WHERE u.role = 'PARENT'")
    List<Users> findAllParents();

    @Query("SELECT u FROM Users u WHERE u.role = 'PSYCHOLOGIST'")
    List<Users> findAllPsychologists();

    Users findByUsername(String username);
} 