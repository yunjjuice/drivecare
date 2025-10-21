package com.gitauto.drivecare.owner.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CarCenterResponseDto {
    private Long id;

    private String name;

    private String address;

    private String telNo;

    private String desc;

    private Double latitude;

    private Double longitude;

}
