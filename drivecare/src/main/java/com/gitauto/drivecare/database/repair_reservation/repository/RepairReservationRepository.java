package com.gitauto.drivecare.database.repair_reservation.repository;

import com.gitauto.drivecare.database.repair_reservation.entity.RepairReservationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RepairReservationRepository extends JpaRepository<RepairReservationEntity, Long> {
    List<RepairReservationEntity> findByUserInfo_UserIdAndReserveDtAfterOrderByReserveDt(String userId, LocalDateTime now);

    Page<RepairReservationEntity> findAllByUserInfo_UserIdOrderByReserveDtDesc(String userId, Pageable pageable);

    List<RepairReservationEntity> findAllByUserInfo_UserIdOrderByReserveDtDesc(String userId);

    List<RepairReservationEntity> findByUserInfo_UserIdAndReserveDtAfter(String userId, LocalDateTime now);

    @EntityGraph(attributePaths = {"userInfo"})
    List<RepairReservationEntity> findByCarCenter_Id(Long carCenterId);
}
