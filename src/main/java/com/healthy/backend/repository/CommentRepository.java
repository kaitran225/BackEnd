package com.healthy.backend.repository;

import com.healthy.backend.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // Sửa đổi truy vấn để sử dụng property "appointmentID" của entity Appointments
    List<Comment> findByAppointment_AppointmentIDAndParentCommentIsNullOrderByCreatedAtAsc(String appointmentID);
}
