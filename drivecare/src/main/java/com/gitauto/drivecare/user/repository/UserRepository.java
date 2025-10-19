package com.gitauto.drivecare.user.repository;

import com.gitauto.drivecare.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUserId(String userId);
    boolean existsByUserId(String userId);

    @Query("SELECT u.carCenterId FROM UserEntity u WHERE u.userId = :userId")
    Optional<Long> findCarCenterIdByUserId(String userId);

    @Transactional
    @Modifying
    @Query("UPDATE UserEntity u SET u.carCenterId = :carCenterId WHERE u.id = :id")
    int updateCarCenterIdById(long id, long carCenterId);
}
