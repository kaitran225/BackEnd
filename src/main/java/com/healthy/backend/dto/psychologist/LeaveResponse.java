package com.healthy.backend.dto.psychologist;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LeaveResponse {
    @Schema(example = "LE001")
    private String requestId;
    @Schema(example = "PSY001")
    private String psychologistName;
    @Schema(example = "2025-01-01")
    private LocalDate startDate;
    @Schema(example = "2025-12-31")
    private LocalDate endDate;
    @Schema(example = "Reason")
    private String reason;
    @Schema(example = "Approved")
    private String status;
    @Schema(example = "Psychology")
    private String department;
    @Schema(example = "2025-01-01")
    private Date createdAt;
}