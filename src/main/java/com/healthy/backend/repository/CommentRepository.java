package com.healthy.backend.repository;

import com.healthy.backend.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByAppointment_AppointmentIDAndParentCommentIsNullOrderByCreatedAtAsc(String appointmentID);

    List<Comment> findByPrograms_ProgramIDAndParentCommentIsNullOrderByCreatedAtAsc(String programID);

    List<Comment> findByArticle_ArticleIDAndParentCommentIsNullOrderByCreatedAtAsc(String articleID);

    boolean existsByProgramsAndUserAndRatingIsNotNull(
            @Param("programs") Programs programs,
            @Param("user") Users user
    );

    boolean existsByArticleAndUserAndRatingIsNotNull(
            @Param("article") Article article,
            @Param("user") Users user
    );

    boolean existsByAppointmentAndUserAndRatingIsNotNull(
            @Param("appointment") Appointments appointment,
            @Param("user") Users user
    );

}
