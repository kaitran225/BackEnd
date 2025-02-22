package com.healthy.backend.dto.psychologist;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
public class LeaveResponseDTO {
    private String requestId;
    private String psychologistName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
    private String status;
    private String department;
    private Date createdAt;
}