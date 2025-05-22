package com.example.findy.entity.file.entity;

import com.example.findy._core.client.kakao.dto.response.KakaoAccountInfoRes;
import com.example.findy.entity._common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class File extends BaseTimeEntity {
    @Comment("파일 URL")
    @Column(nullable = false)
    String url;

    private File(String url) {
        this.url = url;
    }

    public static File of(String profile) {
        return new File(profile);
    }
}
