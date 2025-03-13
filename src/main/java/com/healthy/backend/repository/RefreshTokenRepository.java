package com.healthy.backend.repository;

import com.healthy.backend.entity.RefreshToken;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    RefreshToken findByHashedToken(String hashedToken);

    RefreshToken findByUserId(String userId);

    @Modifying
    @Query("DELETE FROM RefreshToken r WHERE r.expiresAt <= :now")
    void deleteExpiredTokens(@Param("now") LocalDateTime now);

    @Transactional
    @Modifying
    void deleteByUserId(String userId);

} 