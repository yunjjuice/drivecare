package com.gitauto.drivecare.api.service;

import com.gitauto.drivecare.database.user_driving_stat.entity.UserDrivingStatEntity;
import com.gitauto.drivecare.database.user_driving_stat.repository.UserDrivingStatRepository;
import com.gitauto.drivecare.database.user_info.entity.UserInfoEntity;
import com.gitauto.drivecare.database.user_info.repository.UserInfoRepository;
import com.gitauto.drivecare.exception.ApiException;
import com.gitauto.drivecare.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DrivingDataService {

    private final UserInfoRepository userInfoRepository;
    private final UserDrivingStatRepository userDrivingStatRepository;

    public String saveDrivingData(String userId, UserDrivingStatEntity drivingData) {
        try {
            UserInfoEntity user = userInfoRepository.findByUserId(userId)
                            .orElseThrow(() -> new ApiException(ErrorCode.INVALIE_USERID, "User not found with userId: " + userId));

            drivingData.setUserInfo(user);

            userDrivingStatRepository.save(drivingData);
            return "데이터 저장 완료";
        } catch (Exception e) {
            return "데이터 저장 실패: " + e.getMessage();
        }
    }
}
