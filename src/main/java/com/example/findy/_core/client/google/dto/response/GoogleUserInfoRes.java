package com.example.findy._core.client.google.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GoogleUserInfoRes(
        String email,

        @JsonProperty("name")
        String name,

        @JsonProperty("picture")
        String picture
) {}