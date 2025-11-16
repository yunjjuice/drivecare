package com.gitauto.drivecare.database.user_info.repository;

import com.gitauto.drivecare.database.user_info.entity.UserInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfoEntity, Long> {
    Optional<UserInfoEntity> findByUserId(String userId);
    boolean existsByUserId(String userId);
    boolean existsByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE UserInfoEntity u SET u.carCenterId = :carCenterId WHERE u.id = :id")
    int updateCarCenterIdById(long id, long carCenterId);
}
