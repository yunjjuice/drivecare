package com.gitauto.drivecare.api.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationResponseDto {
    private Long id;
    private String address;
    private LocalDateTime reserveDt;
    private String carCenterNm;
    private String telNo;
    private Character approveStatus;
    private String desc;
    private String carModel;
    private String carNumber;
    private String repairDesc;
    private boolean reviewed;
    private Character repairStatus;
}
