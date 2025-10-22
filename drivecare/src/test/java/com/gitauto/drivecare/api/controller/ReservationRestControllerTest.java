package com.gitauto.drivecare.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gitauto.drivecare.api.dto.ReservationResponseDto;
import com.gitauto.drivecare.api.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReservationRestController.class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class ReservationRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReservationService reservationService;

    @Autowired
    private ObjectMapper objectMapper;

    private ReservationResponseDto reservation1;
    private List<ReservationResponseDto> reservationResponseDtoList;

    @BeforeEach
    void init() {
        reservation1 = new ReservationResponseDto();

        reservation1.setId(1L);
        reservation1.setAddress("서울특별시 성동구 아차산로 123");
        reservation1.setReserveDt(LocalDateTime.now().plusDays(3));
        reservation1.setCarCenterNm("성수카센터");
        reservation1.setTelNo("02-1234-5678");
        reservation1.setApproveStatus('P');
        reservation1.setDesc("엔진 오일 교체 및 타이어 공기압 체크 요청합니다.");
        reservation1.setCarModel("현대 쏘나타 DN8");
        reservation1.setCarNumber("12가 3456");
        reservation1.setRepairDesc(null);

        reservationResponseDtoList = new ArrayList<>();
        reservationResponseDtoList.add(reservation1);
    }

    @Test
    void getReservationList() throws Exception {
        given(reservationService.reservationList()).willReturn(reservationResponseDtoList);

        mockMvc.perform(get("/api/reservation/list")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(reservation1.getId()))
                .andExpect(jsonPath("$[0].address").value(reservation1.getAddress()))
                .andExpect(jsonPath("$[0].carCenterNm").value(reservation1.getCarCenterNm()))
                .andExpect(jsonPath("$[0].telNo").value(reservation1.getTelNo()))
                .andExpect(jsonPath("$[0].approveStatus").value(String.valueOf(reservation1.getApproveStatus())))
                .andExpect(jsonPath("$[0].desc").value(reservation1.getDesc()))
                .andExpect(jsonPath("$[0].carModel").value(reservation1.getCarModel()))
                .andExpect(jsonPath("$[0].carNumber").value(reservation1.getCarNumber()))
                .andExpect(jsonPath("$[0].repairDesc").isEmpty())
                .andDo(document("reservation/list",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].id").description("예약 ID"),
                                fieldWithPath("[].address").description("정비소 주소"),
                                fieldWithPath("[].reserveDt").description("예약 날짜 및 시간"),
                                fieldWithPath("[].carCenterNm").description("정비소 이름"),
                                fieldWithPath("[].telNo").description("정비소 전화번호"),
                                fieldWithPath("[].approveStatus").description("예약 상태 (Y: 확정, P: 대기, N: 거절)"),
                                fieldWithPath("[].desc").description("증상 설명"),
                                fieldWithPath("[].carModel").description("차량 모델명"),
                                fieldWithPath("[].carNumber").description("차량 번호"),
                                fieldWithPath("[].repairDesc").description("정비 상세 내역").optional()
                        )
                ));
    }
}