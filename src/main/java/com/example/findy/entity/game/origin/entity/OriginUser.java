package com.example.findy.entity.game.origin.entity;

import com.example.findy.api.game.origin.dto.request.ResultReq;
import com.example.findy.entity.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import static java.lang.Math.max;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OriginUser {
    @EmbeddedId
    private OriginUserId id;

    @Column(nullable = false)
    @Comment("점수")
    private int score;

    @Column(nullable = false)
    @Comment("소요 시간")
    private int time;

    private OriginUser(User user, Origin origin, int score, int time) {
        this.id = new OriginUserId(origin, user);
        this.score = score;
        this.time = time;
    }

    public static OriginUser of(User user, Origin origin, ResultReq req){
        return new OriginUser(user, origin, req.correct()*120 + req.remainTime() * 30, req.remainTime());
    }

    public void update(ResultReq req){
        if(this.score < req.correct()*120 + req.remainTime() * 30){
            this.score = req.correct()*120 + req.remainTime() * 30;
            this.time = req.remainTime();
        }
    }
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
