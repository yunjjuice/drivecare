package com.gitauto.drivecare.user.service;

import com.gitauto.drivecare.user.dto.LoginRequestDto;
import com.gitauto.drivecare.user.dto.RegisterRequestDto;
import com.gitauto.drivecare.user.entity.UserEntity;
import com.gitauto.drivecare.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final PasswordEncoder passwordEncoder;

    public UserEntity login(LoginRequestDto dto) {
        UserEntity user = userRepository.findByUserId(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 아이디입니다."));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        return user;
    }

    public boolean checkUserIdDuplicate(String userId) {
        return userRepository.existsByUserId(userId);
    }

    public void register(RegisterRequestDto dto) {
        // id 중복체크
        if (userRepository.existsByUserId(dto.getUserId())) {
            throw new IllegalStateException("이미 사용 중인 아이디입니다.");
        }

        // 비밀번호 확인
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new IllegalStateException("비밀번호와 확인이 일치하지 않습니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        // 엔터티 생성
        UserEntity user = UserEntity.builder()
                .userId(dto.getUserId())
                .password(encodedPassword)
                .email(dto.getEmail())
                .name(dto.getName())
                .auth(dto.getAuth())
                .build();

        userRepository.save(user);
    }
}
