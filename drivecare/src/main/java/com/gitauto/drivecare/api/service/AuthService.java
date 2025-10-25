package com.gitauto.drivecare.api.service;

import com.gitauto.drivecare.api.dto.TokenResponseDto;
import com.gitauto.drivecare.database.refresh_token.entity.RefreshTokenEntity;
import com.gitauto.drivecare.database.refresh_token.repository.RefreshTokenRepository;
import com.gitauto.drivecare.database.user_info.entity.UserInfoEntity;
import com.gitauto.drivecare.database.user_info.repository.UserInfoRepository;
import com.gitauto.drivecare.exception.ApiException;
import com.gitauto.drivecare.exception.ErrorCode;
import com.gitauto.drivecare.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserInfoRepository userInfoRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenResponseDto login(String userId, String password) {
        UserInfoEntity userInfo = userInfoRepository.findByUserId(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.INVALIE_USERID, ErrorCode.INVALIE_USERID.getDefaultMessage()));

        // TODO : 비밀번호 검증 필요한지 검토

        String accessToken= jwtTokenProvider.generateAccessToken(userId,
                Map.of("uid", userInfo.getId(), "role", userInfo.getAuth()));

        String refreshToken = jwtTokenProvider.generateRefreshToken(userId);

        RefreshTokenEntity entity = RefreshTokenEntity.builder()
                .user(userInfo)
                .token(refreshToken)
                .expiresAt(LocalDateTime.now().plusSeconds(jwtTokenProvider.getRefreshTokenExpirationSeconds()))
                .revoked('N')
                .build();
        refreshTokenRepository.save(entity);

        return TokenResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpiresIn(jwtTokenProvider.getAccessTokenExpirationSeconds())
                .refreshTokenExpiresIn(jwtTokenProvider.getRefreshTokenExpirationSeconds())
                .build();
    }
}
