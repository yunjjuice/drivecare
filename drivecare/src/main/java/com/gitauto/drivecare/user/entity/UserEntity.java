package com.gitauto.drivecare.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "USER_INFO")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USER_ID", unique = true, nullable = false)
    private String userId;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "NAME")
    private String name;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "TEL_NO")
    private String telNo;

    @Column(name = "AUTH")
    private String auth;

    @CreationTimestamp
    @Column(name = "CRE_DT", updatable = false)
    private LocalDateTime creDt;

    @UpdateTimestamp
    @Column(name = "UPD_DT")
    private LocalDateTime uptDt;

    @Column(name = "DEL_DT")
    private LocalDateTime delDt;

    @Column(name = "USER_STATUS")
    private Integer userStatus;

    @Column(name = "PW_ALTR_DT")
    private LocalDateTime pwAltrDt;

    @Column(name = "PW_ERR_CNT")
    private Integer pwErrCnt;

    @Column(name = "DEVICE_ID")
    private String deviceId;
}
