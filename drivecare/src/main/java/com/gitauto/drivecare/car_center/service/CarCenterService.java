package com.gitauto.drivecare.car_center.service;

import com.gitauto.drivecare.car_center.dto.CarCenterResponseDto;
import com.gitauto.drivecare.car_center.entity.CarCenterEntity;
import com.gitauto.drivecare.car_center.repository.CarCenterRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CarCenterService {

    private final CarCenterRepository carCenterRepository;

    public List<CarCenterResponseDto> getCarCenterList(){
        List<CarCenterEntity> carCenterEntityList = carCenterRepository.findAllBy();

        List<CarCenterResponseDto> carCenterResponseDtoList = carCenterEntityList.stream()
                .map(r -> new CarCenterResponseDto(
                        r.getId(),
                        r.getName(),
                        r.getAddress(),
                        r.getTelNo(),
                        r.getDesc(),
                        r.getLatitude(),
                        r.getLongitude()
                ))
                .toList();

        return carCenterResponseDtoList;
    }
}
