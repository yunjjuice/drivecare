package com.gitauto.drivecare.api.service;

import com.gitauto.drivecare.api.dto.DrivingResponseDto;
import com.gitauto.drivecare.database.user_driving_stat.entity.UserDrivingStatEntity;
import com.gitauto.drivecare.database.user_driving_stat.repository.UserDrivingStatRepository;
import com.gitauto.drivecare.database.user_info.entity.UserInfoEntity;
import com.gitauto.drivecare.database.user_info.repository.UserInfoRepository;
import com.gitauto.drivecare.exception.ApiException;
import com.gitauto.drivecare.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

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

    public DrivingResponseDto getDrivingData(String userId) {

        // 최근 점수
        UserDrivingStatEntity recentStat = userDrivingStatRepository.findTopByUserInfo_UserIdOrderByCreDtDesc(userId)
                .orElse(null);

        DrivingResponseDto.DrivingScoreDto recentDto = recentStat != null
                ? DrivingResponseDto.DrivingScoreDto.builder()
                .driveScore(recentStat.getDriveScore())
                .accelCount((double) recentStat.getAccelCount())
                .brakeCount((double) recentStat.getBrakeCount())
                .handleMissCount((double) recentStat.getHandleMissCount())
                .build()
                : null;

        // 전월 평균점수
        YearMonth lastMonth = YearMonth.now().minusMonths(1);
        LocalDateTime start = lastMonth.atDay(1).atStartOfDay();
        LocalDateTime end = lastMonth.atEndOfMonth().atTime(23, 59, 59);

        List<UserDrivingStatEntity>  lastMonthStats = userDrivingStatRepository.findAllByUserInfo_UserIdAndCreDtBetween(userId, start, end);

        DrivingResponseDto.DrivingScoreDto lastMonthDto = lastMonthStats != null && !lastMonthStats.isEmpty()
                ? DrivingResponseDto.DrivingScoreDto.builder()
                .driveScore(lastMonthStats.stream().mapToDouble(UserDrivingStatEntity::getDriveScore).average().orElse(0))
                .accelCount(lastMonthStats.stream().mapToDouble(UserDrivingStatEntity::getAccelCount).average().orElse(0))
                .brakeCount(lastMonthStats.stream().mapToDouble(UserDrivingStatEntity::getBrakeCount).average().orElse(0))
                .handleMissCount(lastMonthStats.stream().mapToDouble(UserDrivingStatEntity::getHandleMissCount).average().orElse(0))
                .build()
                : null;

        return DrivingResponseDto.builder()
                .recentScore(recentDto)
                .lastMonthScore(lastMonthDto)
                .build();
    }
}
