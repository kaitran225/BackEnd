package com.healthy.backend.dto.event;

import com.healthy.backend.entity.Programs;
import com.healthy.backend.entity.Students;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProgramWithStudents {
    private Students students;
    private Programs programs;
}
