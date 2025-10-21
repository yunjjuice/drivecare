package com.gitauto.drivecare.dealer.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReservationDto {
    private Long id;
    private LocalDateTime reserveDt;
    private String name;
    private String carModel;
    private String carNumber;
    private String desc;
    private Character approveStatus;
    private String approveStatusName;
    private String telNo;

    public ReservationDto(Long id, LocalDateTime reserveDt, String name, String carModel, String carNumber, String desc, Character approveStatus, String telNo) {
        this.id = id;
        this.reserveDt = reserveDt;
        this.name = name;
        this.carModel = carModel;
        this.carNumber = carNumber;
        this.desc = desc;
        this.approveStatus = approveStatus;
        this.approveStatusName = convertStatus(approveStatus);
        this.telNo = telNo;
    }

    private String convertStatus(Character code) {
        return switch (code) {
            case 'Y' -> "확정";
            case 'N' -> "거절";
            case 'P' -> "대기";
            default -> "-";
        };
    }
}
