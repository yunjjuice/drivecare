package com.gitauto.drivecare.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gitauto.drivecare.api.dto.TokenResponseDto;
import com.gitauto.drivecare.api.service.AuthService;
import com.gitauto.drivecare.config.SecurityConfig;
import com.gitauto.drivecare.database.user_info.entity.UserInfoEntity;
import com.gitauto.drivecare.security.JwtAuthenticationFilter;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Map;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = AuthRestController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = JwtAuthenticationFilter.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = SecurityConfig.class)
        }
)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
class AuthRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

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

        Mockito.when(authService.login(eq("test02"), eq("1234"))).thenReturn(tokenResponse);

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
        // 1. 로그인 시 반환될 토큰 정의
        TokenResponseDto loginResponse = TokenResponseDto.builder()
                .accessToken("access-token")
                .refreshToken("refresh-token") // 로그인 후 받은 토큰
                .accessTokenExpiresIn(3600L)
                .refreshTokenExpiresIn(86400L)
                .build();

        // 2. 리프레시 시 반환될 새 토큰 정의
        TokenResponseDto refreshedResponse = TokenResponseDto.builder()
                .accessToken("new-access-token")
                .refreshToken("new-refresh-token")
                .accessTokenExpiresIn(3600L)
                .refreshTokenExpiresIn(86400L)
                .build();

        // 3. Mock 동작 정의
        Mockito.when(authService.login(eq("test02"), eq("1234"))).thenReturn(loginResponse);
        Mockito.when(authService.refresh(eq("refresh-token"))).thenReturn(refreshedResponse);

        // 4. 로그인 요청
        UserInfoEntity userInfo = UserInfoEntity.builder()
                .userId("test02")
                .password("1234")
                .build();

        ResultActions loginResult = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userInfo)));

        String loginResponseJson = loginResult.andReturn().getResponse().getContentAsString();
        String refreshToken = JsonPath.read(loginResponseJson, "$.data.refreshToken");

        // 5. refresh 요청 (로그인 결과 토큰 사용)
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
