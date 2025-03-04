package com.healthy.backend.repository;

import com.healthy.backend.entity.Appointments;
import com.healthy.backend.entity.Comment;
import com.healthy.backend.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByAppointment_AppointmentIDAndParentCommentIsNullOrderByCreatedAtAsc(String appointmentID);

    boolean existsByAppointmentAndAuthorAndRatingIsNotNull(
            @Param("appointment") Appointments appointment,
            @Param("author") Users author
    );

}
