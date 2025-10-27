package com.gitauto.drivecare.database.user_driving_stat.entity;

import com.gitauto.drivecare.database.user_info.entity.UserInfoEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "USER_DRIVING_STAT")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDrivingStatEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private UserInfoEntity userInfo;

    @Column(name = "DRIVE_SCORE")
    private Long driveScore;

    @Column(name = "ACCEL_COUNT")
    private Integer accelCount;

    @Column(name = "BRAKE_COUNT")
    private Integer brakeCount;

    @Column(name = "HANDLE_MISS_COUNT")
    private Integer handleMissCount;

    @Column(name = "CRE_DT", nullable = false, updatable = false)
    private LocalDateTime creDt;

    @PrePersist
    protected void onCreate() {
        this.creDt = LocalDateTime.now();
    }
}