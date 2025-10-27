package com.gitauto.drivecare.database.vehicle_health_dtc.repository;

import com.gitauto.drivecare.database.vehicle_health_dtc.entity.VehicleHealthDtcEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleHealthDtcRepository extends JpaRepository<VehicleHealthDtcEntity, Long> {
    List<VehicleHealthDtcEntity> findAllByVehicleHealth_Id(Long vehicleHealthId);
}
