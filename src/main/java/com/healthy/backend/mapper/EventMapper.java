package com.healthy.backend.mapper;

import com.healthy.backend.dto.appointment.AppointmentResponse;
import com.healthy.backend.dto.event.EventDetails;
import com.healthy.backend.dto.programs.ProgramsResponse;
import com.healthy.backend.dto.event.EventResponse;
import com.healthy.backend.dto.psychologist.PsychologistResponse;
import com.healthy.backend.entity.Appointments;
import com.healthy.backend.entity.Programs;
import com.healthy.backend.entity.Psychologists;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class EventMapper {

    private final AppointmentMapper appointmentMapper;
    private final PsychologistsMapper psychologistsMapper;
    private final ProgramMapper programMapper;

    public EventResponse buildEventResponse(
            List<Appointments> appointments,
            List<Programs> programs,
            String studentId
    ) {
        Map<String, EventDetails> dateMap = new HashMap<>();

        List<AppointmentResponse> appointmentResponses = appointments.stream()
                .map(appointment -> {
                    PsychologistResponse psychologistResponse = psychologistsMapper.buildPsychologistResponse(appointment.getPsychologist());
                    return appointmentMapper.buildBasicAppointmentResponse(appointment, psychologistResponse);
                })
                .toList();


        List<ProgramsResponse> programsResponses = programs.stream()
                .map(programMapper::buildBasicProgramResponse)
                .toList();

//        for (Appointments appointment : appointments){
//            String date = appointment.getTimeSlot().getSlotDate().toString();
//            dateMap.putIfAbsent(date, new EventDetails(new ArrayList<>(), new ArrayList<>()));
//            dateMap.get(date).getAppointment().add(appointmentResponses.get(appointments.indexOf(appointment)));
//        }
        for (Programs program : programs) {
            String date = program.getStartDate().toString();
            dateMap.putIfAbsent(date, new EventDetails(new ArrayList<>(), new ArrayList<>()));
            dateMap.get(date).getProgram().add(programsResponses.get(programs.indexOf(program)));
        }
        return EventResponse.builder()
                .event(dateMap)
                .studentId(studentId)
                .build();
    }
}