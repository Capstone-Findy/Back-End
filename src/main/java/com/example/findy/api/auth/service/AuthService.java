package com.example.findy.api.auth.service;

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
import com.example.findy.entity.token.KakaoToken;
import com.example.findy.entity.token.Token;
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
    public void signUp(SignUpReq req) {
        validMailRepository.validCheck(req.email());
        User user = authMapper.toEntity(req);
        userRepository.save(user);
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
    public SignInRes signIn(WebClientResponse res, SignInReq req) {
        User user = userRepository.getByEmail(req.email());
        if(!passwordEncoder.matches(req.password(), user.getPassword())){
            throw new NotFoundUserException();
        }

        Token token = jwtProvider.issueTokens(user, req.rememberMe());

        res.addTokenCookies(res, token);
        return new SignInRes(token.getRefreshToken());
    }

    @Transactional
    public RefreshRes refresh(WebClientResponse res, RefreshReq req) {
        Token existToken = tokenRepository.getByRefreshToken(req.refreshToken());
        User user = userRepository.getById(existToken.getUserId());

        BlackList blackList = BlackList.of(existToken.getAccessToken());
        blackListRepository.save(blackList);

        if(user.getPassword().equals("kakao")) {
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

        if(user.getPassword().equals("kakao")) {
            KakaoToken kakaoToken = kakaoTokenRepository.getByUserId(userId);
            kakaoClient.logout(kakaoToken);
            kakaoTokenRepository.delete(kakaoToken);
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
