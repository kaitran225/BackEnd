package com.healthy.backend.dto.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PsychologistDetails {

    @Schema(example = "DPT001")
    @NotNull(message = "Department ID is required")
    private String departmentID;

    @Schema(example = "10")
    @NotNull(message = "Years of experience is required")
    private int yearsOfExperience;
}
