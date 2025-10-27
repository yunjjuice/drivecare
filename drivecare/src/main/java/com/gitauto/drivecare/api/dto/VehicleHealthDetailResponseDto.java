package com.gitauto.drivecare.api.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VehicleHealthDetailResponseDto {
    List<DtcDetailDto> dtcList;
    private Integer batteryCharge;
    private Double drivingRange;
    private String engineOilLv;
    private Character washerFluidWarnYn;
    private Character indicatorWarnYn;
    private Character airbagYn;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class DtcDetailDto
    {
        private String dtcCd;
        private String dtcDesc;
    }
}

