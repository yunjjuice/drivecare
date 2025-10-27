package com.gitauto.drivecare.database.vehicle_health.repository;

import com.gitauto.drivecare.database.vehicle_health.entity.VehicleHealthEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VehicleHealthRepository extends JpaRepository<VehicleHealthEntity, Long> {
    Optional<VehicleHealthEntity> findFirstByUserInfo_UserIdOrderByCreDtDesc(String userId);
}
