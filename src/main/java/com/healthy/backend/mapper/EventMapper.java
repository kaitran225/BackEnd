package com.healthy.backend.mapper;

import com.healthy.backend.dto.appointment.AppointmentResponse;
import com.healthy.backend.dto.event.EventDetails;
import com.healthy.backend.dto.event.EventResponse;
import com.healthy.backend.dto.event.ProgramWithStudents;
import com.healthy.backend.dto.programs.ProgramsResponse;
import com.healthy.backend.entity.Appointments;
import com.healthy.backend.entity.ProgramSchedule;
import com.healthy.backend.entity.Programs;
import com.healthy.backend.entity.Students;
import com.healthy.backend.exception.ResourceNotFoundException;
import com.healthy.backend.repository.ProgramParticipationRepository;
import com.healthy.backend.repository.ProgramScheduleRepository;
import com.healthy.backend.repository.StudentRepository;
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
    private final ProgramScheduleRepository programScheduleRepository;
    private final ProgramParticipationRepository programParticipationRepository;
    private final StudentRepository studentRepository;

    public EventResponse buildParentEventResponse(
            List<Appointments> appointments,
            List<ProgramWithStudents> programs,
            String userId
    ) {
        List<AppointmentResponse> appointmentResponses = appointments.stream()
                .map(appointment -> {
                    return appointmentMapper.buildAppointmentResponse(
                            appointment,
                            psychologistsMapper.buildPsychologistResponse(appointment.getPsychologist()),
                            studentMapper.buildStudentResponse(appointment.getStudent())
                    );
                })
                .toList();

        List<ProgramsResponse> programsResponses = programs.stream()
                .map(program -> {
                    return getProgramBasicResponse(program.getPrograms(), program.getStudents().getUser().getFullName());
                })
                .toList();

        Map<String, EventDetails> dateMap = dateParentMap(appointments, programs, appointmentResponses, programsResponses);

        return EventResponse.builder()
                .event(dateMap)
                .userId(userId)
                .build();
    }

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
                .map(this::getProgramBasicResponse)
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
                .map(this::getProgramBasicResponse)
                .toList();

        Map<String, EventDetails> dateMap = dateMap(appointments, programs, appointmentResponses, programsResponses);

        return EventResponse.builder()
                .event(dateMap)
                .userId(userId)
                .build();
    }

    public EventResponse buildEmptyEventResponse(
            List<Appointments> appointments,
            List<Programs> programs,
            String userId
    ) {
        List<AppointmentResponse> appointmentResponses = List.of();

        List<ProgramsResponse> programsResponses = List.of();

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
        // Return a detail list for each appointment
        List<AppointmentResponse> appointmentResponses = appointments.stream()
                .map(appointment -> {
                    return appointmentMapper.buildAppointmentResponse(
                            appointment,
                            psychologistsMapper.buildPsychologistResponse(appointment.getPsychologist()),
                            studentMapper.buildStudentResponse(appointment.getStudent())
                    );
                })
                .toList();

        // Return a detail list for each program
        List<ProgramsResponse> programsResponses = programs.stream()
                .map(p -> {
                    List<String> studentIDList = programParticipationRepository.findStudentIDsByProgramID(p.getProgramID());
                    List<Students> students = studentRepository.findAllById(studentIDList);
                    return getProgramDetailsResponse(p, students);
                })
                .toList();

        Map<String, EventDetails> dateMap = dateMap(appointments, programs, appointmentResponses, programsResponses);

        return EventResponse.builder()
                .event(dateMap)
                .userId(userId)
                .build();
    }
    private Map<String, EventDetails> dateParentMap(
            List<Appointments> appointments,
            List<ProgramWithStudents> programs,
            List<AppointmentResponse> appointmentResponses,
            List<ProgramsResponse> programsResponses) {

        Map<String, EventDetails> dateMap = new HashMap<>();

        for (Appointments appointment : appointments) {
            String date = appointment.getTimeSlot().getSlotDate().toString();
            dateMap.putIfAbsent(date, new EventDetails(new ArrayList<>(), new ArrayList<>()));
            dateMap.get(date).getAppointment().add(appointmentResponses.get(appointments.indexOf(appointment)));
        }
        for (int i = 0; i < programs.size(); i++) {
            String date = programs.get(i).getPrograms().getStartDate().toString();
            dateMap.putIfAbsent(date, new EventDetails(new ArrayList<>(), new ArrayList<>()));
            dateMap.get(date).getProgram().add(programsResponses.get(i));
        }
        return dateMap;
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

    private ProgramsResponse getProgramBasicResponse(Programs program) {
        if (program == null) throw new ResourceNotFoundException("Program not found");
        List<ProgramSchedule> programSchedule = programScheduleRepository.findByProgramID(program.getProgramID());
        return programMapper.buildBasicProgramResponse(program,
                programSchedule.getLast());
    }

    private ProgramsResponse getProgramBasicResponse(Programs program, String name) {
        if (program == null) throw new ResourceNotFoundException("Program not found");
        List<ProgramSchedule> programSchedule = programScheduleRepository.findByProgramID(program.getProgramID());
        return programMapper.buildBasicProgramResponse(program,
                programSchedule.getLast(), name);
    }

    private ProgramsResponse getProgramDetailsResponse(Programs program, List<Students> students) {
        if (program == null) throw new ResourceNotFoundException("Program not found");
        List<ProgramSchedule> programSchedule = programScheduleRepository.findByProgramID(program.getProgramID());
        return programMapper.buildProgramsDetailsResponse(program,
                students.stream().map(studentMapper::buildBasicStudentResponse).toList(), programSchedule.getLast());
    }
}