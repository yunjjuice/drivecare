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

    public TokenResponseDto refresh(String refreshToken) {
        RefreshTokenEntity entity = refreshTokenRepository.findByTokenAndRevoked(refreshToken, 'N')
                .orElseThrow(() -> new ApiException(ErrorCode.UNAUTHORIZED, "리프레시 토큰 없음/만료"));

        String subject = jwtTokenProvider.getSubject(refreshToken);

        if (!entity.getUser().getUserId().equals(subject)) {
            throw new ApiException(ErrorCode.UNAUTHORIZED, "토큰 사용자 불일치");
        }

        if (entity.getExpiresAt() != null && entity.getExpiresAt().isBefore(LocalDateTime.now())) {
            entity.setRevoked('Y');
            refreshTokenRepository.save(entity);
            throw new ApiException(ErrorCode.UNAUTHORIZED, "리프레시 토큰 만료");
        }

        UserInfoEntity user = entity.getUser();

        // access token 재발급
        String newAccessToken = jwtTokenProvider.generateAccessToken(
                user.getUserId(),
                Map.of("uid", user.getId(), "role", user.getAuth()));

        // 전달받은 refresh 비활성화 후 refresh token 재발급
        entity.setRevoked('Y');
        refreshTokenRepository.save(entity);

        String newRefreshToken = jwtTokenProvider.generateRefreshToken(user.getUserId());
        RefreshTokenEntity newEntity = RefreshTokenEntity.builder()
                .user(user)
                .token(newRefreshToken)
                .expiresAt(LocalDateTime.now().plusSeconds(jwtTokenProvider.getRefreshTokenExpirationSeconds()))
                .revoked('N')
                .build();
        refreshTokenRepository.save(newEntity);

        return TokenResponseDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .accessTokenExpiresIn(jwtTokenProvider.getAccessTokenExpirationSeconds())
                .refreshTokenExpiresIn(jwtTokenProvider.getRefreshTokenExpirationSeconds())
                .build();
    }
}
