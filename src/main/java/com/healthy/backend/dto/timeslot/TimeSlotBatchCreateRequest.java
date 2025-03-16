package com.healthy.backend.dto.timeslot;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TimeSlotBatchCreateRequest {
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Schema(example = "")
    private LocalDate slotDate;

    @NotEmpty
    @Schema(example = "")
    private List<String> defaultSlotIds;
}