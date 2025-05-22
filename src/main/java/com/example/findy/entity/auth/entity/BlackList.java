package com.example.findy.entity.auth.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(value = "black_list", timeToLive = 7200)
public class BlackList {
    @Id
    private String accessToken;

    private BlackList(String accessToken) {
        this.accessToken = accessToken;
    }

    public static BlackList of(String accessToken) {
        return new BlackList(accessToken);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        BlackList blackList = (BlackList) o;
        return Objects.equals(accessToken, blackList.accessToken);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(accessToken);
    }

}
