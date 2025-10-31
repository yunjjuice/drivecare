package com.gitauto.drivecare.api.service;

import com.gitauto.drivecare.api.dto.ReservationResponseDto;
import com.gitauto.drivecare.database.repair_reservation.entity.RepairReservationEntity;
import com.gitauto.drivecare.database.repair_reservation.repository.RepairReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {

    private final RepairReservationRepository repairReservationRepository;

    public List<ReservationResponseDto> reservationList(String userId) {
        List<RepairReservationEntity> repairReservation = repairReservationRepository.findAllByUserInfo_UserIdOrderByReserveDtDesc(userId);

        List<ReservationResponseDto> ReservationResponseList = repairReservation.stream()
                .map(r -> new ReservationResponseDto(
                        r.getId(),
                        r.getCarCenter().getAddress(),
                        r.getReserveDt(),
                        r.getCarCenter().getName(),
                        r.getCarCenter().getTelNo(),
                        r.getApproveStatus(),
                        r.getDesc(),
                        r.getCarModel(),
                        r.getCarNumber(),
                        r.getRepairDesc(),
                        r.isReviewed(),
                        r.getRepairStatus()
                ))
                .collect(Collectors.toList());

        return ReservationResponseList;
    }
}
