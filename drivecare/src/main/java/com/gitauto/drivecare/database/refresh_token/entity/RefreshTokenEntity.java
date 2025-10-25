package com.gitauto.drivecare.database.refresh_token.entity;

import com.gitauto.drivecare.database.user_info.entity.UserInfoEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "REFRESH_TOKEN")
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 256)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private UserInfoEntity user;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private Character revoked;
}
