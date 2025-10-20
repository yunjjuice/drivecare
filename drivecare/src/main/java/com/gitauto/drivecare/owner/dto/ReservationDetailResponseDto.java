package com.gitauto.drivecare.owner.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ReservationDetailResponseDto {
    private long id;
    private LocalDateTime reserveDt;
    private Character approveStatus;
    private String desc;
    private String repairDesc;

    private String carCenterNm;
    private String carCenterNum;
    private String carCenterAddress;

    private String carNm;
    private String carNumber;
}
