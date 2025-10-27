package com.gitauto.drivecare;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gitauto.drivecare.api.TestController;
import com.gitauto.drivecare.api.TestDto;
import com.gitauto.drivecare.security.JwtAuthenticationFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TestController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                value = JwtAuthenticationFilter.class)
)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
class DrivecareApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private TestDto testDto;

    @BeforeEach
    void init() {
        testDto = TestDto.builder()
                .name("ABC")
                .description("I'm a test.")
                .build();
    }

    @Test
    void contextLoads() {
    }

    @Test
    @DisplayName("RestDocs 테스트")
    void restDocsTest() throws Exception {
        mockMvc.perform(post("/test/hello")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(testDto.getName()))
                .andExpect(jsonPath("$.description").value(testDto.getDescription()))
                .andDo(document("test-doc",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("name").description("사용자 이름").attributes(key("constraint").value("Not Null")),
                                fieldWithPath("description").description("설명")
                                ),
                        responseFields(
                                fieldWithPath("name").description("사용자 이름"),
                                fieldWithPath("description").description("설명")
                        )
                ));
    }
}
