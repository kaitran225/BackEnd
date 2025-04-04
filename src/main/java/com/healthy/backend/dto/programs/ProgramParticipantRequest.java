package com.healthy.backend.dto.programs;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProgramParticipantRequest {

    @Schema(example = "PRG001")
    private String programId;
    @Schema(example = "STU001")
    private String studentId;
}
