package com.healthy.backend.dto.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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

    @Max(value = 100, message = "Years of experience must be at most 100")
    @Min(value = 1, message = "Years of experience must be at least 1")
    @Schema(example = "10")
    @NotNull(message = "Years of experience is required")
    private int yearsOfExperience;
}
