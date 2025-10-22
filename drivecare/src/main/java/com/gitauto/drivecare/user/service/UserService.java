package com.gitauto.drivecare.user.service;

import com.gitauto.drivecare.user.dto.LoginRequestDto;
import com.gitauto.drivecare.user.dto.RegisterRequestDto;
import com.gitauto.drivecare.database.user_info.entity.UserInfoEntity;
import com.gitauto.drivecare.database.user_info.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserInfoRepository userInfoRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final PasswordEncoder passwordEncoder;

    public UserInfoEntity login(LoginRequestDto dto) {
        UserInfoEntity user = userInfoRepository.findByUserId(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 아이디입니다."));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        return user;
    }

    public boolean checkUserIdDuplicate(String userId) {
        return userInfoRepository.existsByUserId(userId);
    }

    public void register(RegisterRequestDto dto) {
        // id 중복체크
        if (userInfoRepository.existsByUserId(dto.getUserId())) {
            throw new IllegalStateException("이미 사용 중인 아이디입니다.");
        }

        // 비밀번호 확인
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new IllegalStateException("비밀번호와 확인이 일치하지 않습니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        // 엔터티 생성
        UserInfoEntity user = UserInfoEntity.builder()
                .userId(dto.getUserId())
                .password(encodedPassword)
                .email(dto.getEmail())
                .name(dto.getName())
                .auth(dto.getAuth())
                .build();

        userInfoRepository.save(user);
    }
}
