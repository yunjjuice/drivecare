package com.gitauto.drivecare.database.repair_review.repository;

import com.gitauto.drivecare.database.repair_review.entity.RepairReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RepairReviewRepository extends JpaRepository<RepairReviewEntity, Long> {
    Optional<RepairReviewEntity> findByUserInfo_IdAndRepairReservation_Id(Long userInfoId, Long userInfoId1);
}
