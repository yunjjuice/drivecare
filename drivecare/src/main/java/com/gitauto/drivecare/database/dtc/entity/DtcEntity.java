package com.gitauto.drivecare.database.dtc.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "DTC")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DtcEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "DTC_CD")
    private String dtcCd;

    @Column(name = "DTC_DESC")
    private String dtcDesc;
}
