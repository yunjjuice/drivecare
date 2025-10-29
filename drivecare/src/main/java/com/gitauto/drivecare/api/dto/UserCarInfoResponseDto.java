package com.gitauto.drivecare.api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserCarInfoResponseDto {
    private String maker;
    private String model;
    private String year;
    private String engine;
    private String carNumber;
}
