package com.gitauto.drivecare.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gitauto.drivecare.api.dto.DrivingResponseDto;
import com.gitauto.drivecare.api.service.DrivingDataService;
import com.gitauto.drivecare.config.SecurityConfig;
import com.gitauto.drivecare.database.user_driving_stat.entity.UserDrivingStatEntity;
import com.gitauto.drivecare.security.JwtAuthenticationFilter;
import com.gitauto.drivecare.security.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(
        controllers = DrivingDataRestController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = JwtAuthenticationFilter.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = SecurityConfig.class)
        }
)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
class DrivingDataRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DrivingDataService drivingDataService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("운전 데이터 저장 API 성공 테스트")
    void saveDrivingData_success() throws Exception {
        // given
        String token = "Bearer test-token";
        String userId = "testuser";

        UserDrivingStatEntity drivingData = UserDrivingStatEntity.builder()
                .driveScore(95D)
                .accelCount(2)
                .brakeCount(3)
                .handleMissCount(1)
                .creDt(LocalDateTime.now())
                .build();

        Mockito.when(jwtTokenProvider.getSubject(any())).thenReturn(userId);
        Mockito.when(drivingDataService.saveDrivingData(eq(userId), any(UserDrivingStatEntity.class)))
                .thenReturn("데이터 저장 완료");

        // when & then
        mockMvc.perform(post("/api/driving/save")
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(drivingData)))
                .andDo(print())
                .andDo(document("driving-save",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value("데이터 저장 완료"))
                .andExpect(jsonPath("$.message").doesNotExist());
    }

    @Test
    @DisplayName("운전 데이터 리스트 API 성공 테스트")
    void getDrivingData_success() throws Exception {
        // given
        String token = "Bearer test-token";
        String userId = "testuser";

        DrivingResponseDto.DrivingScoreDto recentDto = DrivingResponseDto.DrivingScoreDto.builder()
                .driveScore(80D)
                .accelCount(2D)
                .brakeCount(3D)
                .handleMissCount(1D)
                .build();

        DrivingResponseDto.DrivingScoreDto lastMonthDto = DrivingResponseDto.DrivingScoreDto.builder()
                .driveScore(88.5)
                .accelCount(2.5)
                .brakeCount(1D)
                .handleMissCount(2D)
                .build();

        DrivingResponseDto drivingResult = DrivingResponseDto.builder()
                .recentScore(recentDto)
                .lastMonthScore(lastMonthDto)
                .build();

        Mockito.when(jwtTokenProvider.getSubject(any())).thenReturn(userId);
        Mockito.when(drivingDataService.getDrivingData(eq(userId))).thenReturn(drivingResult);

        // when & then
        mockMvc.perform(post("/api/driving/list")
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(document("driving-list",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}

