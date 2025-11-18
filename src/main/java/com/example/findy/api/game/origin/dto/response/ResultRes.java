package com.example.findy.api.game.origin.dto.response;

import com.querydsl.core.annotations.QueryProjection;

public record ResultRes(
        long gameId,
        int score
) {
    @QueryProjection
    public ResultRes {}
}
