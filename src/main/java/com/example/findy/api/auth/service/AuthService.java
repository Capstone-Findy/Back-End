package com.example.findy.api.auth.service;

import com.example.findy._core.client.google.GoogleClient;
import com.example.findy._core.client.kakao.KakaoClient;
import com.example.findy._core.client.kakao.dto.request.KakaoCodeReq;
import com.example.findy._core.environment.SpringProperties;
import com.example.findy._core.infrastructure.servlet.WebClientResponse;
import com.example.findy.api.auth.dto.JwtAuthentication;
import com.example.findy.api.auth.dto.request.*;
import com.example.findy.api.auth.dto.response.LogoutRes;
import com.example.findy.api.auth.dto.response.RefreshRes;
import com.example.findy.api.auth.dto.response.SignInRes;
import com.example.findy.api.auth.mapper.AuthMapper;
import com.example.findy.entity.auth.entity.BlackList;
import com.example.findy.entity.auth.entity.ValidMail;
import com.example.findy.entity.auth.repository.BlackListRepository;
import com.example.findy.entity.auth.repository.ValidMailRepository;
import com.example.findy.entity.file.entity.File;
import com.example.findy.entity.file.repository.FileRepository;
import com.example.findy.entity.token.GoogleToken;
import com.example.findy.entity.token.KakaoToken;
import com.example.findy.entity.token.Token;
import com.example.findy.entity.token.repository.GoogleTokenRepository;
import com.example.findy.entity.token.repository.KakaoTokenRepository;
import com.example.findy.entity.token.repository.TokenRepository;
import com.example.findy.entity.user.entity.User;
import com.example.findy.entity.user.exception.NotFoundUserException;
import com.example.findy.entity.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtProvider jwtProvider;
    private final AuthMapper authMapper;
    private final JavaMailSender mailSender;
    private final SpringProperties properties;
    private final MailContentBuilder mailContentBuilder;
    private final KakaoClient kakaoClient;
    private final GoogleClient googleClient;
    private final GoogleTokenRepository googleTokenRepository;

    private final TokenRepository tokenRepository;
    private final ValidMailRepository validMailRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final BlackListRepository blackListRepository;
    private final KakaoTokenRepository kakaoTokenRepository;
    private final FileRepository fileRepository;

    @Transactional(readOnly = true)
    public void sendValidMail(ValidMailReq req) {
        userRepository.duplicatedByEmail(req.email());

        String link = "http://localhost:8080/valid/" + req.email();
        String message = mailContentBuilder.build(link);

        sendMail(req, message);
    }

    @Transactional
    public void validEmail(String email) {
        ValidMail validMail = ValidMail.of(email);
        validMailRepository.save(validMail);
    }


    @Transactional
    public RefreshRes kakaoSignUp(WebClientResponse res, KakaoCodeReq req){
        KakaoSignUpReq kakaoSignUpReq = kakaoClient.signUp(req).block();
        User user;

        if(!userRepository.existsByEmail(kakaoSignUpReq.email())){
            File file = File.of(kakaoSignUpReq.file());
            fileRepository.save(file);
            user = User.of(kakaoSignUpReq, file);
            userRepository.save(user);
        }
        else user = userRepository.getByEmail(kakaoSignUpReq.email());

        KakaoToken kakaoToken = KakaoToken.of(user, kakaoSignUpReq);
        kakaoTokenRepository.save(kakaoToken);

        Token token = jwtProvider.issueTokens(user, true);
        res.addTokenCookies(res, token);

        return new RefreshRes(token.getRefreshToken());
    }

    @Transactional
    public RefreshRes googleAuth(WebClientResponse res, String code) {
        GoogleSignUpReq googleSignUpReq = googleClient.signUp(code).block();
        User user;

        // 회원가입 또는 기존 유저 조회
        if (!userRepository.existsByEmail(googleSignUpReq.email())) {
            File file = File.of(googleSignUpReq.picture());
            fileRepository.save(file);

            user = User.of(googleSignUpReq, file);
            userRepository.save(user);
        } else {
            user = userRepository.getByEmail(googleSignUpReq.email());
        }

        // refresh_token 처리
        String refreshToken = googleSignUpReq.refreshToken();
        GoogleToken existingToken = googleTokenRepository.findByUserId(user.getId()).orElse(null);

        if (refreshToken == null && existingToken != null) {
            refreshToken = existingToken.getRefreshToken();
        }
        if (refreshToken == null) {
            throw new RuntimeException("Google refresh_token is missing and cannot be saved.");
        }

        // 저장
        GoogleToken googleToken = GoogleToken.of(user, googleSignUpReq.accessToken(), refreshToken);
        googleTokenRepository.save(googleToken);

        // JWT 발급 및 응답
        Token token = jwtProvider.issueTokens(user, true);
        res.addTokenCookies(res, token);

        return new RefreshRes(token.getRefreshToken());
    }



    @Transactional
    public RefreshRes refresh(WebClientResponse res, RefreshReq req) {
        Token existToken = tokenRepository.getByRefreshToken(req.refreshToken());
        User user = userRepository.getById(existToken.getUserId());

        BlackList blackList = BlackList.of(existToken.getAccessToken());
        blackListRepository.save(blackList);

        if(user.getType() == User.LoginType.KAKAO) {
            KakaoToken kakaoToken = jwtProvider.issueKakaoTokens(user);
        }

        Token token = jwtProvider.issueTokens(user, existToken.isRememberMe());
        res.addTokenCookies(res, token);

        return new RefreshRes(token.getRefreshToken());
    }

    @Transactional
    public LogoutRes logout(WebClientResponse res) {
        res.removeAllCustomCookies();

        Long userId = JwtAuthentication.getUserId();
        User user = userRepository.getById(userId);
        Token token = tokenRepository.getByUserId(userId);

        BlackList blackList = BlackList.of(token.getAccessToken());
        blackListRepository.save(blackList);

        if(user.getType() == User.LoginType.KAKAO) {
            KakaoToken kakaoToken = kakaoTokenRepository.getByUserId(userId);
            kakaoClient.logout(kakaoToken);
            kakaoTokenRepository.delete(kakaoToken);
        }

        if(user.getType() == User.LoginType.GOOGLE) {
            GoogleToken googleToken = googleTokenRepository.getByUserId(userId);
            googleClient.logout(googleToken.getAccessToken());
            googleTokenRepository.delete(googleToken);
        }

        tokenRepository.delete(token);

        return new LogoutRes(userId);
    }

    private void sendMail(ValidMailReq req, String message) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            messageHelper.setFrom(properties.mail().username());
            messageHelper.setTo(req.email());
            messageHelper.setSubject("findy 인증 메일");
            messageHelper.setText(message, true);
        };

        mailSender.send(messagePreparator);
    }
}
