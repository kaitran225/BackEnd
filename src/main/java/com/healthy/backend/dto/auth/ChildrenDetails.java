package com.healthy.backend.dto.auth;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChildrenDetails {
    @NotEmpty(message = "Student IDs cannot be empty")
    private Set<String> studentIds;
}
