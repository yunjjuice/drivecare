package com.gitauto.drivecare.dealer.repository;

import com.gitauto.drivecare.dealer.dto.ReservationDto;
import com.gitauto.drivecare.dealer.entity.RepairReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepairReservationRepository extends JpaRepository<RepairReservationEntity, Long> {
    @Query("""
        SELECT new com.gitauto.drivecare.dealer.dto.ReservationDto(r.id, r.reserveDt, u.name, r.carModel, r.carNumber, r.desc, r.appoveStatus, u.telNo) 
        FROM RepairReservationEntity r LEFT JOIN r.user u 
        WHERE r.carCenterId = :carCenterId
    """)
    List<ReservationDto> findReservationsByCarCenterId(Long carCenterId);
}
