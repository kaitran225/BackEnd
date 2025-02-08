package com.healthy.BackEnd.DTO.Programs;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.healthy.BackEnd.Entity.ProgramParticipation;
import com.healthy.BackEnd.Entity.Programs;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProgramParticipationResponse {
    private String programID;
    private String programName;
    private Programs.Category category;
    private String description;
    private Integer numberParticipants;
    private Integer duration;
    private ProgramParticipation.Status status;
    private LocalDateTime createdAt;
    private String managedByStaffID;

}
