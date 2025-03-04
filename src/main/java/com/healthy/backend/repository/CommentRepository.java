package com.healthy.backend.repository;

import com.healthy.backend.entity.Appointments;
import com.healthy.backend.entity.Comment;
import com.healthy.backend.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // Sửa đổi truy vấn để sử dụng property "appointmentID" của entity Appointments
    List<Comment> findByAppointment_AppointmentIDAndParentCommentIsNullOrderByCreatedAtAsc(String appointmentID);

    boolean existsByAppointmentAndAuthorAndRatingIsNotNull(
            @Param("appointment") Appointments appointment,
            @Param("author") Users author
    );

}
