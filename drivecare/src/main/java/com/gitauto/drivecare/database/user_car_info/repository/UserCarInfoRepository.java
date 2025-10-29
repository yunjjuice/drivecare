package com.gitauto.drivecare.database.user_car_info.repository;

import com.gitauto.drivecare.database.user_car_info.entity.UserCarInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserCarInfoRepository extends JpaRepository<UserCarInfoEntity, Long> {
    Optional<UserCarInfoEntity> findAllByUserInfo_UserId(String userId);
}
