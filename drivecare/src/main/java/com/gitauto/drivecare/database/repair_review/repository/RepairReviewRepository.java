package com.gitauto.drivecare.database.repair_review.repository;

import com.gitauto.drivecare.database.repair_review.entity.RepairReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RepairReviewRepository extends JpaRepository<RepairReviewEntity, Long> {
    Optional<RepairReviewEntity> findByUserInfo_IdAndRepairReservation_Id(Long userInfoId, Long userInfoId1);

    @Query("""
               SELECT COALESCE(AVG(r.rating), 0)
               FROM RepairReviewEntity r
               JOIN r.repairReservation c
               JOIN c.carCenter rv
               WHERE rv.id = :carCenterId
           """)
    Double findAvgRatingByCarCenterId(Long carCenterId);
}
