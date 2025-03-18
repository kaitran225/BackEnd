package com.healthy.backend.dto.manager;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Map;

@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppointmentStatsResponse {
    private Map<String, Long> appointmentCounts; // Map of appointment status to count
}