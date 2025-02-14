package com.healthy.backend.dto.psychologist;

import com.healthy.backend.dto.user.UsersRequest;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PsychologistRequest {
    private String specialization;
    private Integer yearsOfExperience;
    private String status;
}
