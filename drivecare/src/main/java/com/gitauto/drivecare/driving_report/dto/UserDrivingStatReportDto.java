package com.gitauto.drivecare.driving_report.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDrivingStatReportDto {
    private String userName;
    private Double driveScoreAvg;

    private Integer accelCountSum;
    private Double accelCountAvg;
    private Integer brakeCountSum;
    private Double brakeCountAvg;
    private Integer handleMissCountSum;
    private Double handleMissCountAvg;

    private String period;
    private String userEmail;
    private int days; // 기간 내 일수 (평균 계산용)
    private String dateRange;
}