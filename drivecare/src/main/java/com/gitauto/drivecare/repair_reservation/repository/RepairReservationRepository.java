package com.gitauto.drivecare.repair_reservation.repository;

import com.gitauto.drivecare.repair_reservation.entity.RepairReservationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RepairReservationRepository extends JpaRepository<RepairReservationEntity, Long> {
    List<RepairReservationEntity> findByReserveDtAfterOrderByReserveDt(LocalDateTime now);

    Page<RepairReservationEntity> findAllByUser_UserIdOrderByReserveDtDesc(String userId, Pageable pageable);

    List<RepairReservationEntity> findAllByUser_UserIdOrderByReserveDtDesc(String userId);

    List<RepairReservationEntity> findByReserveDtAfterAndUser_UserId(LocalDateTime now, String userId);
}
