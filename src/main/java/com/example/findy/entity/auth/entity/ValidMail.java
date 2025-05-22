package com.example.findy.entity.auth.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(value = "valid_mail_list", timeToLive = 7200)
public class ValidMail {
    @Id
    private String mail;

    private ValidMail(String mail) {
        this.mail = mail;
    }

    public static ValidMail of(String mail) {
        return new ValidMail(mail);
    }
}
