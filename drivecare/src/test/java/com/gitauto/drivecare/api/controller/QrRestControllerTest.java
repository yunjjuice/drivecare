package com.gitauto.drivecare.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gitauto.drivecare.api.service.AuthService;
import com.gitauto.drivecare.api.service.QrService;
import com.gitauto.drivecare.config.SecurityConfig;
import com.gitauto.drivecare.security.JwtAuthenticationFilter;
import com.gitauto.drivecare.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.util.InMemoryResource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = QrRestController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = JwtAuthenticationFilter.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = SecurityConfig.class)
        }
)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
class QrRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private QrService qrService;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    private String token;
    private String userId;

    @BeforeEach
    void init() {
        token = "Bearer test-token";
        userId = "testuser";
    }

    @Test
    void getQrHomepage() throws Exception {
        byte[] testImage = {1, 2, 3, 4};

        given(jwtTokenProvider.getSubject(any())).willReturn(userId);
        given(qrService.getHomePageQr()).willReturn(new InMemoryResource(testImage));

        mockMvc.perform(get("/api/qr/homepage")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(content().bytes(testImage))
                .andDo(document("qr/homepage",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }
}