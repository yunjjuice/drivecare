package com.gitauto.drivecare.owner.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationResponseDto {
    private LocalDateTime reserveDt;
    private String carCenterNm;
    private Character status;
}
