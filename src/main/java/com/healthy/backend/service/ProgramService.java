package com.healthy.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthy.backend.dto.appointment.AppointmentResponse;
import com.healthy.backend.dto.programs.*;
import com.healthy.backend.dto.student.StudentResponse;
import com.healthy.backend.entity.*;
import com.healthy.backend.enums.ParticipationStatus;
import com.healthy.backend.enums.ProgramStatus;
import com.healthy.backend.enums.ProgramType;
import com.healthy.backend.enums.TimeslotStatus;
import com.healthy.backend.exception.OperationFailedException;
import com.healthy.backend.exception.ResourceAlreadyExistsException;
import com.healthy.backend.exception.ResourceNotFoundException;
import com.healthy.backend.mapper.AppointmentMapper;
import com.healthy.backend.mapper.ProgramMapper;
import com.healthy.backend.mapper.StudentMapper;
import com.healthy.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProgramService {

    private final UserRepository userRepository;
    private final TagsRepository tagsRepository;
    private final ProgramRepository programRepository;
    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;
    private final PsychologistRepository psychologistRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final DefaultTimeSlotRepository defaultTimeSlotRepository;
    private final ProgramScheduleRepository programScheduleRepository;
    private final ProgramParticipationRepository programParticipationRepository;
    private final AppointmentRepository appointmentRepository;

    private final ProgramMapper programMapper;
    private final StudentMapper studentMapper;
    private final AppointmentMapper appointmentMapper;

    private final GeneralService __;
    private final NotificationService notificationService;
    private final EmailService emailService;
    private final Map<String, LocalTime> DEFAULT_SLOT_IDS = Map.ofEntries(
            // Morning
            Map.entry("MORNING-00", LocalTime.of(8, 0)), Map.entry("MORNING-01", LocalTime.of(8, 30)),
            Map.entry("MORNING-02", LocalTime.of(9, 0)), Map.entry("MORNING-03", LocalTime.of(9, 30)),
            Map.entry("MORNING-04", LocalTime.of(10, 0)), Map.entry("MORNING-05", LocalTime.of(10, 30)),
            // Afternoon
            Map.entry("AFTERNOON-00", LocalTime.of(13, 0)), Map.entry("AFTERNOON-01", LocalTime.of(13, 30)),
            Map.entry("AFTERNOON-02", LocalTime.of(14, 0)), Map.entry("AFTERNOON-03", LocalTime.of(14, 30)),
            Map.entry("AFTERNOON-04", LocalTime.of(15, 0)), Map.entry("AFTERNOON-05", LocalTime.of(15, 30)),
            Map.entry("AFTERNOON-06", LocalTime.of(16, 0)), Map.entry("AFTERNOON-07", LocalTime.of(16, 30))
    );

    private static int calculateDurationInWeeks(LocalDate startDate, LocalDate endDate) {
        long weeksBetween = ChronoUnit.WEEKS.between(startDate, endDate);
        return (int) weeksBetween;
    }

    public List<ProgramsResponse> getAllProgramsDetails() {

        List<Programs> programs = programRepository.findAll();
        if (programs.isEmpty()) throw new ResourceNotFoundException("No programs found");
        return programs.stream().map(this::getProgramDetailsResponse).toList();
    }

    public List<ProgramsResponse> getAllPrograms(String studentID) {
        List<Programs> programs = programRepository.findAll();
        if (programs.isEmpty()) throw new ResourceNotFoundException("No programs found");
        return programs.stream().map(program -> {
            return getProgramResponse(program, studentID);
        }).toList();
    }

    public ProgramTagResponse createProgramTag(ProgramTagRequest programTagRequest) {
        if (tagsRepository.existsByTagName(programTagRequest.getTagName())) {
            throw new ResourceAlreadyExistsException("Tag already exists");
        }
        String tagId = __.generateTagID();
        Tags newTag = new Tags(tagId, programTagRequest.getTagName());
        tagsRepository.save(newTag);
        return programMapper.buildProgramTagResponse(newTag);
    }

    private boolean checkIfTimeSlotExists(String facilitatorId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        Psychologists psychologist = psychologistRepository.findById(facilitatorId).orElseThrow(() -> new ResourceNotFoundException("Facilitator not found"));
        return timeSlotRepository.existsByPsychologistAndSlotDateAndStartTimeAndEndTime(psychologist, date, startTime, endTime);
    }

    private void createMissingTimeSlots(String facilitatorId, ProgramsRequest programsRequest) {
        List<LocalDate> scheduleDates = generateWeeklySchedule(
                programsRequest.getStartDate(),
                programsRequest.getDuration(),
                programsRequest.getWeeklyScheduleRequest().getWeeklyAt());

        LocalTime programStartTime = parseTime(programsRequest.getWeeklyScheduleRequest().getStartTime());
        LocalTime programEndTime = parseTime(programsRequest.getWeeklyScheduleRequest().getEndTime());

        for (LocalDate date : scheduleDates) {
            List<String> defaultSlotIds = getDefaultSlotIds(programStartTime, programEndTime);
            if (!checkIfTimeSlotExists(facilitatorId, date, programStartTime, programEndTime) || defaultSlotIds.isEmpty()) {
                createTimeSlotsFromDefaults(
                        facilitatorId,
                        date,
                        defaultSlotIds
                );
            }
        }
    }

    @Transactional
    private void setTimeSlotUnavailable(String facilitatorId, ProgramsRequest programsRequest) {
        List<LocalDate> scheduleDates = generateWeeklySchedule(
                programsRequest.getStartDate(),
                programsRequest.getDuration(),
                programsRequest.getWeeklyScheduleRequest().getWeeklyAt());

        LocalTime programStartTime = parseTime(programsRequest.getWeeklyScheduleRequest().getStartTime());
        LocalTime programEndTime = parseTime(programsRequest.getWeeklyScheduleRequest().getEndTime());

        List<TimeSlots> timeSlots = new ArrayList<>();
        for (LocalDate date : scheduleDates) {
            timeSlots.addAll(timeSlotRepository.findByPsychologistIdAndDate(facilitatorId, date));
        }

        List<TimeSlots> occupiedSlots = timeSlots.stream()
                .filter(slot -> slot.getStartTime().isBefore(programEndTime) && slot.getEndTime().isAfter(programStartTime))
                .collect(Collectors.toList());

        occupiedSlots.forEach(slot -> slot.setStatus(TimeslotStatus.UNAVAILABLE));

        timeSlotRepository.saveAll(occupiedSlots);
    }

    private boolean isModulo30(LocalTime time) {
        return time.getMinute() % 30 == 0;
    }

    public ProgramsResponse createProgram(ProgramsRequest programsRequest, String userId) {
        String programId = __.generateProgramID();
        Users staffUser = fetchUser(userId);
        HashSet<Tags> tags = fetchTags(programsRequest.getTags());
        Department department = fetchDepartment(programsRequest.getDepartmentId());
        Psychologists facilitator = fetchFacilitator(programsRequest.getFacilitatorId());

        validateTimeRequest(programsRequest);

        validateFacilitatorAvailability(facilitator.getPsychologistID(), programsRequest);

        createMissingTimeSlots(facilitator.getPsychologistID(), programsRequest);

        setTimeSlotUnavailable(facilitator.getPsychologistID(), programsRequest);

        Programs program = buildProgram(programId, programsRequest, department, facilitator, tags, staffUser);


        validateParticipantCount(programId, programsRequest.getNumberParticipants());

        ProgramSchedule programSchedule = createProgramSchedule(
                program,
                programsRequest.getWeeklyScheduleRequest().getWeeklyAt(),
                programsRequest.getWeeklyScheduleRequest().getStartTime(),
                programsRequest.getWeeklyScheduleRequest().getEndTime()
        );

        saveProgramAndSchedule(program, programSchedule);

        return getProgramById(programId, null);
    }

    public void _createProgram(ProgramsRequest programsRequest, Users user) {
        String programId = __.generateProgramID();
        Users staffUser = fetchUser(user.getUserId());
        HashSet<Tags> tags = fetchTags(programsRequest.getTags());
        Department department = fetchDepartment(programsRequest.getDepartmentId());
        Psychologists facilitator = fetchFacilitator(programsRequest.getFacilitatorId());

        validateTimeRequest(programsRequest);

        validateFacilitatorAvailability(facilitator.getPsychologistID(), programsRequest);

        createMissingTimeSlots(facilitator.getPsychologistID(), programsRequest);

        setTimeSlotUnavailable(facilitator.getPsychologistID(), programsRequest);

        Programs program = buildProgram(programId, programsRequest, department, facilitator, tags, staffUser);


        validateParticipantCount(programId, programsRequest.getNumberParticipants());

        ProgramSchedule programSchedule = createProgramSchedule(
                program,
                programsRequest.getWeeklyScheduleRequest().getWeeklyAt(),
                programsRequest.getWeeklyScheduleRequest().getStartTime(),
                programsRequest.getWeeklyScheduleRequest().getEndTime()
        );
        saveProgramAndSchedule(program, programSchedule);
    }

    public ProgramsResponse getProgramById(String programId, String studentID) {
        Programs program = programRepository.findById(programId).orElse(null);
        if (program == null) throw new ResourceNotFoundException("Program not found");
        return getProgramResponse(program, studentID);
    }

    public List<ProgramsResponse> getFacilitatorPrograms(Psychologists psychologists) {
        List<Programs> programsList = programRepository.findByFacilitatorID(psychologists.getPsychologistID());
        if (programsList.isEmpty()) {
            return List.of();
        }
        return programsList.stream().map(
                program -> {
                    ProgramSchedule programSchedule = programScheduleRepository.findFirstByProgramIDOrderByScheduleIDDesc(program.getProgramID());
                    return programMapper.buildProgramsDetailsResponse(
                            program,
                            getActiveStudentsByProgram(program.getProgramID()),
                            programSchedule
                    );
                }
        ).toList();
    }

    public List<ProgramTagResponse> getProgramTags() {
        List<Tags> tags = tagsRepository.findAll();
        if (tags.isEmpty()) throw new ResourceNotFoundException("No tags found");
        return tags.stream().map(programMapper::buildProgramTagResponse).toList();
    }

    private String convertToJson(List<Appointments> overlappingAppointments) {
        try {
            List<AppointmentResponse> responses = overlappingAppointments.stream()
                    .map(appointmentMapper::buildAppointmentResponse)
                    .toList();

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(responses);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting appointments to JSON", e);
        }
    }

    private boolean isOverlapping(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
        return start1.isBefore(end2) && end1.isAfter(start2);
    }

    public boolean registerForProgram(String programId, String studentId) {

        Programs program = programRepository.findById(programId)
                .orElseThrow(() -> new ResourceNotFoundException("Program not found"));

        validateStudentAvailability(studentId, program);

        String programParticipationId = __.generateParticipantID();
        programParticipationRepository.save(
                new ProgramParticipation(
                        programParticipationId,
                        studentId,
                        programId,
                        ParticipationStatus.JOINED,
                        LocalDate.now()
                )
        );

        Students students = studentRepository.findByStudentID(studentId);
        // Send notification
        notificationService.createProgramNotification(
                students.getUserID(),
                "New Program Registration",
                "You have a new program registration for " + program.getProgramName(),
                program.getProgramID());
        // Send email
        emailService.sendRegistrationConfirmationEmail(
                students.getUser().getEmail(),
                program,
                programScheduleRepository.findByProgramID(programId).getLast(),
                students.getUser().getFullName()
        );
        return programParticipationRepository.findById(programParticipationId).isPresent();
    }

    public String getProgramStatus(String programId) {
        Programs program = programRepository.findById(programId)
                .orElseThrow(() -> new ResourceNotFoundException("Program not found"));
        return program.getStatus().name();
    }

    public boolean cancelParticipation(String programId, String studentId) {

        if (!programRepository.existsById(programId)) {
            throw new ResourceNotFoundException("Program not found");
        }
        if (!isJoined(programId, studentId)) {
            throw new ResourceNotFoundException("Participation not found");
        }
        ProgramParticipation participation = programParticipationRepository.    findFirstByProgramIDAndStudentIDOrderByParticipationIDDesc(
                programId,
                studentId);
        if (participation.getStatus().equals(ParticipationStatus.CANCELLED))
            throw new ResourceAlreadyExistsException("Participation is already cancelled");

        if (participation.getStatus().equals(ParticipationStatus.COMPLETED)) {
            throw new ResourceAlreadyExistsException("Participation is already completed");
        }

        participation.setStatus(ParticipationStatus.CANCELLED);
        ProgramParticipation updatedParticipation = programParticipationRepository.save(participation);

        Programs program = updatedParticipation.getProgram();
        Students students = participation.getStudent();

        notificationService.createProgramNotification(
                participation.getStudent().getUser().getUserId(),
                "Program Cancellation",
                "You have cancel program registration for " + participation.getProgram().getProgramName(),
                participation.getProgram().getProgramID());

        // Send emails
        emailService.sendProgramCancellationEmail(
                students.getUser().getEmail(),
                program,
                students.getUser().getFullName()
        );
        return updatedParticipation.getStatus().equals(ParticipationStatus.CANCELLED);
    }

    public List<ProgramsResponse> getEnrolledPrograms(String studentId) {
        List<ProgramParticipation> participationList = programParticipationRepository.findByStudentID(studentId).stream()
                .filter(participation -> !participation.getStatus().equals(ParticipationStatus.CANCELLED))
                .toList();

        if (participationList.isEmpty()) {
            return new ArrayList<>();
        }

        return participationList.stream()
                .map(participation -> {
                    Programs program = participation.getProgram();
                    return getProgramResponse(program, studentId);
                }).toList();
    }

    @Transactional
    public boolean deleteProgram(String programId) {
        if (programRepository.existsById(programId)) {
            throw new ResourceNotFoundException("Program not found");
        }
        if (!programRepository.existsById(programId)) {
            throw new ResourceNotFoundException("Program not found with ID: " + programId);
        }

        Programs program = programRepository.findById(programId).orElseThrow(() -> new ResourceNotFoundException("Program not found"));
        program.setStatus(ProgramStatus.DELETED);
        programRepository.save(program);

        if (programRepository.findById(programId).isPresent()) return false;
        return programRepository.findById(programId).isEmpty();
    }


    public ProgramsResponse updateProgram(String programId, ProgramUpdateRequest updateRequest) {
        Programs program = fetchProgram(programId);

        validateProgramData(program, updateRequest);

        Set<Tags> tags = fetchTags(updateRequest.getTags());

        updateAndSaveProgramDetails(program, updateRequest, tags);
        updateProgramSchedule(programScheduleRepository.findFirstByProgramIDOrderByScheduleIDDesc(programId), updateRequest);

        return getProgramResponse(program, null);
    }

    private List<StudentResponse> getActiveStudentsByProgram(String programId) {
        List<String> studentIDs = programParticipationRepository.findActiveStudentIDsByProgramID(programId, ParticipationStatus.CANCELLED);
        List<Students> students = studentRepository.findStudentsByIds(studentIDs);
        List<ProgramParticipation> programParticipations = programParticipationRepository
                .findProgramParticipationsByProgramIdAndStudentIds(programId, studentIDs);
        Map<String, ProgramParticipation> studentProgramParticipationMap = programParticipations.stream()
                .collect(Collectors.toMap(
                        ProgramParticipation::getStudentID,
                        participation -> participation
                ));

        return students.stream()
                .map(studentMapper::buildStudentResponse)
                .peek(studentResponse -> {
                    ProgramParticipation programParticipation = studentProgramParticipationMap.get(studentResponse.getStudentId());
                    if (programParticipation != null) {
                        studentResponse.setProgramStatus(programParticipation.getStatus().name());
                    }
                })
                .collect(Collectors.toList());
    }

    private Users fetchUser(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID " + userId + " not found."));
    }

    private HashSet<Tags> fetchTags(Set<String> tagIds) {
        return tagIds.stream()
                .map(String::toUpperCase)
                .map(tag -> tagsRepository.findById(tag)
                        .orElseThrow(() -> new ResourceNotFoundException("Tag not found with ID: " + tag)))
                .collect(Collectors.toCollection(HashSet::new)); // Guarantees HashSet<Tags>
    }

    private Department fetchDepartment(String departmentId) {
        return departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + departmentId));
    }

    private Psychologists fetchFacilitator(String facilitatorId) {
        return psychologistRepository.findById(facilitatorId)
                .orElseThrow(() -> new ResourceNotFoundException("Psychologist not found with ID: " + facilitatorId));
    }

    private Programs buildProgram(String programId, ProgramsRequest request, Department department,
                                  Psychologists facilitator, HashSet<Tags> tags, Users users) {
        return new Programs(
                programId,
                request.getName(),
                request.getDescription(),
                request.getNumberParticipants(),
                request.getDuration(),
                ProgramStatus.valueOf(request.getStatus().toUpperCase()),
                department,
                facilitator,
                tags,
                LocalDate.parse(request.getStartDate()),
                request.getMeetingLink(),
                ProgramType.valueOf(request.getType().toUpperCase()),
                users // Created by
        );
    }

    private void saveProgramAndSchedule(Programs program, ProgramSchedule schedule) {
        programRepository.save(program);
        programScheduleRepository.save(schedule);
    }

    private Programs fetchProgram(String programId) {
        return programRepository.findById(programId)
                .orElseThrow(() -> new ResourceNotFoundException("Program not found"));
    }

    private void validateStudentAvailability(String studentId, Programs program) {

        if (isJoined(program.getProgramID(), studentId)) {
            throw new ResourceAlreadyExistsException("Student is already registered for this program");
        }

        List<Appointments> appointments = appointmentRepository.findScheduledOrInProgressAppointmentsByStudentId(
                studentId);

        ProgramSchedule programSchedule = programScheduleRepository.findFirstByProgramIDOrderByScheduleIDDesc(program.getProgramID());

        LocalTime programStart = programSchedule.getStartTime();
        LocalTime programEnd = programSchedule.getEndTime();

        List<Appointments> overlappingAppointments = appointments.stream()
                .filter(appointment -> isOverlapping(
                        appointment.getTimeSlot().getStartTime(),
                        appointment.getTimeSlot().getEndTime(),
                        programStart, programEnd))
                .toList();

        if (!overlappingAppointments.isEmpty()) {
            String jsonResponse = convertToJson(overlappingAppointments);
            throw new OperationFailedException("Student has conflicting appointments: " + jsonResponse);
        }
    }

    private void validateTimeRequest(ProgramsRequest programsRequest)   {
        LocalTime startTime = parseTime(programsRequest.getWeeklyScheduleRequest().getStartTime());
        LocalTime endTime = parseTime(programsRequest.getWeeklyScheduleRequest().getEndTime());

        if (!isModulo30(startTime) || !isModulo30(endTime)) {
            throw new IllegalArgumentException("Start time and end time must be in 30 minute intervals");
        }

        if (startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("Start time must be before end time");
        }
//
//        if (startTime.isBefore(endTime)) {
//            return;
//        }
    }

    private void validateProgramStatus(Programs program, ProgramUpdateRequest updateRequest) {
        ProgramStatus currentStatus = program.getStatus();
        ProgramStatus newStatus = ProgramStatus.valueOf(updateRequest.getStatus().toUpperCase());

        if (currentStatus == ProgramStatus.COMPLETED && newStatus != ProgramStatus.COMPLETED) {
            throw new OperationFailedException("Cannot change status of a completed program");
        }
        if (currentStatus == ProgramStatus.DELETED) {
            throw new OperationFailedException("Cannot update a cancelled program");
        }
    }

    private void validateDateConstraints(Programs program, ProgramUpdateRequest updateRequest) {
        LocalDate today = LocalDate.now();
        LocalDate programStartDate = program.getStartDate();
        LocalDate newStartDate = LocalDate.parse(updateRequest.getStartDate());
        LocalDate newEndDate = newStartDate.plusDays(updateRequest.getDuration());

        if (!programStartDate.isAfter(today) && programStartDate.plusDays(program.getDuration()).isAfter(today)) {
            throw new OperationFailedException("Program is currently in progress and cannot be updated");
        }

        if (!programStartDate.isEqual(newStartDate) && programStartDate.isBefore(today)) {
            throw new OperationFailedException("Cannot modify start date of a program that has already started");
        }

        if (newEndDate.isBefore(today)) {
            throw new OperationFailedException("End date cannot be in the past");
        }
    }

    private void validateParticipantCount(String programId, int newParticipantCount) {
        int enrolledCount = programParticipationRepository.findByProgramID(programId).size();
        if (newParticipantCount < enrolledCount) {
            throw new OperationFailedException("Number of participants cannot be less than enrolled participants (" + enrolledCount + ")");
        }
    }

    private void validateDepartmentAndFacilitator(ProgramUpdateRequest updateRequest) {
        if (!departmentRepository.existsById(updateRequest.getDepartmentId())) {
            throw new OperationFailedException("Department not found");
        }

        Psychologists facilitator = psychologistRepository.findById(updateRequest.getFacilitatorId())
                .orElseThrow(() -> new OperationFailedException("Facilitator not found"));

        if (!facilitator.getDepartmentID().equals(updateRequest.getDepartmentId())) {
            throw new OperationFailedException("Facilitator does not belong to the specified department");
        }
    }

    private void validateMeetingLink(String meetingLink) {
        if (meetingLink != null && !meetingLink.matches("^(http|https)://.*$")) {
            throw new OperationFailedException("Invalid meeting link format");
        }
    }

    private void validateFacilitatorAvailability(String facilitatorId, ProgramsRequest programsRequest) {
        if (!isFacilitatorAvailable(facilitatorId, programsRequest)) {
            throw new OperationFailedException("Facilitator is not available for the program");
        }
    }

    private void validateProgramData(Programs program, ProgramUpdateRequest updateRequest) {
        validateName(updateRequest.getName());
        validateDescription(updateRequest.getDescription());
        validateStartDateAndDuration(updateRequest.getStartDate(),updateRequest.getDuration());

        if (updateRequest.getWeeklyScheduleRequest() != null) {
            validateWeeklySchedule(updateRequest.getWeeklyScheduleRequest());
        }
        validateProgramStatus(program, updateRequest);
        validateDateConstraints(program, updateRequest);
        validateParticipantCount(program.getProgramID(), updateRequest.getNumberParticipants());
        validateDepartmentAndFacilitator(updateRequest);
        validateMeetingLink(updateRequest.getMeetingLink());
        isFacilitatorAvailable(updateRequest.getFacilitatorId(), updateRequest);
    }

    private void validateName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Program name cannot be empty");
        }
    }

    private void validateDescription(String description) {
        if (description == null || description.isEmpty()) {
            throw new IllegalArgumentException("Program description cannot be empty");
        }
    }

    private void validateStartDateAndDuration(String startDate, Integer duration) {
        if (startDate == null || startDate.isEmpty()) {
            throw new IllegalArgumentException("Start date cannot be empty");
        }
        try {
            LocalDate date = LocalDate.parse(startDate);
            if (date.isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("Start date cannot be in the past");
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format");
        }

        if (duration == null || duration <= 0) {
            throw new IllegalArgumentException("Duration must be a positive integer");
        }
        if (duration > 52) {
            throw new IllegalArgumentException("Duration cannot exceed 12 months (52 weeks)");
        }
    }

    @SuppressWarnings("unused")
    private void validateDuration(Integer duration) {
        if (duration == null || duration <= 0) {
            throw new IllegalArgumentException("Duration must be a positive integer");
        }
    }

    private void validateWeeklySchedule(ProgramWeeklyScheduleRequest schedule) {
        if (schedule.getWeeklyAt() == null || schedule.getWeeklyAt().isEmpty()) {
            throw new IllegalArgumentException("Weekly schedule day cannot be empty");
        }
        if (schedule.getStartTime() == null || schedule.getStartTime().isEmpty()) {
            throw new IllegalArgumentException("Start time cannot be empty");
        }
        if (schedule.getEndTime() == null || schedule.getEndTime().isEmpty()) {
            throw new IllegalArgumentException("End time cannot be empty");
        }
    }

    private boolean validateTimeSlotOverlaps(List<LocalDate> scheduleDates, String psychologistId, LocalTime programStartTime, LocalTime programEndTime) {
        List<LocalDate> conflictingDates = new ArrayList<>(List.of());

        for (LocalDate scheduleDate : scheduleDates) {
            List<TimeSlots> timeSlots = timeSlotRepository.findByPsychologistIdAndDate(psychologistId, scheduleDate)
                    .stream()
                    .toList();
            timeSlots.forEach(slot -> {
                if (slot.getStatus().equals(TimeslotStatus.BOOKED)) {
                    conflictingDates.add(scheduleDate);
                    return;
                }
                if (isTimeOverlap(programStartTime, programEndTime,
                        slot.getStartTime(), slot.getEndTime())) {
                    conflictingDates.add(scheduleDate);
                }
            });
        }
        return conflictingDates.isEmpty();
    }

    private void updateAndSaveProgramDetails(Programs program, ProgramUpdateRequest updateRequest, Set<Tags> tags) {
        program.setProgramName(updateRequest.getName());
        program.setDescription(updateRequest.getDescription());
        program.setDuration(updateRequest.getDuration());
        program.setNumberParticipants(updateRequest.getNumberParticipants());
        program.setStatus(ProgramStatus.valueOf(updateRequest.getStatus().toUpperCase()));
        program.setMeetingLink(updateRequest.getMeetingLink());
        program.setType(ProgramType.valueOf(updateRequest.getType().toUpperCase()));
        program.setFacilitatorID(updateRequest.getFacilitatorId());
        program.setDepartmentID(updateRequest.getDepartmentId());
        program.setStartDate(LocalDate.parse(updateRequest.getStartDate()));
        program.setTags(tags);
        programRepository.save(program); // Save the updated program
    }

    private void updateProgramSchedule(ProgramSchedule programSchedule, ProgramUpdateRequest updateRequest) {

        String dayOfWeek = updateRequest.getWeeklyScheduleRequest().getWeeklyAt();
        String startTime = updateRequest.getWeeklyScheduleRequest().getStartTime();
        String endTime = updateRequest.getWeeklyScheduleRequest().getEndTime();
        if (!isValidDayOfWeek(dayOfWeek)) {
            throw new IllegalArgumentException("Invalid day of the week: " + dayOfWeek);
        }

        // Parse times
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH);
        LocalTime start = LocalTime.parse(startTime, formatter);
        LocalTime end = LocalTime.parse(endTime, formatter);

        // Update program schedule
        programSchedule.setDayOfWeek(dayOfWeek);
        programSchedule.setStartTime(start);
        programSchedule.setEndTime(end);
        programScheduleRepository.save(programSchedule);
    }

    private ProgramsResponse getProgramResponse(Programs program, String studentID) {
        if (program == null) throw new ResourceNotFoundException("Program not found");
        ProgramSchedule lastSchedule = programScheduleRepository.findFirstByProgramIDOrderByScheduleIDDesc(program.getProgramID());

        int activeStudentsCount = Math.toIntExact(programParticipationRepository.countActiveStudentsByProgramID(program.getProgramID()));

        String status = null;

        if (studentID != null && !studentID.isBlank()) {
            ProgramParticipation latestParticipation = programParticipationRepository
                    .findFirstByProgramIDAndStudentIDOrderByParticipationIDDesc(program.getProgramID(), studentID);

            if (latestParticipation != null) {
                status = String.valueOf(latestParticipation.getStatus());
            }
        }

        return programMapper.buildProgramResponse(program, activeStudentsCount, lastSchedule, status);
    }

    public ProgramsResponse getProgramParticipants(String programId) {

        Programs program = programRepository.findById(programId).orElse(null);
        if (program == null) throw new ResourceNotFoundException("Program not found");
        List<StudentResponse> studentResponses = getActiveStudentsByProgram(programId)
                .stream()
                .filter(studentResponse -> {
                    ProgramParticipation programParticipation = programParticipationRepository
                            .findFirstByProgramIDAndStudentIDOrderByParticipationIDDesc(programId, studentResponse.getStudentId());
                    return programParticipation != null && programParticipation.getStatus().equals(ParticipationStatus.JOINED);
                })
                .toList();
        return programMapper.buildProgramsParticipantResponse(program, studentResponses);
    }

    private ProgramsResponse getProgramDetailsResponse(Programs program) {
        if (program == null) throw new ResourceNotFoundException("Program not found");
        ProgramSchedule programSchedule = programScheduleRepository.findFirstByProgramIDOrderByScheduleIDDesc(program.getProgramID());
        return programMapper.buildProgramsDetailsResponse(program,
                getActiveStudentsByProgram(program.getProgramID()),
                programSchedule);
    }

    private ProgramSchedule createProgramSchedule(Programs program, String dayOfWeek, String startTime, String endTime) {
        // Validate dayOfWeek
        if (!isValidDayOfWeek(dayOfWeek)) {
            throw new IllegalArgumentException("Invalid day of the week: " + dayOfWeek);
        }

        LocalTime start = parseTime(startTime);
        LocalTime end = parseTime(endTime);

        String scheduleId = __.generateProgramScheduleID();

        return new ProgramSchedule(scheduleId, program, dayOfWeek, start, end);
    }

    private LocalTime parseTime(String time) {
        DateTimeFormatter formatter24 = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.ENGLISH);
        try {
            return LocalTime.parse(time, formatter24);
        } catch (Exception e) {
            DateTimeFormatter formatter12 = DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH);
            return LocalTime.parse(time, formatter12);
        }
    }

    private boolean isValidDayOfWeek(String day) {
        return Arrays.stream(DayOfWeek.values())
                .map(d -> d.name().toLowerCase())
                .anyMatch(d -> d.equals(day.toLowerCase()));
    }

    private boolean isFacilitatorAvailable(String facilitatorId, ProgramsRequest programsRequest) {
        Psychologists facilitator = psychologistRepository.findById(facilitatorId)
                .orElseThrow(() -> new ResourceNotFoundException("Psychologist not found"));

        List<LocalDate> scheduleDates = generateWeeklySchedule(programsRequest);

        LocalTime programStartTime = parseTime(programsRequest
                .getWeeklyScheduleRequest().getStartTime());
        LocalTime programEndTime = parseTime(programsRequest
                .getWeeklyScheduleRequest().getEndTime());

        return validateTimeSlotOverlaps(scheduleDates, facilitator.getPsychologistID(), programStartTime, programEndTime);
    }

    public boolean isFacilitatorAvailable(String facilitatorId, String startDate,
                                          String endDate, String dateOfWeek,
                                          String startTime, String endTime) {
        Psychologists facilitator = psychologistRepository.findById(facilitatorId)
                .orElseThrow(() -> new ResourceNotFoundException("Psychologist not found"));

        List<LocalDate> scheduleDates = generateWeeklySchedule(startDate, dateOfWeek, endDate);

        LocalTime programStartTime = LocalTime.parse(startTime, DateTimeFormatter.ofPattern("h:mm a"));
        LocalTime programEndTime = LocalTime.parse(endTime, DateTimeFormatter.ofPattern("h:mm a"));

        return !validateTimeSlotOverlaps(scheduleDates, facilitator.getPsychologistID(), programStartTime, programEndTime);
    }

    private boolean isTimeOverlap(LocalTime startTime1, LocalTime endTime1, LocalTime startTime2, LocalTime endTime2) {
        return (startTime1.isBefore(endTime2) && endTime1.isAfter(startTime2));
    }

    private List<LocalDate> generateWeeklySchedule(String startDate,
                                                   String endDate, String dateOfWeek) {

        int duration = calculateDurationInWeeks(LocalDate.parse(startDate), LocalDate.parse(endDate));

        LocalDate start = LocalDate.parse(startDate);

        DayOfWeek targetDay = DayOfWeek.valueOf(dateOfWeek.toUpperCase());

        List<LocalDate> scheduleDates = new ArrayList<>();

        LocalDate nextOccurrence = getNextDayOfWeek(start, targetDay);
        scheduleDates.add(nextOccurrence);

        for (int i = 1; i < duration; i++) {
            nextOccurrence = nextOccurrence.plusWeeks(1); // Add one week
            scheduleDates.add(nextOccurrence);
        }
        return scheduleDates;
    }

    private List<LocalDate> generateWeeklySchedule(String startDate,
                                                   int duration, String dateOfWeek) {

        LocalDate start = LocalDate.parse(startDate);

        DayOfWeek targetDay = DayOfWeek.valueOf(dateOfWeek.toUpperCase());

        List<LocalDate> scheduleDates = new ArrayList<>();

        LocalDate nextOccurrence = getNextDayOfWeek(start, targetDay);
        scheduleDates.add(nextOccurrence);

        for (int i = 1; i < duration; i++) {
            nextOccurrence = nextOccurrence.plusWeeks(1); // Add one week
            scheduleDates.add(nextOccurrence);
        }
        return scheduleDates;
    }

    private List<LocalDate> generateWeeklySchedule(ProgramsRequest programsRequest) {

        String startDate = programsRequest.getStartDate();
        String dateOfWeek = programsRequest.getWeeklyScheduleRequest().getWeeklyAt();
        int duration = programsRequest.getDuration();

        LocalDate start = LocalDate.parse(startDate);

        DayOfWeek targetDay = DayOfWeek.valueOf(dateOfWeek.toUpperCase());

        List<LocalDate> scheduleDates = new ArrayList<>();

        LocalDate nextOccurrence = getNextDayOfWeek(start, targetDay);
        scheduleDates.add(nextOccurrence);

        for (int i = 1; i < duration; i++) {
            nextOccurrence = nextOccurrence.plusWeeks(1); // Add one week
            scheduleDates.add(nextOccurrence);
        }
        return scheduleDates;
    }

    private LocalDate getNextDayOfWeek(LocalDate start, DayOfWeek targetDay) {
        if (start.getDayOfWeek() == targetDay) {
            return start;
        }
        int daysToNext = (targetDay.getValue() - start.getDayOfWeek().getValue() + 7) % 7;
        return start.plusDays(daysToNext);
    }

    private boolean isJoined(String programId, String studentId) {
        if (!programParticipationRepository.existsByProgramIDAndStudentID(programId, studentId)) {
            return false;
        }
        ProgramParticipation participation = programParticipationRepository.findFirstByProgramIDAndStudentIDOrderByParticipationIDDesc(
                programId, studentId);
        return participation.getStatus().equals(ParticipationStatus.JOINED);
    }

    @Async
    public void sendProgramReminders() {
        // Get today's date and tomorrow's date
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        // Retrieve programs scheduled for today or tomorrow
        List<Programs> programs = programRepository.findByStartDate(tomorrow);
        programs.addAll(programRepository.findByStartDate(today));

        for (Programs program : programs) {
            List<ProgramParticipation> participations = programParticipationRepository.findByProgramID(program.getProgramID());

            for (ProgramParticipation participation : participations) {
                // Get the program start date for the participant
                LocalDate programStartDate = participation.getStartDate();
                Students student = participation.getStudent();

                // Find the schedule of the program
                ProgramSchedule schedule = programScheduleRepository.findByProgramID(program.getProgramID()).getLast();

                // Notify 1 day before the program
                if (programStartDate.equals(tomorrow)) {
                    emailService.sendProgramParticipationReminder(student.getUser().getEmail(), program, schedule, student.getUser().getFullName(), false);
                }

                // Notify on the day of the program
                if (programStartDate.equals(today)) {
                    emailService.sendProgramParticipationReminder(student.getUser().getEmail(), program, schedule, student.getUser().getFullName(), true);
                }
            }
        }
    }

    public void updateProgramStatuses() {
        LocalDate today = LocalDate.now();
        List<Programs> programs = programRepository.findAll();

        programs.forEach(program -> {
            LocalDate endDate = program.getStartDate().plusDays(program.getDuration());

            // Change PENDING → IN_PROGRESS
            if (program.getStatus() == ProgramStatus.ACTIVE && program.getStartDate().isBefore(today)) {
                program.setStatus(ProgramStatus.IN_PROGRESS);
            }

            // Change IN_PROGRESS → COMPLETED
            if (program.getStatus() == ProgramStatus.IN_PROGRESS && endDate.isBefore(today)) {
                program.setStatus(ProgramStatus.COMPLETED);
                // Update participation status
                List<ProgramParticipation> programParticipationList = programParticipationRepository.findByProgramID(program.getProgramID());
                programParticipationList.forEach(participation -> {
                    if (participation.getStatus().equals(ParticipationStatus.JOINED)) {
                        participation.setStatus(ParticipationStatus.COMPLETED);
                    }
                });
                programParticipationRepository.saveAll(programParticipationList);
            }
        });

        programRepository.saveAll(programs); // Bulk update
        System.out.println("Program statuses updated: " + today);
    }


    private List<String> getDefaultSlotIds(LocalTime startTime, LocalTime endTime) {
        return DEFAULT_SLOT_IDS.entrySet().stream()
                .filter(entry -> {
                    LocalTime slotStart = entry.getValue();
                    LocalTime slotEnd = slotStart.plusMinutes(30);
                    return (slotStart.isBefore(endTime) || slotStart.equals(endTime)) &&
                            (slotEnd.isAfter(startTime) || slotEnd.equals(startTime));
                })
                .map(Map.Entry::getKey)
                .toList();
    }


    @Transactional
    private void createTimeSlotsFromDefaults(
            String psychologistId,
            LocalDate slotDate,
            List<String> defaultSlotIds
    ) {
        Psychologists psychologist = psychologistRepository.findById(psychologistId)
                .orElseThrow(() -> new ResourceNotFoundException("Psychologist not found"));

        List<DefaultTimeSlot> defaultSlots = defaultTimeSlotRepository.findAllById(defaultSlotIds);
        if (defaultSlots.size() != defaultSlotIds.size()) {
            throw new ResourceNotFoundException("Some default slots not found");
        }

        // Fetch all existing slots in one query
        List<TimeSlots> existingSlots = timeSlotRepository.findByPsychologistIdAndDate(psychologist.getPsychologistID(), slotDate);
        Set<String> existingTimeRanges = existingSlots.stream()
                .map(slot -> slot.getStartTime() + "-" + slot.getEndTime())
                .collect(Collectors.toSet());

        List<TimeSlots> newSlots = new ArrayList<>();

        for (DefaultTimeSlot defaultSlot : defaultSlots) {
            String slotKey = defaultSlot.getStartTime() + "-" + defaultSlot.getEndTime();

            if (!existingTimeRanges.contains(slotKey)) {
                TimeSlots slot = new TimeSlots();
                slot.setSlotDate(slotDate);
                slot.setStartTime(defaultSlot.getStartTime());
                slot.setEndTime(defaultSlot.getEndTime());
                slot.setPsychologist(psychologist);
                slot.setMaxCapacity(3); // Default capacity
                slot.setStatus(TimeslotStatus.UNAVAILABLE);
                slot.setTimeSlotsID(generateSlotId(psychologistId, slotDate, defaultSlot.getSlotId()));
                newSlots.add(slot);
            }
        }

        if (!newSlots.isEmpty()) {
            timeSlotRepository.saveAll(newSlots);
        }
    }


    private String generateSlotId(String psychologistId, LocalDate date, String defaultSlotId) {
        return "TS-" + psychologistId + "-" + date.toString() + "-" + defaultSlotId;
    }


    // Deprecated function
    @SuppressWarnings("unused")
    private ProgramsResponse _getProgramResponse(Programs program, String studentID) {
        if (program == null) throw new ResourceNotFoundException("Program not found");

        Students student = studentRepository.findByStudentID(studentID);

        List<ProgramSchedule> programSchedule = programScheduleRepository.findByProgramID(program.getProgramID());
        ProgramSchedule lastSchedule = programSchedule.isEmpty() ? null : programSchedule.getLast();

        Integer activeStudentsCount = getActiveStudentsByProgram(program.getProgramID()).size();

        String status = null;
        if (student != null) {
            ProgramParticipation participation =
                    programParticipationRepository.findFirstByProgramIDAndStudentIDOrderByParticipationIDDesc(program.getProgramID(), student.getStudentID());
            if (participation != null) {
                status = String.valueOf(participation.getStatus());
            }
        }
        return programMapper.buildProgramResponse(program, activeStudentsCount, lastSchedule, status);
    }

    @SuppressWarnings("unused")
    private ProgramsResponse _getProgramDetailsResponse(Programs program) {
        if (program == null) throw new ResourceNotFoundException("Program not found");
        List<ProgramSchedule> programSchedule = programScheduleRepository.findByProgramID(program.getProgramID());
        return programMapper.buildProgramsDetailsResponse(program,
                getActiveStudentsByProgram(program.getProgramID()),
                programSchedule.getLast());
    }

    public ProgramsResponse __updateProgram(String programId, ProgramUpdateRequest updateRequest) {
        Programs program = fetchProgram(programId);
        validateProgramStatus(program, updateRequest);
        validateDateConstraints(program, updateRequest);
        validateParticipantCount(programId, updateRequest.getNumberParticipants());
        validateDepartmentAndFacilitator(updateRequest);
        validateMeetingLink(updateRequest.getMeetingLink());
        isFacilitatorAvailable(updateRequest.getFacilitatorId(), updateRequest);

        Set<Tags> tags = fetchTags(updateRequest.getTags());

        updateAndSaveProgramDetails(program, updateRequest, tags);
        updateProgramSchedule(programScheduleRepository.findByProgramID(programId).getLast(), updateRequest);

        return getProgramResponse(program, null);
    }
    
    @SuppressWarnings("unused")
    private ProgramsResponse _createProgram(ProgramsRequest programsRequest, String userId) {

        String programId = __.generateProgramID();
        Optional<Users> staffUser = userRepository.findById(userId);
        if (staffUser.isEmpty()) {
            throw new ResourceNotFoundException("User with ID " + userId + " not found.");
        }

        HashSet<Tags> tags = programsRequest.getTags()
                .stream()
                .map(String::toUpperCase)
                .map(tag -> tagsRepository.findById(tag)
                        .orElseThrow(() -> new ResourceNotFoundException("Tag not found with ID: " + tag)))
                .collect(Collectors.toCollection(HashSet::new));
        Department department = departmentRepository.findById(programsRequest.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + programsRequest.getDepartmentId()));
        Psychologists facilitator = psychologistRepository.findById(programsRequest.getFacilitatorId())
                .orElseThrow(() -> new ResourceNotFoundException("Psychologist not found with ID: " + programsRequest.getFacilitatorId()));

        Programs program = new Programs(
                programId,
                programsRequest.getName(),
                programsRequest.getDescription(),
                programsRequest.getNumberParticipants(),
                programsRequest.getDuration(),
                ProgramStatus.valueOf(programsRequest.getStatus().toUpperCase()),
                department,
                facilitator,
                tags,
                LocalDate.parse(programsRequest.getStartDate()),
                programsRequest.getMeetingLink(),
                ProgramType.valueOf(programsRequest.getType().toUpperCase()),
                staffUser.get().getUserId());

        ProgramSchedule programSchedule = createProgramSchedule(program,
                programsRequest.getWeeklyScheduleRequest().getWeeklyAt(),
                programsRequest.getWeeklyScheduleRequest().getStartTime(),
                programsRequest.getWeeklyScheduleRequest().getEndTime());

        programRepository.save(program);
        programScheduleRepository.save(programSchedule);
        return getProgramById(programId, null);
    }

    @SuppressWarnings("unused")
    private ProgramsResponse _updateProgram(String programId, ProgramUpdateRequest updateRequest) {

        Programs program = programRepository.findById(programId).orElse(null);
        if (program == null) throw new ResourceNotFoundException("Program not found");

        if (program.getStartDate().isBefore(LocalDate.now()) &&
                program.getStartDate().plusDays(program.getDuration()).isAfter(LocalDate.now())) {
            throw new OperationFailedException("Program is currently in progress and cannot be updated");
        }

        if (!program.getStartDate().isEqual(LocalDate.parse(updateRequest.getStartDate())) &&
                program.getStartDate().isBefore(LocalDate.now())) {
            throw new OperationFailedException("Cannot modify start date of a program that has already started");
        }

        LocalDate newStartDate = LocalDate.parse(updateRequest.getStartDate());
        LocalDate newEndDate = newStartDate.plusDays(updateRequest.getDuration());
        if (newEndDate.isBefore(LocalDate.now())) {
            throw new OperationFailedException("End date cannot be in the past");
        }

        int enrolledCount = programParticipationRepository.findByProgramID(programId).size();
        if (updateRequest.getNumberParticipants() < enrolledCount) {
            throw new OperationFailedException("Number of participants cannot be less than the number of enrolled participants (" + enrolledCount + ")");
        }

        if (!departmentRepository.existsById(updateRequest.getDepartmentId())) {
            throw new OperationFailedException("Department not found");
        }

        if (!psychologistRepository.existsById(updateRequest.getFacilitatorId())) {
            throw new OperationFailedException("Facilitator not found");
        }

        Psychologists facilitator = psychologistRepository.findById(updateRequest.getFacilitatorId()).orElse(null);
        if (facilitator != null && !facilitator.getDepartmentID().equals(updateRequest.getDepartmentId())) {
            throw new OperationFailedException("Facilitator does not belong to the specified department");
        }

        ProgramStatus currentStatus = program.getStatus();
        ProgramStatus newStatus = ProgramStatus.valueOf(updateRequest.getStatus().toUpperCase());
        if (currentStatus == ProgramStatus.COMPLETED && newStatus != ProgramStatus.COMPLETED) {
            throw new OperationFailedException("Cannot change status of a completed program");
        }

        if (currentStatus == ProgramStatus.DELETED) {
            throw new OperationFailedException("Cannot update a cancelled program");
        }

        if (updateRequest.getMeetingLink() != null && !updateRequest.getMeetingLink().matches("^(http|https)://.*$")) {
            throw new OperationFailedException("Invalid meeting link format");
        }

        // Update program details
        program.setProgramName(updateRequest.getName());
        program.setDescription(updateRequest.getDescription());
        program.setDuration(updateRequest.getDuration());
        program.setNumberParticipants(updateRequest.getNumberParticipants());
        program.setStatus(newStatus);
        program.setMeetingLink(updateRequest.getMeetingLink());
        program.setType(ProgramType.valueOf(updateRequest.getType().toUpperCase()));
        program.setFacilitatorID(updateRequest.getFacilitatorId());
        program.setDepartmentID(updateRequest.getDepartmentId());
        program.setStartDate(newStartDate);

        // Handle tags
        Set<Tags> tags = new HashSet<>();
        for (String tagId : updateRequest.getTags()) {
            Tags tag = tagsRepository.findById(tagId).orElse(null);
            if (tag == null) throw new ResourceNotFoundException("Tag not found");
            tags.add(tag);
        }
        program.setTags(tags);
        programRepository.save(program);
        return getProgramResponse(program, null);
    }

    @SuppressWarnings("unused")
    private List<StudentResponse> _getStudentsByProgram(String programId) {
        List<String> studentIDs = programParticipationRepository.findStudentIDsByProgramID(programId);
        if (studentIDs.isEmpty()) {
            return new ArrayList<>();
        }
        return studentIDs.stream()
                .map(studentRepository::findByStudentID)
                .map(studentMapper::buildStudentResponse)
                .peek(studentResponse -> {
                    ProgramParticipation programParticipation = programParticipationRepository
                            .findFirstByProgramIDAndStudentIDOrderByParticipationIDDesc(programId, studentResponse.getStudentId());
                    if (programParticipation != null) {
                        studentResponse.setProgramStatus(programParticipation.getStatus().name());
                    }
                })
                .toList();
    }

    @SuppressWarnings("unused")
    private boolean _checkIfPsychologistIsAvailable(String psychologistId, List<LocalDate> scheduleDates) {

        for (LocalDate scheduleDate : scheduleDates) {
            List<TimeSlots> timeSlots = timeSlotRepository.findByPsychologistIdAndDate(psychologistId, scheduleDate)
                    .stream()
                    .filter(slot -> slot.getStatus().equals(TimeslotStatus.BOOKED))
                    .toList();

            if (!timeSlots.isEmpty()) {
                return false;
            }
        }
        // If no booked time slots are found across all dates, return true (psychologist is available)
        return true;
    }

    @SuppressWarnings("unused")
    private List<StudentResponse> _getActiveStudentsByProgram(String programId) {
        List<String> studentIDs = programParticipationRepository.findActiveStudentIDsByProgramID(programId, ParticipationStatus.CANCELLED);

        return studentIDs.stream()
                .map(studentRepository::findByStudentID)
                .map(studentMapper::buildStudentResponse)
                .peek(studentResponse -> {
                    ProgramParticipation programParticipation = programParticipationRepository
                            .findByProgramIDAndStudentID(programId, studentResponse.getStudentId()).getLast();
                    if (programParticipation != null) {
                        studentResponse.setProgramStatus(programParticipation.getStatus().name());
                    }
                })
                .toList();
    }
}