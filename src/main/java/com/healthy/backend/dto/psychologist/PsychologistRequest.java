package com.healthy.backend.dto.psychologist;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.healthy.backend.dto.user.UsersRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PsychologistRequest {

    private String departmentID;

    private Integer yearsOfExperience;
}
