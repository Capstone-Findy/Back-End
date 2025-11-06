package com.example.findy.entity.game.origin.entity;

import com.example.findy.entity.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

public class OriginUser {
    @EmbeddedId
    private OriginUserId id;

    @Column(nullable = false)
    @Comment("점수")
    private int score;

    @Column(nullable = false)
    @Comment("소요 시간")
    private int time;

    @Getter
    @Embeddable
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @EqualsAndHashCode
    public class OriginUserId {
        @JoinColumn(name = "origin_id", nullable = false)
        @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        private Origin originId;

        @JoinColumn(name = "user_id", nullable = false)
        @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        private User userId;
    }
}
