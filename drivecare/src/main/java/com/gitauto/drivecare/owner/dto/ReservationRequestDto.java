package com.gitauto.drivecare.owner.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationRequestDto {
    private Long CarCenterId;
    private LocalDateTime reserveDt;
    private String car;
    private String desc;
}
