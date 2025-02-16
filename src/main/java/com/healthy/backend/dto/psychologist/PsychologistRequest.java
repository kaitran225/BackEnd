package com.healthy.backend.dto.psychologist;

import com.healthy.backend.dto.user.UsersRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PsychologistRequest {
    @Schema(example = "PSY001")
    private String departmentID;
    @Schema(example = "John Smith")
    private Integer yearsOfExperience;
    @Schema(example = "Psychologist")
    private String status;
}
