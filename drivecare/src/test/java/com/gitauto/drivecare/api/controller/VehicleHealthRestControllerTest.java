package com.gitauto.drivecare.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gitauto.drivecare.api.dto.ReservationResponseDto;
import com.gitauto.drivecare.api.dto.VehicleHealthDetailResponseDto;
import com.gitauto.drivecare.api.dto.VehicleHealthSaveRequestDto;
import com.gitauto.drivecare.api.service.QrService;
import com.gitauto.drivecare.api.service.VehicleHealthService;
import com.gitauto.drivecare.config.SecurityConfig;
import com.gitauto.drivecare.security.JwtAuthenticationFilter;
import com.gitauto.drivecare.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = VehicleHealthRestController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = JwtAuthenticationFilter.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = SecurityConfig.class)
        }
)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
class VehicleHealthRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private VehicleHealthService vehicleHealthService;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    private String token;
    private String userId;
    private VehicleHealthSaveRequestDto vehicleHealthSave1;
    private VehicleHealthDetailResponseDto vehicleHealth1;

    @BeforeEach
    void init() {
        token = "Bearer test-token";
        userId = "testuser";

        vehicleHealthSave1 = new VehicleHealthSaveRequestDto();
        vehicleHealthSave1.setBatteryCharge(80);
        vehicleHealthSave1.setDrivingRange(200.5);
        vehicleHealthSave1.setEngineOilLv("NORMAL");
        vehicleHealthSave1.setWasherFluidWarnYn('Y');
        vehicleHealthSave1.setIndicatorWarnYn('N');
        vehicleHealthSave1.setAirbagYn('Y');

        vehicleHealth1 = new VehicleHealthDetailResponseDto();

        vehicleHealth1.setDtcList(List.of(new VehicleHealthDetailResponseDto.DtcDetailDto("A0001", "테스트 고장코드1")));
        vehicleHealth1.setBatteryCharge(80);
        vehicleHealth1.setDrivingRange(200.5);
        vehicleHealth1.setEngineOilLv("NORMAL");
        vehicleHealth1.setWasherFluidWarnYn('Y');
        vehicleHealth1.setIndicatorWarnYn('N');
        vehicleHealth1.setAirbagYn('Y');
    }

    @Test
    void getVehicleHealthDetail() throws Exception {
        given(jwtTokenProvider.getSubject(any())).willReturn(userId);
        given(vehicleHealthService.vehicleHealthDetail(userId)).willReturn(vehicleHealth1);

        mockMvc.perform(get("/api/vehicle-health/detail")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.dtcList[0].dtcCd").value(vehicleHealth1.getDtcList().getFirst().getDtcCd()))
                .andExpect(jsonPath("$.data.dtcList[0].dtcDesc").value(vehicleHealth1.getDtcList().getFirst().getDtcDesc()))
                .andExpect(jsonPath("$.data.batteryCharge").value(vehicleHealth1.getBatteryCharge()))
                .andExpect(jsonPath("$.data.drivingRange").value(vehicleHealth1.getDrivingRange()))
                .andExpect(jsonPath("$.data.engineOilLv").value(vehicleHealth1.getEngineOilLv()))
                .andExpect(jsonPath("$.data.washerFluidWarnYn").value(String.valueOf(vehicleHealth1.getWasherFluidWarnYn())))
                .andExpect(jsonPath("$.data.indicatorWarnYn").value(String.valueOf(vehicleHealth1.getIndicatorWarnYn())))
                .andExpect(jsonPath("$.data.airbagYn").value(String.valueOf(vehicleHealth1.getAirbagYn())))
                .andExpect(jsonPath("$.message").isEmpty())
                .andDo(document("vehicle-health/detail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("success").description("성공 여부"),
                                fieldWithPath("data.dtcList[].dtcCd").description("고장 코드(DTC)"),
                                fieldWithPath("data.dtcList[].dtcDesc").description("고장 코드 설명"),
                                fieldWithPath("data.batteryCharge").description("배터리 잔량(%)"),
                                fieldWithPath("data.drivingRange").description("주행 가능 거리(km)"),
                                fieldWithPath("data.engineOilLv").description("엔진 오일 잔량"),
                                fieldWithPath("data.washerFluidWarnYn").description("워셔액 부족 경고 여부(Y/N)"),
                                fieldWithPath("data.indicatorWarnYn").description("경고등 점등 여부(Y/N)"),
                                fieldWithPath("data.airbagYn").description("에어백 경고 여부(Y/N)"),
                                fieldWithPath("message").description("응답 메세지")
                        )
                ));
    }

    @Test
    void saveVehicleHealth() throws Exception {
        given(jwtTokenProvider.getSubject(any())).willReturn(userId);
        doNothing().when(vehicleHealthService).saveVehicleHealth(userId, vehicleHealthSave1);

        mockMvc.perform(post("/api/vehicle-health/save")
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehicleHealthSave1)))
                .andExpect(status().isOk())
                .andDo(document("vehicle-health/save",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("batteryCharge").description("배터리 잔량(%)"),
                                fieldWithPath("drivingRange").description("주행 가능 거리(km)"),
                                fieldWithPath("engineOilLv").description("엔진 오일 잔량"),
                                fieldWithPath("washerFluidWarnYn").description("워셔액 부족 경고 여부(Y/N)").attributes(key("constraint").value("Not Null, Y/N")),
                                fieldWithPath("indicatorWarnYn").description("경고등 점등 여부(Y/N)").attributes(key("constraint").value("Not Null, Y/N")),
                                fieldWithPath("airbagYn").description("에어백 경고 여부(Y/N)").attributes(key("constraint").value("Not Null, Y/N"))
                        )
                ));
    }
}