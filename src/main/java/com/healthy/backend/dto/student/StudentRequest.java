package com.healthy.backend.dto.student;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentRequest {
    @Schema(example = "S001")
    private String name;
    @Schema(example = "Doe")
    private String surname;
    @Schema(example = "stutdent@example.com")
    private String email;
    @Schema(example = "1234567890")
    private String phoneNumber;
}
