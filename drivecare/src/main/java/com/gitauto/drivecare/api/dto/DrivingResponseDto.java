package com.gitauto.drivecare.api.dto;

import lombok.*;

@Data
@Builder
public class DrivingResponseDto {

    private DrivingScoreDto recentScore;
    private DrivingScoreDto lastMonthScore;

    @Data
    @Builder
    public static class DrivingScoreDto {
        private Double driveScore;
        private Double accelCount;
        private Double brakeCount;
        private Double handleMissCount;

    }
}
