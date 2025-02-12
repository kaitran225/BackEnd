package com.healthy.backend.dto.programs;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.healthy.backend.entity.Programs;
import com.healthy.backend.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProgramsResponse {
    private String programID;
    private String title;
    private String description;
    private Programs.Category category;
    private LocalDate startDate;
    private Integer duration;
    private Integer numberParticipants;
    private Programs.Status status;
    private String managedByStaffID;
    private Set<Tag> tags;
    private LocalDate createdAt;
}
