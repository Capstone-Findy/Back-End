package com.example.findy.api.auth.controller;

import com.example.findy._core.client.kakao.dto.request.KakaoCodeReq;
import com.example.findy._core.dto.ApiResponse;
import com.example.findy._core.dto.CommonResult;
import com.example.findy._core.dto.SingleResult;
import com.example.findy._core.infrastructure.servlet.WebClientResponse;
import com.example.findy.api.auth.dto.request.RefreshReq;
import com.example.findy.api.auth.dto.request.SignUpReq;
import com.example.findy.api.auth.dto.request.SignInReq;
import com.example.findy.api.auth.dto.request.ValidMailReq;
import com.example.findy.api.auth.dto.response.LogoutRes;
import com.example.findy.api.auth.dto.response.RefreshRes;
import com.example.findy.api.auth.dto.response.SignInRes;
import com.example.findy.api.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/valid")
    public ResponseEntity<CommonResult> sendValidEmail(
            @RequestBody @Valid ValidMailReq req
    ) {
        authService.sendValidMail(req);
        return ApiResponse.ok();
    }

    @GetMapping("/valid/{email}")
    public ResponseEntity<CommonResult> validEmail(
            @PathVariable String email
    ) {
        authService.validEmail(email);
        return ApiResponse.ok();
    }

    @PostMapping("/sign-up")
    public ResponseEntity<CommonResult> signUp(
            @Valid @RequestBody SignUpReq req
    ) {
        authService.signUp(req);
        return ApiResponse.ok();
    }

    @GetMapping("/kakao/sign-up")
    public ResponseEntity<SingleResult<RefreshRes>> kakaoSignUp(
            WebClientResponse res,
            KakaoCodeReq req
    ) {
        RefreshRes response = authService.kakaoSignUp(res, req);
        return ApiResponse.ok(response);
    }

    @GetMapping("/google/sign-up")
    public ResponseEntity<SingleResult<RefreshRes>> googleSignUp(
            WebClientResponse res,
            @RequestParam("code") String code
    ) {
        RefreshRes response = authService.googleSignUp(res, code);
        return ApiResponse.ok(response);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<SingleResult<SignInRes>> signIn(
            WebClientResponse res,
            @Valid @RequestBody SignInReq req
    ) {
        SignInRes response = authService.signIn(res, req);
        return ApiResponse.ok(response);
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<SingleResult<RefreshRes>> refresh(
            WebClientResponse res,
            @Valid @RequestBody RefreshReq req
    ) {
        RefreshRes response = authService.refresh(res, req);
        return ApiResponse.ok(response);
    }

}
