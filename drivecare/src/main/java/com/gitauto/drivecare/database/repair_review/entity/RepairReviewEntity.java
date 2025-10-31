package com.gitauto.drivecare.database.repair_review.entity;

import com.gitauto.drivecare.database.repair_reservation.entity.RepairReservationEntity;
import com.gitauto.drivecare.database.user_info.entity.UserInfoEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "REPAIR_REVIEW")
@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class RepairReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private UserInfoEntity userInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RESERVATION_ID")
    private RepairReservationEntity repairReservation;

    @Column(name = "RATING")
    private int rating;

    @Column(name = "REVIEW_TEXT")
    private String reviewText;

    @CreationTimestamp
    @Column(name = "CRE_DT")
    private LocalDateTime creDt;

    @UpdateTimestamp
    @Column(name = "UPD_DT")
    private LocalDateTime updDt;
}
