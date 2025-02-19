package com.healthy.backend.repository;

import com.healthy.backend.entity.ResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Repository
public interface ResetTokenRepository extends JpaRepository<ResetToken, Long> {

    ResetToken findByUserId(String userId);

    ResetToken findByHashedToken(String hashedToken);

    @Modifying
    @Query("DELETE FROM ResetToken r WHERE r.expiresAt <= :now")
    void deleteExpiredTokens(@Param("now") LocalDateTime now);

    @Transactional
    @Modifying
    void deleteByUserId(String userId);
}