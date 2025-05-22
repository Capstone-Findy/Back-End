package com.example.findy._testutils.fixture;

import com.example.findy.api.auth.dto.request.RefreshReq;
import com.example.findy.api.auth.dto.request.SignInReq;
import com.example.findy.api.auth.dto.request.SignUpReq;
import com.example.findy.api.auth.dto.request.ValidMailReq;
import com.example.findy.api.auth.dto.response.LogoutRes;
import com.example.findy.api.auth.dto.response.RefreshRes;
import com.example.findy.api.auth.dto.response.SignInRes;

public class AuthFixture {
    public static ValidMailReq validMailReq() {
        return new ValidMailReq("test@gmail.com");
    }
    public static SignUpReq signUpReq() {
        return new SignUpReq(
                "nickname",
                "file",
                "test@gmail.com",
                "password1!"
        );
    }
    public static SignInReq signInReq() {
        return new SignInReq(
                "test@gmail.com",
                "password1!",
                true
        );
    }

    public static SignInRes signInRes() {
        return new SignInRes("refreshToken");
    }

    public static RefreshReq refreshReq() {
        return new RefreshReq("refreshToken");
    }

    public static RefreshRes refreshRes() {
        return new RefreshRes("newRefreshToken");
    }

    public static LogoutRes logoutRes() {
        return new LogoutRes(1L);
    }
}
