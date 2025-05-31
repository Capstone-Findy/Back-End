package com.example.findy._core.client.google;

import com.example.findy._core.client.WebClientFactory;
import com.example.findy._core.client.google.dto.response.GoogleTokenRes;
import com.example.findy._core.client.google.dto.response.GoogleUserInfoRes;
import com.example.findy._core.environment.ExternalsProperties;
import com.example.findy.api.auth.dto.request.GoogleSignUpReq;
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
public class GoogleClient {

    private final ExternalsProperties.GoogleProperties googleProperties;
    private final WebClient tokenWebClient;
    private final WebClient userInfoWebClient;
    private final WebClient revokeWebClient;

    public GoogleClient(ExternalsProperties externals, WebClientFactory webClientFactory) {
        this.googleProperties = externals.google();

        this.tokenWebClient = webClientFactory.createWebClient(
                "https://oauth2.googleapis.com/token",
                RuntimeException::new,
                contentTypeHeader()
        );

        this.userInfoWebClient = webClientFactory.createWebClient(
                "https://www.googleapis.com/oauth2/v2/userinfo",
                RuntimeException::new
        );

        this.revokeWebClient = webClientFactory.createWebClient(
                "https://oauth2.googleapis.com/revoke",
                RuntimeException::new
        );
    }

    public Mono<GoogleTokenRes> getToken(String code) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("code", code);
        formData.add("client_id", googleProperties.clientId());
        formData.add("client_secret", googleProperties.clientSecret());
        formData.add("redirect_uri", googleProperties.redirectUri());
        formData.add("grant_type", "authorization_code");

        return this.tokenWebClient
                .post()
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(GoogleTokenRes.class)
                .publishOn(Schedulers.boundedElastic());
    }

    public Mono<GoogleUserInfoRes> getUserInfo(String accessToken) {
        return this.userInfoWebClient
                .mutate()
                .filter(addAccessToken(accessToken))
                .build()
                .get()
                .retrieve()
                .bodyToMono(GoogleUserInfoRes.class);
    }

    public Mono<GoogleSignUpReq> signUp(String code) {
        return getToken(code)
                .flatMap(tokenRes -> getUserInfo(tokenRes.access_token())
                        .map(userInfo -> GoogleSignUpReq.of(
                                userInfo.name(),
                                userInfo.picture(),
                                userInfo.email(),
                                "google",
                                tokenRes.refresh_token(),
                                tokenRes.access_token()
                        )));
    }

    public Mono<GoogleTokenRes> refreshToken(String refreshToken) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", googleProperties.clientId());
        formData.add("client_secret", googleProperties.clientSecret());
        formData.add("refresh_token", refreshToken);
        formData.add("grant_type", "refresh_token");

        return this.tokenWebClient
                .post()
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(GoogleTokenRes.class)
                .publishOn(Schedulers.boundedElastic());
    }

    public void logout(String accessToken) {
        this.revokeWebClient
                .mutate()
                .filter(addAccessToken(accessToken))
                .build()
                .post()
                .retrieve()
                .bodyToMono(Void.class)
                .subscribe();
    }

    private ExchangeFilterFunction contentTypeHeader() {
        return (request, next) -> {
            ClientRequest filteredRequest = ClientRequest.from(request)
                    .headers(headers -> headers.set("Content-Type", "application/x-www-form-urlencoded"))
                    .build();
            return next.exchange(filteredRequest);
        };
    }

    private ExchangeFilterFunction addAccessToken(String accessToken) {
        return (request, next) -> {
            ClientRequest filteredRequest = ClientRequest.from(request)
                    .headers(headers -> headers.setBearerAuth(accessToken))
                    .build();
            return next.exchange(filteredRequest);
        };
    }
}
