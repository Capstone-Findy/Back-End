package com.example.findy.api.user.dto.response;

import com.querydsl.core.annotations.QueryProjection;

public record UserRes(
        String name,
        int money,
        int heart,
        int item1,
        int item2,
        int item3
) {
    @QueryProjection
    public UserRes{}
}
