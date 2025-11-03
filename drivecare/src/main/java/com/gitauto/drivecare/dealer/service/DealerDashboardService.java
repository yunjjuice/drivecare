package com.gitauto.drivecare.dealer.service;

import com.gitauto.drivecare.database.repair_reservation.entity.RepairReservationEntity;
import com.gitauto.drivecare.database.repair_reservation.repository.RepairReservationRepository;
import com.gitauto.drivecare.database.user_info.entity.UserInfoEntity;
import com.gitauto.drivecare.dealer.dto.DealerDashboardDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DealerDashboardService {

    private final RepairReservationRepository repairReservationRepository;

    public DealerDashboardDto getTodayReservationsCount(UserInfoEntity userInfo) {

        DealerDashboardDto dealerDashboardDto = new DealerDashboardDto();

        // 오늘 예약 건수
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);
        long todayReservationCount = repairReservationRepository.countByCarCenterIdAndReserveDtBetween(userInfo.getCarCenterId(), startOfDay, endOfDay);
        dealerDashboardDto.setTodayReservationCount(todayReservationCount);

        // 증감 추이
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDateTime startOfYesterday = yesterday.atStartOfDay();
        LocalDateTime endOfYesterday = yesterday.atTime(LocalTime.MAX);
        long yesterdayReservationCount = repairReservationRepository.countByCarCenterIdAndReserveDtBetween(userInfo.getCarCenterId(), startOfYesterday, endOfYesterday);
        long reservationDifferent = todayReservationCount - yesterdayReservationCount;
        dealerDashboardDto.setReservationDifferent(reservationDifferent);

        // 진행 중 작업
        List<RepairReservationEntity> progressRepairs = repairReservationRepository.findByCarCenterIdAndRepairStatusInAndCreDtBefore(userInfo.getCarCenterId(), List.of('P', 'N'), endOfDay);
        dealerDashboardDto.setProgressRepairs(progressRepairs);

        // 오늘 일정
        List<RepairReservationEntity> todaySchedules = repairReservationRepository.findByCarCenterIdAndReserveDtBetweenAndApproveStatus(userInfo.getCarCenterId(), startOfDay, endOfDay, 'Y');
        dealerDashboardDto.setTodaySchedules(todaySchedules);

        return dealerDashboardDto;
    }

    public Character updateRepairStatus(Long reservationId) {
        RepairReservationEntity entity = repairReservationRepository.findById(reservationId).orElseThrow();
        Character currentStatus = entity.getRepairStatus();
        Character newStatus = currentStatus.equals('N') ? 'P' : 'Y';
        entity.setRepairStatus(newStatus);
        repairReservationRepository.save(entity);
        return newStatus;
    }
}
