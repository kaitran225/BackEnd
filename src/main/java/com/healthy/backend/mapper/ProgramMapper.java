package com.healthy.backend.mapper;

import com.healthy.backend.dto.programs.ProgramParticipationResponse;
import com.healthy.backend.dto.programs.ProgramsResponse;
import com.healthy.backend.entity.ProgramParticipation;
import com.healthy.backend.entity.Programs;
import org.springframework.stereotype.Component;

@Component
public class ProgramMapper {

    public ProgramsResponse buildProgramResponse(Programs program) {
        return ProgramsResponse.builder()
                .programID(program.getProgramID())
                .programName(program.getProgramName())
                .category(program.getCategory())
                .description(program.getDescription())
                .numberParticipants(program.getNumberParticipants())
                .duration(program.getDuration())
                .status(program.getStatus())
                .createdAt(program.getCreatedAt())
                .managedByStaffID(program.getManagedByStaffID())
                .build();
    }

    public ProgramParticipationResponse buildProgramParticipationResponse(ProgramParticipation participation) {
        return ProgramParticipationResponse.builder()
                .programID(participation.getProgram().getProgramID())
                .programName(participation.getProgram().getProgramName())
                .category(participation.getProgram().getCategory())
                .description(participation.getProgram().getDescription())
                .numberParticipants(participation.getProgram().getNumberParticipants())
                .duration(participation.getProgram().getDuration())
                .status(participation.getStatus())
                .createdAt(participation.getProgram().getCreatedAt())
                .build();
    }
}
