package com.healthy.BackEnd.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PsychologistDTO {
    private String psychologistId;
    private String userId;
    private String specialization;
    private Integer yearsOfExperience;
    private String status;
}
