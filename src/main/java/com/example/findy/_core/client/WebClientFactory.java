package com.example.findy._core.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Slf4j
@Component
public class WebClientFactory {

    private final WebClient.Builder webClientBuilder;

    public WebClientFactory(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public WebClient createWebClient(
            String baseUrl,
            Function<String, ? extends Throwable> exceptionSupplier,
            ExchangeFilterFunction... additionalFilters
    ) {
        WebClient.Builder builder = webClientBuilder
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .filters(filters -> {
                    if (!filters.isEmpty()) {
                        filters.clear();
                    }
                })
                .filter(createErrorHandlingFilter(exceptionSupplier));

        for (ExchangeFilterFunction filter : additionalFilters) {
            builder.filter(filter);
        }

        return builder.build();
    }

    private ExchangeFilterFunction createErrorHandlingFilter(Function<String, ? extends Throwable> exceptionSupplier) {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            if (clientResponse.statusCode().isError()) {
                return handleErrorResponse(clientResponse, exceptionSupplier);
            }
            return Mono.just(clientResponse);
        });
    }

    private Mono<ClientResponse> handleErrorResponse(
            ClientResponse clientResponse,
            Function<String, ? extends Throwable> exceptionSupplier
    ) {
        return clientResponse.bodyToMono(String.class).flatMap(errorBody -> {
            if (clientResponse.statusCode().is4xxClientError()) {
                log.error("4xx Error Response: {}", errorBody);
            } else if (clientResponse.statusCode().is5xxServerError()) {
                log.error("5xx Error Response: {}", errorBody);
            }
            return Mono.error(exceptionSupplier.apply(errorBody));
        });
    }
}