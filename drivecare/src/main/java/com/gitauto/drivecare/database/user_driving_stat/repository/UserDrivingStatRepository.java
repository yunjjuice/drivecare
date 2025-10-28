package com.gitauto.drivecare.database.user_driving_stat.repository;

import com.gitauto.drivecare.database.user_driving_stat.entity.UserDrivingStatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserDrivingStatRepository extends JpaRepository<UserDrivingStatEntity, Long> {
    Optional<UserDrivingStatEntity> findTopByUserInfo_UserIdOrderByCreDtDesc(String userId);
    List<UserDrivingStatEntity> findAllByUserInfo_UserIdAndCreDtBetween(String userId, LocalDateTime start, LocalDateTime end);
}
