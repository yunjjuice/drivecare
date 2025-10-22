package com.gitauto.drivecare.dealer.service;

import com.gitauto.drivecare.database.repair_reservation.entity.RepairReservationEntity;
import com.gitauto.drivecare.database.repair_reservation.repository.RepairReservationRepository;
import com.gitauto.drivecare.dealer.dto.ReservationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DealerBookingService {
    private final RepairReservationRepository repairReservationRepository;

    public List<ReservationDto> getBookings(Long carCenterId) {
        List<RepairReservationEntity> reservations = repairReservationRepository.findByCarCenter_Id(carCenterId);

        return reservations.stream()
                .map(r -> new ReservationDto(
                        r.getId(),
                        r.getReserveDt(),
                        r.getUserInfo().getName(),
                        r.getCarModel(),
                        r.getCarNumber(),
                        r.getDesc(),
                        r.getApproveStatus(),
                        r.getUserInfo().getTelNo()
                ))
                .toList();
    }

    public ReservationDto updateAppoveStatus(Long reservationId, Character status) {
        RepairReservationEntity reservation = repairReservationRepository.findById(reservationId).orElse(null);

        reservation.setApproveStatus(status);
        repairReservationRepository.save(reservation);

        return new ReservationDto(
                reservation.getId(),
                reservation.getReserveDt(),
                reservation.getUserInfo().getName(),
                reservation.getCarModel(),
                reservation.getCarNumber(),
                reservation.getDesc(),
                reservation.getApproveStatus(),
                reservation.getUserInfo().getTelNo()
        );
    }
}
