package com.gitauto.drivecare.database.repair_reservation.entity;

import com.gitauto.drivecare.database.car_center.entity.CarCenterEntity;
import com.gitauto.drivecare.database.user_info.entity.UserInfoEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "REPAIR_RESERVATION")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepairReservationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private UserInfoEntity userInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CAR_CENTER_ID", nullable = false)
    private CarCenterEntity carCenter;

    @CreationTimestamp
    @Column(name = "CRE_DT", updatable = false)
    private LocalDateTime creDt;

    @UpdateTimestamp
    @Column(name = "UPD_DT")
    private LocalDateTime uptDt;

    @Column(name = "RESERVE_DT")
    private LocalDateTime reserveDt;

    @Column(name = "APPROVE_DT")
    private LocalDateTime approveDt;

    @Column(name = "APPROVE_STATUS", columnDefinition = "CHAR(1) DEFAULT 'P'", nullable = false)
    private Character approveStatus;

    @Column(name = "`DESC`")
    private String desc;

    @Column(name = "CAR_MODEL")
    private String carModel;

    @Column(name = "CAR_NUMBER")
    private String carNumber;

    @Column(name = "REPAIR_DESC")
    private String repairDesc;
}