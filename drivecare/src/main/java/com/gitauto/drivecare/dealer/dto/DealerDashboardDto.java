package com.gitauto.drivecare.dealer.dto;

import com.gitauto.drivecare.database.repair_reservation.entity.RepairReservationEntity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class DealerDashboardDto {
    private long todayReservationCount;
    private long reservationDifferent;
    private List<RepairReservationEntity> progressRepairs;
    private List<RepairReservationEntity> todaySchedules;
}
