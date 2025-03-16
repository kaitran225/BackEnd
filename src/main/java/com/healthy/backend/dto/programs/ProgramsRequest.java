package com.healthy.backend.dto.programs;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProgramsRequest {
    @Schema(example = "Program Name")
    private String name;
    @Schema(example = "Program Description")
    private String description;
    @Schema(example = "10")
    private Integer numberParticipants;
    @Schema(example = "2")
    private Integer duration;
    @Schema(example = "2023-01-01")
    private String startDate;
    @Schema(example = "")
    private ProgramWeeklyScheduleRequest weeklyScheduleRequest;
    @Schema(example = "Active")
    private String status;
    @Schema(
            description = "A set of tags associated with the entity.",
            example = "[\"TAG001\", \"TAG002\", \"TAG003\"]"
    )
    private HashSet<String> tags;
    @Schema(example = "PSY001")
    private String facilitatorId;
    @Schema(example = "DPT001")
    private String departmentId;
    @Schema(example = "Online")
    private String type;
    @Schema(example = "https://zoom.us/j/123456789")
    private String meetingLink;
}
