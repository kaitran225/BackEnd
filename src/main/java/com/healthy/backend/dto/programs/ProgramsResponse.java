package com.healthy.backend.dto.programs;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.healthy.backend.dto.student.StudentResponse;
import com.healthy.backend.entity.Programs;
import com.healthy.backend.entity.Students;
import com.healthy.backend.entity.Tags;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
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
    private Set<String> tags;
    private LocalDate createdAt;
    private Programs.Type type;
    private String meetingLink;
    private List<StudentResponse> enrolled;
}

//id: 14,
//title: "Mindful Leadership",
//description: "Develop leadership skills with a focus on mindfulness and emotional intelligence.",
//category: "Leadership",
//startDate: "2024-06-20",
//duration: "5 weeks",
//capacity: 20,
//enrolled: 13,
//status: "Open",
//facilitator: "Prof. John Anderson",
//tags: ["Leadership", "Mindfulness", "Personal Development"],