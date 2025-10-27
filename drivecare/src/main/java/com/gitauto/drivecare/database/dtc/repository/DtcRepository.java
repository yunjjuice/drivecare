package com.gitauto.drivecare.database.dtc.repository;

import com.gitauto.drivecare.database.dtc.entity.DtcEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DtcRepository extends JpaRepository<DtcEntity, Long> {

}
