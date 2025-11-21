package com.example.findy.api.game.origin.controller;

import com.example.findy._testutils.ControllerTest;
import com.example.findy._testutils.docs.DocumentLinkGenerator;
import com.example.findy._testutils.fixture.OriginFixture;
import com.example.findy.api.game.origin.dto.request.ResultReq;
import com.example.findy.api.game.origin.dto.response.ResultRes;
import com.example.findy.api.game.origin.service.OriginService;
import com.example.findy.entity.game.origin.entity.Country;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static com.example.findy._testutils.ApiDocumentUtils.*;
import static com.example.findy._testutils.ApiDocumentUtils.fieldWithPath;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(OriginController.class)
class OriginControllerTest extends ControllerTest {

    @MockitoBean
    private OriginService originService;

    @Test
    void updateResult() throws Exception {
        ResultReq req = OriginFixture.resultReq();
        this.mockMvc
                .perform(post("/auth/origin/result")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().is2xxSuccessful())
                .andDo(document(
                        "{method-name}",
                        requestFields(
                                fieldWithPath("gameId").type(JsonFieldType.NUMBER).description("game ID"),
                                fieldWithPath("remainTime").type(JsonFieldType.NUMBER).description("남은 시간"),
                                fieldWithPath("correct").type(JsonFieldType.NUMBER).description("정답 수"),
                                fieldWithPath("item1").type(JsonFieldType.NUMBER).description("아이템1 증가 / 감소 개수"),
                                fieldWithPath("item2").type(JsonFieldType.NUMBER).description("아이템2 증가 / 감소 개수"),
                                fieldWithPath("item3").type(JsonFieldType.NUMBER).description("아이템3 증가 / 감소 개수")
                        ),
                        responseFieldsForCommonResult()
                ));
    }

    @Test
    void getScore() throws Exception {
        Country country = Country.USA;
        List<ResultRes> res = OriginFixture.resultResList();

        when(originService.getResult(any(Country.class))).thenReturn(res);
        this.mockMvc
                .perform(get("/auth/origin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("country", String.valueOf(country)))
                .andExpect(status().is2xxSuccessful())
                .andDo(document(
                        "{method-name}",
                        queryParameters(
                                parameterWithName("country").description(DocumentLinkGenerator.generateLinkCode(Country.class))
                        ),
                        responseFieldsForListResult(
                                fieldWithPath("gameId").type(JsonFieldType.NUMBER).description("game ID"),
                                fieldWithPath("score").type(JsonFieldType.NUMBER).description("게임 점수")
                        )
                ));
    }
}