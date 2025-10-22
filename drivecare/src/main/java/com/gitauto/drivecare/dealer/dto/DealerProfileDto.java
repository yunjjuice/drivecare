package com.gitauto.drivecare.dealer.dto;

import lombok.Data;

@Data
public class DealerProfileDto {
    private String address;
    private String name;
    private String telNo;
    private String desc;
    private Double latitude;
    private Double longitude;
    private String email;
}
