package com.gitauto.drivecare.database.user_driving_stat.repository;

import com.gitauto.drivecare.database.user_driving_stat.entity.UserDrivingStatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDrivingStatRepository extends JpaRepository<UserDrivingStatEntity, Long> {
}
