package com.gitauto.drivecare.owner.entity;

import com.gitauto.drivecare.carcenter.entity.CarCenterEntity;
import com.gitauto.drivecare.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "REPAIR_RESERVATION")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepairReservationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private UserEntity user;

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

    @Column(name = "APPROVE_YN", columnDefinition = "CHAR(1) DEFAULT 'N'", nullable = false)
    private Character approveYn;

    // 차종 정보 추가

    @Column(name = "DESC")
    private String desc;
}