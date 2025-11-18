package com.example.findy.api.user.controller;

import com.example.findy._testutils.ControllerTest;
import com.example.findy._testutils.fixture.UserFixture;
import com.example.findy.api.user.dto.request.UpdateItem;
import com.example.findy.api.user.dto.response.UserRes;
import com.example.findy.api.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static com.example.findy._testutils.ApiDocumentUtils.*;
import static com.example.findy._testutils.ApiDocumentUtils.responseFieldsForCommonResult;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@WebMvcTest(UserController.class)
class UserControllerTest extends ControllerTest {

    @MockitoBean
    private UserService userService;

    @Test
    void updateHeart() throws Exception {
        int cnt = 1;

        this.mockMvc
                .perform(patch("/auth/user/heart/{cnt}", cnt)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(document(
                        "{method-name}",
                        responseFieldsForCommonResult()
                ));
    }

    @Test
    void updateItem() throws Exception {
        UpdateItem req = UserFixture.updateItem();

        this.mockMvc
                .perform(patch("/auth/user/item")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().is2xxSuccessful())
                .andDo(document(
                        "{method-name}",
                        requestFields(
                                fieldWithPath("item1").type(JsonFieldType.NUMBER).description("아이템1 증가 / 감소 개수"),
                                fieldWithPath("item2").type(JsonFieldType.NUMBER).description("아이템2 증가 / 감소 개수"),
                                fieldWithPath("item3").type(JsonFieldType.NUMBER).description("아이템3 증가 / 감소 개수")
                        ),
                        responseFieldsForCommonResult()
                ));
    }

    @Test
    void getUser() throws Exception {
        UserRes res = UserFixture.userRes();
        when(userService.getUser()).thenReturn(res);
        this.mockMvc
                .perform(get("/auth/user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(document(
                        "{method-name}",
                        responseFieldsForSingleResult(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("사용자 이름"),
                                fieldWithPath("money").type(JsonFieldType.NUMBER).description("사용자 소지 금액"),
                                fieldWithPath("heart").type(JsonFieldType.NUMBER).description("사용자 하트 수"),
                                fieldWithPath("item1").type(JsonFieldType.NUMBER).description("아이템1 증가 / 감소 개수"),
                                fieldWithPath("item2").type(JsonFieldType.NUMBER).description("아이템2 증가 / 감소 개수"),
                                fieldWithPath("item3").type(JsonFieldType.NUMBER).description("아이템3 증가 / 감소 개수")
                        )
                ));
    }
}