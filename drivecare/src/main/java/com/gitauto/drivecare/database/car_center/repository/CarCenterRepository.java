package com.gitauto.drivecare.database.car_center.repository;


import com.gitauto.drivecare.database.car_center.entity.CarCenterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarCenterRepository extends JpaRepository<CarCenterEntity, Long> {
    List<CarCenterEntity> findAllBy();
}
