package com.healthy.backend.mapper;

import com.healthy.backend.dto.programs.*;
import com.healthy.backend.dto.student.StudentResponse;
import com.healthy.backend.entity.*;
import com.healthy.backend.enums.ParticipationStatus;
import com.healthy.backend.enums.Role;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProgramMapper {

    public ProgramTagResponse buildProgramTagResponse(Tags tag) {
        return ProgramTagResponse.builder()
                .tagID(tag.getTagId())
                .tagName(tag.getTagName())
                .build();
    }

    public ProgramsResponse buildProgramsParticipantResponse(Programs program, List<StudentResponse> enrolled) {
        return ProgramsResponse.builder()
                .programID(program.getProgramID())
                .title(program.getProgramName())
                .facilitatorName(program.getPsychologists().getFullNameFromUser())
                .departmentName(program.getPsychologists().getDepartment().getName())
                .tags(program.getTags().stream().map(Tags::getTagName).map(String::toUpperCase).collect(Collectors.toSet()))
                .type(program.getType().toString())
                .enrolled(enrolled)
                .build();
    }

    public ProgramsResponse buildProgramsDetailsResponse(Programs program,
                                                         List<StudentResponse> enrolled, ProgramSchedule programSchedule) {
        return ProgramsResponse.builder()
                .programID(program.getProgramID())
                .title(program.getProgramName())
                .description(program.getDescription())
                .currentParticipants(enrolled.size())
                .maxParticipants(program.getNumberParticipants())
                .duration(program.getDuration())
                .status(program.getStatus().toString())
                .startDate(LocalDate.from(program.getStartDate()))
                .weeklySchedule(buildProgramWeeklyScheduleResponse(programSchedule))
                .facilitatorName(program.getPsychologists().getFullNameFromUser())
                .departmentName(program.getPsychologists().getDepartment().getName())
                .tags(program.getTags().stream().map(Tags::getTagName).map(String::toUpperCase).collect(Collectors.toSet()))
                .type(program.getType().toString())
                .meetingLink(program.getMeetingLink())
                .enrolled(enrolled) // Add enrolled students
                .build();
    }

    public ProgramsResponse buildProgramResponse(Programs program, Integer enrolledCount, ProgramSchedule programSchedule, String programStatus) {
        return ProgramsResponse.builder()
                .programID(program.getProgramID())
                .title(program.getProgramName())
                .description(program.getDescription())
                .duration(program.getDuration())
                .currentParticipants(enrolledCount)
                .maxParticipants(program.getNumberParticipants())
                .status(program.getStatus().toString())
                .startDate(LocalDate.from(program.getStartDate()))
                .weeklySchedule(buildProgramWeeklyScheduleResponse(programSchedule))
                .facilitatorName(program.getPsychologists().getFullNameFromUser())
                .departmentName(program.getPsychologists().getDepartment().getName())
                .tags(program.getTags().stream().map(Tags::getTagName).map(String::toUpperCase).collect(Collectors.toSet()))
                .type(program.getType().toString())
                .studentStatus(programStatus)
                .meetingLink(program.getMeetingLink())
                .build();
    }


    public ProgramsResponse buildBasicProgramResponse(Programs program, ProgramSchedule programSchedule) {
        return ProgramsResponse.builder()
                .programID(program.getProgramID())
                .title(program.getProgramName())
                .duration(program.getDuration())
                .status(program.getStatus().toString())
                .startDate(LocalDate.from(program.getStartDate()))
                .weeklySchedule(buildProgramWeeklyScheduleResponse(programSchedule))
                .type(program.getType().toString())
                .meetingLink(program.getMeetingLink())
                .build();
    }

    private ProgramWeeklyScheduleResponse buildProgramWeeklyScheduleResponse(ProgramSchedule programSchedule) {
        return ProgramWeeklyScheduleResponse.builder()
                .weeklyAt(programSchedule.getDayOfWeek())
                .startTime(programSchedule.getStartTime())
                .endTime(programSchedule.getEndTime())
                .build();
    }

    public ProgramParticipationResponse buildProgramParticipationResponse(ProgramParticipation participation) {
        return ProgramParticipationResponse.builder()
                .programID(participation.getProgram().getProgramID())
                .programName(participation.getProgram().getProgramName())
                .description(participation.getProgram().getDescription())
                .numberParticipants(participation.getProgram().getNumberParticipants())
                .duration(participation.getProgram().getDuration())
                .status(participation.getStatus().toString())
                .createdAt(participation.getProgram().getCreatedAt())
                .build();
    }
}
