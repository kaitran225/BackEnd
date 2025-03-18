package com.healthy.backend.service;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class SurveyServiceHelper {
    public BigDecimal calculateNewWeightedAvg(BigDecimal presentResult, int result, int size) {
        int totalWeightBefore = (size * (size + 1)) / 2;
        int newWeight = size + 1;

        BigDecimal weightCurrentTotal = presentResult.multiply(BigDecimal.valueOf(totalWeightBefore));
        BigDecimal weightNewTotal = BigDecimal.valueOf(result).multiply(BigDecimal.valueOf(newWeight));
        BigDecimal totalWeights = BigDecimal.valueOf(totalWeightBefore).add(BigDecimal.valueOf(newWeight));

        return (weightCurrentTotal.add(weightNewTotal)).divide(totalWeights, 2, RoundingMode.HALF_UP);
    }
}
