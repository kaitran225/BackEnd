package com.healthy.backend.dto.manager;

import lombok.Getter;
import lombok.Builder;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationScheduleResponse {
    private LocalTime notificationTime;
    private DayOfWeek notificationDay;
    private String message;
}