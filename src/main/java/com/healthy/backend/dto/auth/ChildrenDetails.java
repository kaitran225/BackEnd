package com.healthy.backend.dto.auth;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChildrenDetails {
    @NotEmpty(message = "Student IDs cannot be empty")
    private List<String> studentIds;
}
