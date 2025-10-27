package com.gitauto.drivecare.database.vehicle_health.entity;

import com.gitauto.drivecare.database.user_info.entity.UserInfoEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "VEHICLE_HEALTH")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleHealthEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private UserInfoEntity userInfo;

    @Column(name = "CRE_DT")
    private LocalDateTime creDt;

    @Column(name = "BATTERY_CHARGE")
    private Integer batteryCharge;

    @Column(name = "DRIVING_RANGE")
    private Double drivingRange;

    @Column(name = "ENGINE_OIL_LV")
    private String engineOilLv;

    @Column(name = "WASHER_FLUID_WARN_YN")
    private Character washerFluidWarnYn;

    @Column(name = "INDICATOR_WARN_YN")
    private Character indicatorWarnYn;

    @Column(name = "AIRBAG_YN")
    private Character airbagYn;
}
