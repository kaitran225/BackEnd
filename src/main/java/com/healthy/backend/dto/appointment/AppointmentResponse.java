package com.healthy.backend.dto.appointment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.healthy.backend.dto.psychologist.PsychologistResponse;
import com.healthy.backend.dto.student.StudentResponse;
import com.healthy.backend.entity.TimeSlots;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppointmentResponse {

    @Schema(example = "APP001")
    private String appointmentID;

    @Schema(example = "TSL150601")
    private String timeSlotID;

    @Schema(example = "TimeSlotInfo")
    private TimeSlots timeSlotInfo;

    @Schema(example = "{" +
            "studentId='STU001'," +
            "studentName='John Doe'," +
            "}"
    )
    private StudentResponse studentResponse;

    @Schema(example = "{" +
            "psychologistId='PSY001'," +
            "specialization='Mental Specialist'," +
            "}"
    )
    private PsychologistResponse psychologistResponse;

    @Schema(example = "Active")
    private String Status;

    @Schema(example = "Notes")
    private String studentNotes;

    @Schema(example = "Notes")
    private String psychologistNotes;

    @Schema(example = "2023-01-01")
    private LocalDateTime CreatedAt;

    @Schema(example = "2023-01-01")
    private LocalDateTime UpdatedAt;
    @Schema(example = "2023-01-01 00:00:00")
    private LocalDateTime checkInTime;
    @Schema(example = "2023-01-01 00:00:00")
    private LocalDateTime checkOutTime;
    @Schema(example = "")
    private String psychologistID;
    @Schema(example = "")
    private String psychologistName;
    @Schema(example = "")
    private String studentID;
    @Schema(example = "")
    private String studentName;
    @Schema(example = "")
    private String startTime;
    @Schema(example = "")
    private String endTime;
    @Schema(example = "")
    private String slotDate;
    @Schema(example = "")
    private String cancelReason;
}