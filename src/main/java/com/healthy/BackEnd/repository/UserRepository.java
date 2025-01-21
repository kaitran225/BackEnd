package com.healthy.BackEnd.repository;

import com.healthy.BackEnd.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, String> {

} 