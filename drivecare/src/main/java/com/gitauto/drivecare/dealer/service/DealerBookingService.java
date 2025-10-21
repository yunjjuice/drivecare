package com.gitauto.drivecare.dealer.service;

import com.gitauto.drivecare.dealer.dto.ReservationDto;
import com.gitauto.drivecare.dealer.entity.RepairReservationEntity;
import com.gitauto.drivecare.dealer.repository.RepairReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DealerBookingService {
    private final RepairReservationRepository repairReservationRepository;

    public List<ReservationDto> getBookings(Long carCenterId) {
        return repairReservationRepository.findReservationsByCarCenterId(carCenterId);
    }

    public ReservationDto updateAppoveStatus(Long reservationId, Character status) {
        RepairReservationEntity reservation = repairReservationRepository.findById(reservationId).orElse(null);

        reservation.setAppoveStatus(status);
        repairReservationRepository.save(reservation);

        return new ReservationDto(
                reservation.getId(),
                reservation.getReserveDt(),
                reservation.getUser().getName(),
                reservation.getCarModel(),
                reservation.getCarNumber(),
                reservation.getDesc(),
                reservation.getAppoveStatus(),
                reservation.getUser().getTelNo()
        );
    }
}
