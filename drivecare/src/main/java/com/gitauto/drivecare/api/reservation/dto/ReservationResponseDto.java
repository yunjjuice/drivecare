package com.gitauto.drivecare.api.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class ReservationResponseDto {
    private Long id;
    private String address;
    private LocalDateTime reserveDt;
    private String carCenterNm;
    private String telNo;
    private Character approveStatus;
}
