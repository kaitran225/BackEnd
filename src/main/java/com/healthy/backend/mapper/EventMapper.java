package com.healthy.backend.mapper;

import com.healthy.backend.dto.appointment.AppointmentResponse;
import com.healthy.backend.dto.event.EventDetails;
import com.healthy.backend.dto.event.EventResponse;
import com.healthy.backend.dto.programs.ProgramsResponse;
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
    private final StudentMapper studentMapper;
    private final ProgramMapper programMapper;

    public EventResponse buildStudentEventResponse(
            List<Appointments> appointments,
            List<Programs> programs,
            String userId
    ) {

        List<AppointmentResponse> appointmentResponses = appointments.stream()
                .map(appointment -> {
                    return appointmentMapper.buildBasicStudentAppointmentResponse(
                            appointment,
                            psychologistsMapper.buildPsychologistResponse(appointment.getPsychologist())
                    );
                })
                .toList();

        List<ProgramsResponse> programsResponses = programs.stream()
                .map(programMapper::buildBasicProgramResponse)
                .toList();

        Map<String, EventDetails> dateMap = dateMap(appointments, programs, appointmentResponses, programsResponses);
        return EventResponse.builder()
                .event(dateMap)
                .userId(userId)
                .build();
    }

    public EventResponse buildPsychologistEventResponse(
            List<Appointments> appointments,
            List<Programs> programs,
            String userId
    ) {

        List<AppointmentResponse> appointmentResponses = appointments.stream()
                .map(appointment -> {
                    return appointmentMapper.buildBasicPsychologistAppointmentResponse(
                            appointment,
                            studentMapper.buildStudentResponse(appointment.getStudent())
                    );
                })
                .toList();

        List<ProgramsResponse> programsResponses = programs.stream()
                .map(programMapper::buildBasicProgramResponse)
                .toList();

        Map<String, EventDetails> dateMap = dateMap(appointments, programs, appointmentResponses, programsResponses);

        return EventResponse.builder()
                .event(dateMap)
                .userId(userId)
                .build();
    }

    public EventResponse buildManagerEventResponse(
            List<Appointments> appointments,
            List<Programs> programs,
            String userId
    ) {

        List<AppointmentResponse> appointmentResponses = appointments.stream()
                .map(appointment -> {
                    return appointmentMapper.buildBasicAppointmentResponse(
                            appointment,
                            studentMapper.buildStudentResponse(appointment.getStudent()),
                            psychologistsMapper.buildPsychologistResponse(appointment.getPsychologist())
                    );
                })
                .toList();

        List<ProgramsResponse> programsResponses = programs.stream()
                .map(programMapper::buildProgramResponse)
                .toList();

        Map<String, EventDetails> dateMap = dateMap(appointments, programs, appointmentResponses, programsResponses);

        return EventResponse.builder()
                .event(dateMap)
                .userId(userId)
                .build();
    }


    private Map<String, EventDetails> dateMap(
            List<Appointments> appointments,
            List<Programs> programs,
            List<AppointmentResponse> appointmentResponses,
            List<ProgramsResponse> programsResponses) {

        Map<String, EventDetails> dateMap = new HashMap<>();

        for (Appointments appointment : appointments) {
            String date = appointment.getTimeSlot().getSlotDate().toString();
            dateMap.putIfAbsent(date, new EventDetails(new ArrayList<>(), new ArrayList<>()));
            dateMap.get(date).getAppointment().add(appointmentResponses.get(appointments.indexOf(appointment)));
        }
        for (Programs program : programs) {
            String date = program.getStartDate().toString();
            dateMap.putIfAbsent(date, new EventDetails(new ArrayList<>(), new ArrayList<>()));
            dateMap.get(date).getProgram().add(programsResponses.get(programs.indexOf(program)));
        }
        return dateMap;
    }

    ;
}