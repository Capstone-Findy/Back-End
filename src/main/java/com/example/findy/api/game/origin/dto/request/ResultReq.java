package com.example.findy.api.game.origin.dto.request;

public record ResultReq(
        long gameId,
        int remainTime,
        int correct,
        int item1,
        int item2,
        int item3
) {
}
