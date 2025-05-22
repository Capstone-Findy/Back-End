package com.example.findy.api.auth.controller;

import com.example.findy._core.infrastructure.servlet.WebClientResponse;
import com.example.findy._testutils.ControllerTest;
import com.example.findy._testutils.fixture.AuthFixture;
import com.example.findy.api.auth.dto.request.RefreshReq;
import com.example.findy.api.auth.dto.request.SignInReq;
import com.example.findy.api.auth.dto.request.SignUpReq;
import com.example.findy.api.auth.dto.request.ValidMailReq;
import com.example.findy.api.auth.dto.response.LogoutRes;
import com.example.findy.api.auth.dto.response.RefreshRes;
import com.example.findy.api.auth.dto.response.SignInRes;
import com.example.findy.api.auth.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static com.example.findy._testutils.ApiDocumentUtils.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@WebMvcTest(AuthController.class)
class AuthControllerTest extends ControllerTest {

    @MockitoBean
    private AuthService authService;

    @Test
    void sendValidEmail() throws Exception{
        ValidMailReq validMailReq = AuthFixture.validMailReq();

        this.mockMvc
                .perform(post("/valid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validMailReq)))
                .andExpect(status().is2xxSuccessful())
                .andDo(document(
                        "{method-name}",
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("인증메일 발송 이메일")
                        ),
                        responseFieldsForCommonResult()
                ));
    }

    @Test
    void signIn() throws Exception{
        SignInReq req = AuthFixture.signInReq();
        SignInRes res = AuthFixture.signInRes();

        when(authService.signIn(any(WebClientResponse.class), eq(req))).thenReturn(res);

        this.mockMvc
                .perform(post("/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().is2xxSuccessful())
                .andDo(document(
                        "{method-name}",
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                                fieldWithPath("rememberMe").type(JsonFieldType.BOOLEAN).description("자동로그인 여부")
                        ),
                        responseFieldsForSingleResult(
                                fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("리프레시 토큰")
                        )
                ));
    }

    @Test
    void signUp() throws Exception{
        SignUpReq req = AuthFixture.signUpReq();

        this.mockMvc
                .perform(post("/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().is2xxSuccessful())
                .andDo(document(
                        "{method-name}",
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("닉네임"),
                                fieldWithPath("file").type(JsonFieldType.STRING).description("프로필 사진"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
                        ),
                        responseFieldsForCommonResult()
                ));
    }

    @Test
    void logout() throws Exception{
        LogoutRes res = AuthFixture.logoutRes();

        when(authService.logout(any(WebClientResponse.class))).thenReturn(res);

        this.mockMvc
                .perform(post("/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(document(
                        "auth/{method-name}",
                        responseFieldsForSingleResult(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("로그아웃 유저 아이디")
                        )
                ));
    }

    @Test
    void refresh() throws Exception{
        RefreshReq req = AuthFixture.refreshReq();
        RefreshRes res = AuthFixture.refreshRes();

        when(authService.refresh(any(WebClientResponse.class), eq(req))).thenReturn(res);

        this.mockMvc
                .perform(post("/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().is2xxSuccessful())
                .andDo(document(
                        "auth/{method-name}",
                        requestFields(
                                fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("리프레시 토큰")
                        ),
                        responseFieldsForSingleResult(
                                fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("새로 발급한 리프레시 토큰")
                        )
                ));
    }

}