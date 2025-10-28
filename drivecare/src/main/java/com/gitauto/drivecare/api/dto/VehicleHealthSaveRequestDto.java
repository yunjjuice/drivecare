package com.gitauto.drivecare.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VehicleHealthSaveRequestDto {
    private Integer batteryCharge;
    private Double drivingRange;
    private String engineOilLv;
    private Character washerFluidWarnYn;
    private Character indicatorWarnYn;
    private Character airbagYn;
}
