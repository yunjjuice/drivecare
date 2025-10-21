package com.gitauto.drivecare.dealer.repository;

import com.gitauto.drivecare.dealer.entity.CarCenterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarCenterRepository extends JpaRepository<CarCenterEntity, Long> {
    Optional<CarCenterEntity> findById(Long id);
    Optional<CarCenterEntity> findByAddress(String address);
}
