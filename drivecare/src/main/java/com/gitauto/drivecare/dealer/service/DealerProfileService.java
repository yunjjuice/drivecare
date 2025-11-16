package com.gitauto.drivecare.dealer.service;

import com.gitauto.drivecare.database.car_center.entity.CarCenterEntity;
import com.gitauto.drivecare.database.car_center.repository.CarCenterRepository;
import com.gitauto.drivecare.database.user_info.entity.UserInfoEntity;
import com.gitauto.drivecare.dealer.dto.DealerProfileDto;
import com.gitauto.drivecare.database.user_info.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DealerProfileService {

    private final UserInfoRepository userInfoRepository;
    private final CarCenterRepository carCenterrepository;

    public CarCenterEntity getCarCenterInfoByUserId(String userId) {
        Optional<Long> carCenterId = userInfoRepository.findByUserId(userId)
                .map(UserInfoEntity::getCarCenterId);

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
        carCenter.setEmail(dto.getEmail());
        carCenter.setDesc(dto.getDesc());
        carCenter.setLatitude(dto.getLatitude());
        carCenter.setLongitude(dto.getLongitude());

        return carCenterrepository.save(carCenter);
    }

    public void updateUserCarCenterInfo(long userId, long carCenterId) {
        userInfoRepository.updateCarCenterIdById(userId, carCenterId);
    }
}
