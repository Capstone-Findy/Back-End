package com.example.findy._core.client.kakao;

import com.example.findy._core.client.WebClientFactory;
import com.example.findy._core.client.kakao.dto.request.KakaoCodeReq;
import com.example.findy._core.client.kakao.dto.response.KakaoAccountInfoRes;
import com.example.findy._core.client.kakao.dto.response.KakaoTokenRes;
import com.example.findy._core.client.kakao.exception.KakaoClientException;
import com.example.findy._core.environment.ExternalsProperties;
import com.example.findy.api.auth.dto.request.KakaoSignUpReq;
import com.example.findy.entity.token.KakaoToken;
import com.example.findy.entity.user.entity.LoginType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Component
public class KakaoClient {
    private final ExternalsProperties.KakaoProperties kakaoProperties;
    private final WebClient logoutClient;
    private final WebClient tokenWebClient;
    private final WebClient webClient;

    public KakaoClient(
            ExternalsProperties externalsProperties,
            WebClientFactory webClientFactory
    ) {

        this.kakaoProperties = externalsProperties.kakao();
        final String BASE_URL = "https://kapi.kakao.com/v2/user/me";
        this.webClient = webClientFactory.createWebClient(
                BASE_URL,
                KakaoClientException::new
        );
        final String TOKEN_URL = "https://kauth.kakao.com/oauth/token";
        this.tokenWebClient = webClientFactory.createWebClient(
                TOKEN_URL,
                KakaoClientException::new,
                this.tokenHeader()
        );
        final String LOGOUT_URL = "https://kapi.kakao.com/v1/user/logout";
        this.logoutClient = webClientFactory.createWebClient(
                LOGOUT_URL,
                KakaoClientException::new
        );
    }

    public void logout(KakaoToken token){
        this.logoutClient.mutate()
                .filter(addAccessToken(token.getAccessToken()))
                .build()
                .post()
                .retrieve()
                .bodyToMono(Void.class)
                .subscribe();
    }

    public Mono<KakaoSignUpReq> signUp(KakaoCodeReq req) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", kakaoProperties.type());
        formData.add("client_id", kakaoProperties.clientId());
        formData.add("redirect_uri", "http://localhost:8080/kakao/sign-up");
        formData.add("code", req.code());
        formData.add("client_secret", kakaoProperties.clientSecret());
        return this.tokenWebClient
                .post()
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(KakaoTokenRes.class)
                .publishOn(Schedulers.boundedElastic())
                .map(response -> {
                    KakaoAccountInfoRes kakaoAccountInfoRes = getAccount(response).block();
                    return KakaoSignUpReq.of(kakaoAccountInfoRes.kakao_account().profile().nickname(),
                            kakaoAccountInfoRes.kakao_account().profile().profile_image_url(),
                            kakaoAccountInfoRes.kakao_account().email(),
                            LoginType.KAKAO,
                            response.refresh_token(),
                            response.access_token());
                });
    }

    public Mono<KakaoTokenRes> refreshToken(String refreshToken) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "refresh_token");
        formData.add("client_id", kakaoProperties.clientId());
        formData.add("refresh_token", refreshToken);
        formData.add("client_secret", kakaoProperties.clientSecret());

        return this.tokenWebClient
                .post()
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(KakaoTokenRes.class)
                .publishOn(Schedulers.boundedElastic());
    }

    private Mono<KakaoAccountInfoRes> getAccount(KakaoTokenRes req) {
        return this.webClient.mutate()
                .filter(addAccessToken(req.access_token()))
                .build()
                .post()
                .retrieve()
                .bodyToMono(KakaoAccountInfoRes.class);
    }

    private ExchangeFilterFunction tokenHeader() {
        return ((request, next) -> {
            ClientRequest filteredRequest = ClientRequest.from(request).headers(headers -> {
                headers.set("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            }).build();
            return next.exchange(filteredRequest);
        });
    }

    private ExchangeFilterFunction addAccessToken(String accessToken) {
        System.out.println(accessToken);
        return ((request, next) -> {
            ClientRequest filteredRequest = ClientRequest.from(request).headers(headers -> {
                headers.set("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
                headers.set("Authorization", "Bearer " + accessToken);
            }).build();
            return next.exchange(filteredRequest);
        });
    }
}
