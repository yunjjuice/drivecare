package com.gitauto.drivecare.database.refresh_token.repository;

import com.gitauto.drivecare.database.refresh_token.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
    Optional<RefreshTokenEntity> findByTokenAndRevoked(String token, Character revoked);
}
