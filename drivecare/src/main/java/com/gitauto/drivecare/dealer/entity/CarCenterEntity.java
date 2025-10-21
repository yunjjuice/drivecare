package com.gitauto.drivecare.dealer.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name = "CAR_CENTER")
public class CarCenterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "[ADDRESS]")
    private String address;

    @Column(name = "TEL_NO")
    private String telNo;

    @Column(name = "[DESC]")
    private String desc;

    @Column(name = "LATITUDE")
    private BigDecimal latitude;

    @Column(name = "LONGITUDE")
    private BigDecimal longitude;

    @Column(name = "EMAIL")
    private String email;
}
