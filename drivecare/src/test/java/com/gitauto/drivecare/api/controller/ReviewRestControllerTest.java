package com.gitauto.drivecare.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gitauto.drivecare.api.dto.ReviewRequestDto;
import com.gitauto.drivecare.api.service.ReviewService;
import com.gitauto.drivecare.config.SecurityConfig;
import com.gitauto.drivecare.security.JwtAuthenticationFilter;
import com.gitauto.drivecare.security.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(
    controllers = ReviewRestController.class,
    excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = JwtAuthenticationFilter.class),
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = SecurityConfig.class)
    }
)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
public class ReviewRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ReviewService reviewService;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("리뷰 작성 API 테스트")
    void testCreateReview() throws Exception {
        // given
        String token = "Bearer test-token";;
        String userId = "testuser";

        ReviewRequestDto dto = ReviewRequestDto.builder()
                .reservationId(1L)
                .rating(5)
                .reviewText("Great service!")
                .build();

        Mockito.when(jwtTokenProvider.getSubject(any())).thenReturn(userId);
        Mockito.when(reviewService.saveReviewRating(eq(userId), any(ReviewRequestDto.class))).thenReturn("데이터 저장 완료");

        // when & then
        mockMvc.perform(post("/api/review/save")
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andDo(document("review-save",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("reservationId").description("예약 ID"),
                                fieldWithPath("rating").description("평점"),
                                fieldWithPath("reviewText").description("리뷰 내용")
                        ),
                        responseFields(
                                fieldWithPath("success").description("요청 성공 여부"),
                                fieldWithPath("data").description("메세지"),
                                fieldWithPath("message").description("메세지").optional()
                        )))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value("데이터 저장 완료"))
                .andExpect(jsonPath("$.message").doesNotExist());
    }
}
