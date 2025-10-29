package com.gitauto.drivecare.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gitauto.drivecare.api.dto.TokenResponseDto;
import com.gitauto.drivecare.api.dto.UserCarInfoResponseDto;
import com.gitauto.drivecare.api.service.AuthService;
import com.gitauto.drivecare.config.SecurityConfig;
import com.gitauto.drivecare.database.user_info.entity.UserInfoEntity;
import com.gitauto.drivecare.security.JwtAuthenticationFilter;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
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

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
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

    private UserInfoEntity userInfo;
    private TokenResponseDto tokenResponseDto;
    private UserCarInfoResponseDto userCarInfoResponseDto;
    private Map<String, Object> loginResult;

    @BeforeEach
    void init() {
        userInfo = UserInfoEntity.builder()
                .userId("test02")
                .password("1234")
                .build();

        tokenResponseDto = TokenResponseDto.builder()
                .accessToken("access-token")
                .refreshToken("refresh-token")
                .tokenType("Bearer")
                .accessTokenExpiresIn(3600L)
                .refreshTokenExpiresIn(86400L)
                .build();

        userCarInfoResponseDto = UserCarInfoResponseDto.builder()
                .maker("현대")
                .model("쏘나타")
                .year("2020")
                .engine("2.0L")
                .carNumber("12가 3456")
                .build();

        loginResult = new HashMap<>();
        loginResult.put("tokenInfo", tokenResponseDto);
        loginResult.put("userCarInfo", userCarInfoResponseDto);
    }

    @Test
    @DisplayName("로그인 API 정상 동작")
    void login_success() throws Exception {
        // given
        Mockito.when(authService.login(eq("test02"), eq("1234"))).thenReturn(loginResult);

        // when
        ResultActions result = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userInfo)));

        System.out.println(result.andReturn().getResponse().getContentAsString());

        // then
        result.andDo(document("auth-login",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("userId").description("사용자 ID"),
                                fieldWithPath("password").description("사용자 비밀번호").optional(),
                                fieldWithPath("id").ignored(),
                                fieldWithPath("name").ignored(),
                                fieldWithPath("email").ignored(),
                                fieldWithPath("telNo").ignored(),
                                fieldWithPath("auth").ignored(),
                                fieldWithPath("creDt").ignored(),
                                fieldWithPath("uptDt").ignored(),
                                fieldWithPath("delDt").ignored(),
                                fieldWithPath("userStatus").ignored(),
                                fieldWithPath("pwAltrDt").ignored(),
                                fieldWithPath("pwErrCnt").ignored(),
                                fieldWithPath("deviceId").ignored(),
                                fieldWithPath("carCenterId").ignored()
                        ),
                        responseFields(
                                fieldWithPath("success").description("요청 성공 여부"),
                                fieldWithPath("data.userCarInfo").description("사용자 자동차 정보"),
                                fieldWithPath("data.userCarInfo.maker").description("자동차 메이커"),
                                fieldWithPath("data.userCarInfo.model").description("자동차 모델 정보"),
                                fieldWithPath("data.userCarInfo.year").description("자동차 연식 정보"),
                                fieldWithPath("data.userCarInfo.engine").description("자동차 엔진 정보"),
                                fieldWithPath("data.userCarInfo.carNumber").description("차량번호"),
                                fieldWithPath("data.tokenInfo").description("토큰 정보"),
                                fieldWithPath("data.tokenInfo.accessToken").description("새로 발급된 Access Token"),
                                fieldWithPath("data.tokenInfo.refreshToken").description("새로 발급된 Refresh Token"),
                                fieldWithPath("data.tokenInfo.tokenType").description("토큰 타입"),
                                fieldWithPath("data.tokenInfo.accessTokenExpiresIn").description("Access Token 만료 시간(초)"),
                                fieldWithPath("data.tokenInfo.refreshTokenExpiresIn").description("Refresh Token 만료 시간(초)"),
                                fieldWithPath("message").description("메시지").optional()
                        )
                ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.tokenInfo.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.data.tokenInfo.refreshToken").isNotEmpty());
    }

    @Test
    @DisplayName("리프레시 API 정상 동작")
    void refresh_success() throws Exception {
        // given
        // 1. 로그인 시 반환될 토큰 정의
        // 2. 리프레시 시 반환될 새 토큰 정의
        TokenResponseDto refreshedResponse = TokenResponseDto.builder()
                .accessToken("new-access-token")
                .refreshToken("new-refresh-token")
                .accessTokenExpiresIn(3600L)
                .refreshTokenExpiresIn(86400L)
                .build();

        // 3. Mock 동작 정의
        Mockito.when(authService.login(eq("test02"), eq("1234"))).thenReturn(loginResult);
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
        String refreshToken = JsonPath.read(loginResponseJson, "$.data.tokenInfo.refreshToken");

        // 5. refresh 요청 (로그인 결과 토큰 사용)
        Map<String, Object> request = Map.of("refreshToken", refreshToken);

        // when
        ResultActions result = mockMvc.perform(post("/api/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        System.out.println(result.andReturn().getResponse().getContentAsString());

        // then
        result.andDo(document("auth-refresh",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("refreshToken").description("발급받은 refresh token")
                        ),
                        responseFields(
                                fieldWithPath("success").description("요청 성공 여부"),
                                fieldWithPath("data.accessToken").description("새로 발급된 Access Token"),
                                fieldWithPath("data.refreshToken").description("새로 발급된 Refresh Token"),
                                fieldWithPath("data.tokenType").description("토큰 타입"),
                                fieldWithPath("data.accessTokenExpiresIn").description("Access Token 만료 시간(초)"),
                                fieldWithPath("data.refreshTokenExpiresIn").description("Refresh Token 만료 시간(초)"),
                                fieldWithPath("message").description("메시지").optional()
                        )
                ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.data.refreshToken").isNotEmpty());
    }
}
