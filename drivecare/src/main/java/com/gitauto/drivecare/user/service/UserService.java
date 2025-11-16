package com.gitauto.drivecare.user.service;

import com.gitauto.drivecare.database.user_car_info.entity.UserCarInfoEntity;
import com.gitauto.drivecare.database.user_car_info.repository.UserCarInfoRepository;
import com.gitauto.drivecare.user.dto.LoginRequestDto;
import com.gitauto.drivecare.user.dto.RegisterRequestDto;
import com.gitauto.drivecare.database.user_info.entity.UserInfoEntity;
import com.gitauto.drivecare.database.user_info.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserInfoRepository userInfoRepository;
    private final UserCarInfoRepository userCarInfoRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserInfoEntity login(LoginRequestDto dto) {
        UserInfoEntity user = userInfoRepository.findByUserId(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 아이디입니다."));

        if (!bCryptPasswordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        return user;
    }

    public boolean checkUserIdDuplicate(String userId) {
        return userInfoRepository.existsByUserId(userId);
    }

    public boolean checkEmailDuplicate(String email) {
        return userInfoRepository.existsByEmail(email);
    }

    public void register(RegisterRequestDto dto) {
        // 비밀번호 암호화
        String encodedPassword = bCryptPasswordEncoder.encode(dto.getPassword());

        // 엔터티 생성
        UserInfoEntity user = UserInfoEntity.builder()
                .userId(dto.getUserId())
                .password(encodedPassword)
                .email(dto.getEmail())
                .name(dto.getName())
                .telNo(dto.getTelNo())
                .auth(dto.getAuth())
                .build();

        UserInfoEntity saved = userInfoRepository.save(user);

        if ("user".equalsIgnoreCase(dto.getAuth())) {
            UserCarInfoEntity car = UserCarInfoEntity.builder()
                    .userInfo(saved)
                    .maker(dto.getMaker())
                    .model(dto.getModel())
                    .year(dto.getYear())
                    .carNumber(dto.getCarNumber())
                    .build();
            userCarInfoRepository.save(car);
        }
    }
}
