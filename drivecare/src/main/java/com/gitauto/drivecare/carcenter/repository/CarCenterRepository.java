package com.gitauto.drivecare.carcenter.repository;


import com.gitauto.drivecare.carcenter.entity.CarCenterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarCenterRepository extends JpaRepository<CarCenterEntity, Long> {
}
