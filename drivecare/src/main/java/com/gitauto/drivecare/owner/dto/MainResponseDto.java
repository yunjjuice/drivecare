package com.gitauto.drivecare.owner.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class MainResponseDto {
    private List<ReservationDto> reservationList;
    private CarInfoDto carInfo;
    private CarDetailInfoDto carDetailInfo;
    private DriveScoreDto driveScore;

    @Data
    @AllArgsConstructor
    public static class ReservationDto {
        private Long id;
        private LocalDateTime reserveDt;
        private String carCenterNm;
        private Character approveStatus;
    }

    @Data
    public static class CarInfoDto {
        private String model;
        private String carNumber;
        private Integer odometer;
    }

    @Data
    public static class CarDetailInfoDto {
        private Integer brake;
        private Integer tire;
        private Integer engine;
        private Integer battery;
    }

    @Data
    public static class DriveScoreDto {
        private Integer score;
    }
}
