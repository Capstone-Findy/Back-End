package com.example.findy._core.client.google.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GoogleTokenRes(
        @JsonProperty("access_token")
        String access_token,

        @JsonProperty("expires_in")
        int expires_in,

        @JsonProperty("refresh_token")
        String refresh_token,

        @JsonProperty("scope")
        String scope,

        @JsonProperty("token_type")
        String token_type,

        @JsonProperty("id_token")
        String id_token
) {}
