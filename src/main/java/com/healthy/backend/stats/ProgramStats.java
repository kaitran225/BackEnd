package com.healthy.backend.stats;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProgramStats {
    private Map<String, Double> programParticipationRates; // key: tag name, value: percentage
    // getters & setters
}