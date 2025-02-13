package com.healthy.backend.dto.programs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProgramParticipationRequest {
    @NotBlank(message = "Student ID is required")
    @Schema(example = "S001")
    String studentID;

    @NotBlank(message = "Program ID is required")
    @Schema(example = "PRG001")
    String programID;
}
