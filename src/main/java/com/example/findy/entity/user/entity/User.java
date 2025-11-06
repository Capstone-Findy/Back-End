package com.example.findy.entity.user.entity;

import com.example.findy.api.auth.dto.request.KakaoSignUpReq;
import com.example.findy.api.auth.dto.request.SignUpReq;
import com.example.findy.api.user.dto.request.UpdateItem;
import com.example.findy.entity._common.BaseTimeEntity;
import com.example.findy.entity.file.entity.File;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.max;
import static java.lang.Math.min;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {
    @Column(nullable = false)
    @Comment("유저 이름")
    private String name;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id")
    private File file;

    @Column(nullable = false, unique = true)
    @Comment("유저 이메일")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Comment("로그인 방식")
    private LoginType type;

    @Column(nullable = false)
    @Comment("라이프")
    private int heart;

    @Column(nullable = false)
    @Comment("포인트")
    private int point;

    @Column(nullable = false)
    @Comment("아이템 1")
    private int item1;

    @Column(nullable = false)
    @Comment("아이템 2")
    private int item2;

    @Column(nullable = false)
    @Comment("아이템 3")
    private int item3;

    @JoinColumn(name = "user_id")
    @OneToMany (fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<User> friends;

    private User(String name, String email, LoginType type, File file) {
        this.file = file;
        this.name = name;
        this.email = email;
        this.type = type;
        this.heart = 5;
        this.point = 0;
        this.item1 = 0;
        this.item2 = 0;
        this.item3 = 0;
        this.friends = new ArrayList<>();
    }

    public static User of(KakaoSignUpReq req,  File file){
        return new User(req.name(), req.email(), req.type(), file);
    }

    public static User of(SignUpReq req, LoginType type, File file){
        return new User(req.name(), req.email(), type, file);
    }

    public void updateHeart(int heart) {
        this.heart += heart;
        this.heart = max(0, this.heart);
        this.heart = min(5, this.heart);
    }

    public void updateItem(UpdateItem req) {
        this.item1 += req.item1();
        this.item2 += req.item2();
        this.item3 += req.item3();
    }
}
