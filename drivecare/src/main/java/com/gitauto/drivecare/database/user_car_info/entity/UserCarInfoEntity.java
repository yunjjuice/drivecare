package com.gitauto.drivecare.database.user_car_info.entity;

import com.gitauto.drivecare.database.user_info.entity.UserInfoEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "USER_CAR_INFO")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCarInfoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private UserInfoEntity userInfo;

    @Column(name = "MAKER")
    private String maker;

    @Column(name = "model")
    private String model;

    @Column(name = "YEAR")
    private String year;

    @Column(name = "ENGINE")
    private String engine;

    @Column(name = "CAR_NUMBER")
    private String carNumber;
}
