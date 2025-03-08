package com.healthy.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "psychologist_kpi")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PsychologistKPI {
    @Id
    private String id;

    @Column(name = "psychologist_id")
    private String psychologistId;

    private int month;
    private int year;

    @Column(name = "target_slots")
    private int targetSlots;

    @Column(name = "achieved_slots")
    private int achievedSlots;

}