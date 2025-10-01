package com.gitauto.drivecare.owner.repository;

import com.gitauto.drivecare.owner.dto.ReservationResponseDto;
import com.gitauto.drivecare.owner.entity.RepairReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RepairReservationRepository extends JpaRepository<RepairReservationEntity, Long> {
}
