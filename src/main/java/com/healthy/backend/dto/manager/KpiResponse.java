package com.healthy.backend.dto.manager;

import lombok.Getter;
import lombok.Builder;
import lombok.AllArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
public class KpiResponse {
    private String psychologistId;
    private int month;
    private int year;
    private int targetSlots;
    private String message;
}