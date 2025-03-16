package com.healthy.backend.dto.manager;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonInclude;

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