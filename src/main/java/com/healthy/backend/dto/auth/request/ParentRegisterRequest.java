package com.healthy.backend.dto.auth.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.healthy.backend.dto.auth.ChildrenDetails;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ParentRegisterRequest extends RegisterRequest {

    @Schema
    @Valid
    private ChildrenDetails childrenDetails;

    @Override
    public String getRole() {
        return "PARENT";
    }

    @Override
    public void setRole(String role) {
        super.setRole(Objects.requireNonNullElse(role, "PARENT"));
    }
}
