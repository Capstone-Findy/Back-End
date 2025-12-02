package com.example.findy.entity.game.origin.entity;

import com.example.findy.api.game.origin.dto.request.ResultReq;
import com.example.findy.entity.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.io.Serializable;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OriginUser {
    @EmbeddedId
    private OriginUserId id;

    @MapsId("originId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "origin_id", insertable = false, updatable = false)
    private Origin origin;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @Column(nullable = false)
    @Comment("점수")
    private int score;

    @Column(nullable = false)
    @Comment("소요 시간")
    private int time;

    private OriginUser(User user, Origin origin, int score, int time) {
        this.id = new OriginUserId(origin, user);
        this.origin = origin;
        this.user = user;
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

    @Embeddable
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class OriginUserId implements Serializable {

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "origin_id", nullable = false)
        private Origin originId;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id", nullable = false)
        private User userId;
    }
}
