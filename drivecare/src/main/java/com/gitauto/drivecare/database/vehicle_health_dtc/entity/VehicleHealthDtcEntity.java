package com.gitauto.drivecare.database.vehicle_health_dtc.entity;

import com.gitauto.drivecare.database.dtc.entity.DtcEntity;
import com.gitauto.drivecare.database.user_info.entity.UserInfoEntity;
import com.gitauto.drivecare.database.vehicle_health.entity.VehicleHealthEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "VEHICLE_HEALTH_DTC")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleHealthDtcEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VEHICLE_HEALTH_ID", nullable = false)
    private VehicleHealthEntity vehicleHealth;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DTC_ID", nullable = false)
    private DtcEntity dtc;
}
