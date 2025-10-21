package com.gitauto.drivecare.dealer.entity;

import com.gitauto.drivecare.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "REPAIR_RESERVATION")
@Getter
@Setter
public class RepairReservationEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CAR_CENTER_ID")
    private Long carCenterId;

    @Column(name = "RESERVE_DT")
    private LocalDateTime reserveDt;

    @Column(name = "CAR_MODEL")
    private String carModel;

    @Column(name = "CAR_NUMBER")
    private String carNumber;

    @Column(name = "[DESC]")
    private String desc;

    @Column(name = "APPROVE_STATUS")
    private Character appoveStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private UserEntity user;
}
