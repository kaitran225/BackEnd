package com.healthy.backend.dto.manager;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PsychologistStatsResponse {
    private String psychologistId;
    private String fullName;
    private double averageRating;
    private long appointmentCount;
}