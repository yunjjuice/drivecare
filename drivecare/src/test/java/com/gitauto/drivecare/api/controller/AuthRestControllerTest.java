package com.gitauto.drivecare.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gitauto.drivecare.api.dto.TokenResponseDto;
import com.gitauto.drivecare.api.service.AuthService;
import com.gitauto.drivecare.database.user_info.entity.UserInfoEntity;
import com.gitauto.drivecare.database.user_info.repository.UserInfoRepository;
import com.gitauto.drivecare.database.refresh_token.repository.RefreshTokenRepository;
import com.gitauto.drivecare.security.JwtTokenProvider;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Map;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private AuthService authService;
    @Mock
    private UserInfoRepository userInfoRepository;
    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("로그인 API 정상 동작")
    void login_success() throws Exception {
        // given
        UserInfoEntity userInfo = UserInfoEntity.builder()
                .userId("test02")
                .password("1234")
                .build();
        TokenResponseDto tokenResponse = TokenResponseDto.builder()
                .accessToken("access-token")
                .refreshToken("refresh-token")
                .accessTokenExpiresIn(3600L)
                .refreshTokenExpiresIn(86400L)
                .build();
        Mockito.when(authService.login(eq("testuser"), eq("testpass"))).thenReturn(tokenResponse);

        // when
        ResultActions result = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userInfo)));

        System.out.println(result.andReturn().getResponse().getContentAsString());

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.data.refreshToken").isNotEmpty());
    }

    @Test
    @DisplayName("리프레시 API 정상 동작")
    void refresh_success() throws Exception {
        // given
        // 1. 로그인하여 refreshToken 발급
        UserInfoEntity userInfo = UserInfoEntity.builder()
                .userId("test02")
                .password("1234")
                .build();
        ResultActions loginResult = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userInfo)));
        String loginResponse = loginResult.andReturn().getResponse().getContentAsString();
        String refreshToken = JsonPath.read(loginResponse, "$.data.refreshToken");

        // 2. 발급받은 refreshToken으로 refresh API 호출
        Map<String, Object> request = Map.of("refreshToken", refreshToken);

        // when
        ResultActions result = mockMvc.perform(post("/api/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        System.out.println(result.andReturn().getResponse().getContentAsString());

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.data.refreshToken").isNotEmpty());
    }
}
