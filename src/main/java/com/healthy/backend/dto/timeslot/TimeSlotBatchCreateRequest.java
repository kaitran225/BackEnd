package com.healthy.backend.dto.timeslot;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

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