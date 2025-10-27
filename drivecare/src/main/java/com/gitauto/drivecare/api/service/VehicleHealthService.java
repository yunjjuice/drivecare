package com.gitauto.drivecare.api.service;

import com.gitauto.drivecare.api.dto.ReservationResponseDto;
import com.gitauto.drivecare.api.dto.VehicleHealthDetailResponseDto;
import com.gitauto.drivecare.api.dto.VehicleHealthSaveRequestDto;
import com.gitauto.drivecare.database.dtc.entity.DtcEntity;
import com.gitauto.drivecare.database.dtc.repository.DtcRepository;
import com.gitauto.drivecare.database.repair_reservation.entity.RepairReservationEntity;
import com.gitauto.drivecare.database.repair_reservation.repository.RepairReservationRepository;
import com.gitauto.drivecare.database.user_info.entity.UserInfoEntity;
import com.gitauto.drivecare.database.user_info.repository.UserInfoRepository;
import com.gitauto.drivecare.database.vehicle_health.entity.VehicleHealthEntity;
import com.gitauto.drivecare.database.vehicle_health.repository.VehicleHealthRepository;
import com.gitauto.drivecare.database.vehicle_health_dtc.entity.VehicleHealthDtcEntity;
import com.gitauto.drivecare.database.vehicle_health_dtc.repository.VehicleHealthDtcRepository;
import com.gitauto.drivecare.exception.ApiException;
import com.gitauto.drivecare.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class VehicleHealthService {
    private final UserInfoRepository userInfoRepository;
    private final VehicleHealthRepository vehicleHealthRepository;
    private final VehicleHealthDtcRepository vehicleHealthDtcRepository;
    private final DtcRepository dtcRepository;

    public VehicleHealthDetailResponseDto vehicleHealthDetail(String userId) {
        VehicleHealthEntity vehicleHealth = vehicleHealthRepository.findFirstByUserInfo_UserIdOrderByCreDtDesc(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.INVALIE_USERID, ErrorCode.INVALIE_USERID.getDefaultMessage()));

        List<VehicleHealthDtcEntity> vehicleHealthDtcList = vehicleHealthDtcRepository.findAllByVehicleHealth_Id(vehicleHealth.getId());

        List<VehicleHealthDetailResponseDto.DtcDetailDto> dtcDetailDtoList = vehicleHealthDtcList.stream()
                .map(r -> new VehicleHealthDetailResponseDto.DtcDetailDto(
                        r.getDtc().getDtcCd(),
                        r.getDtc().getDtcDesc()
                ))
                .collect(Collectors.toList());

       VehicleHealthDetailResponseDto vehicleHealthDto = new VehicleHealthDetailResponseDto(
               dtcDetailDtoList,
               vehicleHealth.getBatteryCharge(),
               vehicleHealth.getDrivingRange(),
               vehicleHealth.getEngineOilLv(),
               vehicleHealth.getWasherFluidWarnYn(),
               vehicleHealth.getIndicatorWarnYn(),
               vehicleHealth.getAirbagYn()
                );

        return vehicleHealthDto;
    }

    public void saveVehicleHealth(String userId, VehicleHealthSaveRequestDto vehicleHealthSaveRequestDto) {
        UserInfoEntity userInfo = userInfoRepository.findByUserId(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.INVALIE_USERID, ErrorCode.INVALIE_USERID.getDefaultMessage()));

        VehicleHealthEntity vehicleHealth = new VehicleHealthEntity(
                null,
                userInfo,
                LocalDateTime.now(),
                vehicleHealthSaveRequestDto.getBatteryCharge(),
                vehicleHealthSaveRequestDto.getDrivingRange(),
                vehicleHealthSaveRequestDto.getEngineOilLv(),
                vehicleHealthSaveRequestDto.getWasherFluidWarnYn(),
                vehicleHealthSaveRequestDto.getIndicatorWarnYn(),
                vehicleHealthSaveRequestDto.getAirbagYn()
        );

        vehicleHealth = vehicleHealthRepository.save(vehicleHealth);

        Random random = new Random();
        int count = random.nextInt(3);
        long id = 0L;

        for(int i = 0; i < count; i++) {
            id = random.nextLong(dtcRepository.count()) + 1;
            DtcEntity dtcEntity = dtcRepository.findById(id)
                    .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, ErrorCode.NOT_FOUND.getDefaultMessage()));

            vehicleHealthDtcRepository.save(new VehicleHealthDtcEntity(null, vehicleHealth, dtcEntity));
        }
    }
}
