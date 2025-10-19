package com.gitauto.drivecare.dealer.service;

import com.gitauto.drivecare.dealer.dto.DealerProfileDto;
import com.gitauto.drivecare.dealer.entity.CarCenterEntity;
import com.gitauto.drivecare.dealer.repository.CarCenterRepository;
import com.gitauto.drivecare.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DealerProfileService {

    private final UserRepository userRepository;
    private final CarCenterRepository carCenterrepository;

    public CarCenterEntity getCarCenterInfoByUserId(String userId) {
        Optional<Long> carCenterId =  userRepository.findCarCenterIdByUserId(userId);

        if (carCenterId.isPresent()) {
            return carCenterrepository.findById(carCenterId.get()).orElse(new CarCenterEntity());
        } else {
            return new CarCenterEntity();
        }
    }

    public CarCenterEntity updateCarCenterInfo(DealerProfileDto dto) {
        CarCenterEntity carCenter = carCenterrepository
                .findByAddress(dto.getAddress())
                .orElse(new CarCenterEntity());

        carCenter.setAddress(dto.getAddress());
        carCenter.setName(dto.getName());
        carCenter.setTelNo(dto.getTelNo());
        carCenter.setDesc(dto.getDesc());
        carCenter.setLatitude(dto.getLatitude());
        carCenter.setLongitude(dto.getLongitude());

        return carCenterrepository.save(carCenter);
    }

    public void updateUserCarCenterInfo(long userId, long carCenterId) {
        userRepository.updateCarCenterIdById(userId, carCenterId);
    }
}
