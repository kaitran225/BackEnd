package com.healthy.backend.dto.psychologist;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PsychologistRequest {

    private String departmentID;

    private Integer yearsOfExperience;
}
