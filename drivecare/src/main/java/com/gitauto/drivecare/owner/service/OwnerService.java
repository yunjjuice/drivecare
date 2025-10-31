package com.gitauto.drivecare.owner.service;

import com.gitauto.drivecare.database.car_center.entity.CarCenterEntity;
import com.gitauto.drivecare.database.car_center.repository.CarCenterRepository;
import com.gitauto.drivecare.owner.dto.*;
import com.gitauto.drivecare.owner.dto.ReservationDetailRequestDto;
import com.gitauto.drivecare.owner.dto.ReservationDetailResponseDto;
import com.gitauto.drivecare.owner.dto.ReservationRequestDto;
import com.gitauto.drivecare.owner.dto.ReservationResponseDto;
import com.gitauto.drivecare.database.repair_reservation.entity.RepairReservationEntity;
import com.gitauto.drivecare.database.repair_reservation.repository.RepairReservationRepository;
import com.gitauto.drivecare.database.user_info.entity.UserInfoEntity;
import com.gitauto.drivecare.database.user_info.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OwnerService {

    private final RepairReservationRepository repairReservationRepository;
    private final CarCenterRepository carCenterRepository;
    private final UserInfoRepository userInfoRepository;

    public MainResponseDto repairMain(String userId) {
        // 자동차 정보
        // 운전데이터 (운전점수)
        userInfoRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다."));

        List<RepairReservationEntity> reservationList = repairReservationRepository.findByUserInfo_UserIdAndReserveDtAfterOrderByReserveDt(userId, LocalDateTime.now());

        MainResponseDto mainResponseDto = MainResponseDto.builder()
                .reservationList(reservationList.stream()
                        .map(r -> new MainResponseDto.ReservationDto(r.getId(), r.getReserveDt(), r.getCarCenter().getName(), r.getApproveStatus()))
                        .collect(Collectors.toList()))
                .build();

        return mainResponseDto;
    }

    public void reservation(String userId, ReservationRequestDto reservationDto) {
        CarCenterEntity carCenter = carCenterRepository.findById(reservationDto.getCarCenterId()).orElseThrow(() -> new RuntimeException("존재하지 않는 카센터입니다."));
        UserInfoEntity user = userInfoRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다."));

        RepairReservationEntity repairReservation = new RepairReservationEntity(
                null,
                user,
                carCenter,
                LocalDateTime.now(),
                LocalDateTime.now(),
                reservationDto.getReserveDt(),
                null,
                'P',
                reservationDto.getDesc(),
                reservationDto.getCarModel(),
                reservationDto.getCarNumber(),
                null,
                false,
                'N'
        );

        repairReservationRepository.save(repairReservation);
    }

    public List<ReservationResponseDto> getUpcomingReservationList(String userId) {
        userInfoRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다."));
        List<RepairReservationEntity> reservationList = repairReservationRepository.findByUserInfo_UserIdAndReserveDtAfter(userId, LocalDateTime.now(ZoneOffset.UTC));

        List<ReservationResponseDto> reservationResponseList = reservationList.stream()
                .map(r -> new ReservationResponseDto(
                        r.getId(),
                        r.getCarCenter().getAddress(),
                        r.getReserveDt(),
                        r.getCarCenter().getName(),
                        r.getCarCenter().getTelNo(),
                        r.getApproveStatus()
                ))
                .toList();

        return reservationResponseList;
    }


    public Page<ReservationResponseDto> reservationHistory(String userId, Pageable pageable) {
        UserInfoEntity user = userInfoRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다."));

        Page<RepairReservationEntity> repairReservation = repairReservationRepository.findAllByUserInfo_UserIdOrderByReserveDtDesc(user.getUserId(), pageable);

        List<ReservationResponseDto> dtos = repairReservation.getContent().stream()
                .map(r -> new ReservationResponseDto(
                        r.getId(),
                        r.getCarCenter().getAddress(),
                        r.getReserveDt(),
                        r.getCarCenter().getName(),
                        r.getCarCenter().getTelNo(),
                        r.getApproveStatus()
                ))
                .collect(Collectors.toList());

        return new PageImpl<>(dtos, pageable, repairReservation.getTotalElements());
    }

    public ReservationDetailResponseDto reservationDetail(String userId, ReservationDetailRequestDto reservationDto) {
        UserInfoEntity user = userInfoRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다."));
        RepairReservationEntity repairReservation = repairReservationRepository.findById(reservationDto.getId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 게시글입니다."));

        if(!Objects.equals(user.getId(), repairReservation.getUserInfo().getId())) {
            throw new RuntimeException("해당 게시글에 대한 권한이 없습니다");
        }

        ReservationDetailResponseDto detailResponseDto = ReservationDetailResponseDto.builder()
                .id(repairReservation.getId())
                .reserveDt(repairReservation.getReserveDt())
                .approveStatus(repairReservation.getApproveStatus())
                .desc(repairReservation.getDesc())
                .repairDesc(repairReservation.getRepairDesc())
                .carCenterNm(repairReservation.getCarCenter().getName())
                .carCenterNum(repairReservation.getCarCenter().getTelNo())
                .carCenterAddress(repairReservation.getCarCenter().getAddress())
                .carNm(repairReservation.getCarModel())
                .carNumber(repairReservation.getCarNumber())
                .build();

        return detailResponseDto;
    }

    public List<CarCenterResponseDto> getCarCenterList() {
        List<CarCenterEntity> carCenterEntityList = carCenterRepository.findAllBy();

        List<CarCenterResponseDto> carCenterResponseDtoList = carCenterEntityList.stream()
                .map(r -> new CarCenterResponseDto(
                        r.getId(),
                        r.getName(),
                        r.getAddress(),
                        r.getTelNo(),
                        r.getDesc(),
                        r.getLatitude(),
                        r.getLongitude()
                ))
                .toList();

        return carCenterResponseDtoList;
    }
}
