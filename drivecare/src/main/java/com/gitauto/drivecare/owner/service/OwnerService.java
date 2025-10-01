package com.gitauto.drivecare.owner.service;

import com.gitauto.drivecare.carcenter.entity.CarCenterEntity;
import com.gitauto.drivecare.carcenter.repository.CarCenterRepository;
import com.gitauto.drivecare.owner.dto.ReservationDetailResponseDto;
import com.gitauto.drivecare.owner.dto.ReservationRequestDto;
import com.gitauto.drivecare.owner.dto.ReservationResponseDto;
import com.gitauto.drivecare.owner.entity.RepairReservationEntity;
import com.gitauto.drivecare.owner.repository.RepairReservationRepository;
import com.gitauto.drivecare.user.entity.UserEntity;
import com.gitauto.drivecare.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OwnerService {

    private final RepairReservationRepository repairReservationRepository;
    private final CarCenterRepository carCenterRepository;
    private final UserRepository userRepository;

    public String repairMain() {
        // 대시보드 - 자동차 정보..
        // 운전데이터 (운전점수)
        // 예약 현황
        return "";

    }

    public void reservation(ReservationRequestDto reservationDto) {
        CarCenterEntity carCenter = carCenterRepository.findById(reservationDto.getCarCenterId()).orElseThrow(() -> new RuntimeException("존재하지 않는 카센터입니다."));
        UserEntity user = userRepository.findById(3L).orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다.")); //임시 삽입

        RepairReservationEntity repairReservation = new RepairReservationEntity(
                null,
                user,
                carCenter,
                LocalDateTime.now(),
                LocalDateTime.now(),
                reservationDto.getReserveDt(),
                null,
                'N',
                reservationDto.getDesc()
        );

        repairReservationRepository.save(repairReservation);
    }

    public List<ReservationResponseDto> reservationHistory() {
        return List.of();
    }

    public ReservationDetailResponseDto reservationDetail() {
        return new ReservationDetailResponseDto();
    }
}
