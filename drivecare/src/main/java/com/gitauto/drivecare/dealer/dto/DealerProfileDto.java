package com.gitauto.drivecare.dealer.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DealerProfileDto {
    private String address;
    private String name;
    private String telNo;
    private String desc;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String email;
}
