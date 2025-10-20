package com.gitauto.drivecare.owner.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationRequestDto {
    private Long CarCenterId;
    private String reserveDate;
    private String reserveTime;
    private String carModel;
    private String carNumber;
    private String desc;

    public LocalDateTime getReserveDt() {
        if (reserveDate != null && !reserveDate.isEmpty() && reserveTime != null && !reserveTime.isEmpty()) {
            return LocalDateTime.parse(reserveDate + "T" + reserveTime);
        }
        return null;
    }
}
