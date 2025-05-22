package com.example.findy.entity.user.entity;

import com.example.findy.api.auth.dto.request.KakaoSignUpReq;
import com.example.findy.api.auth.dto.request.SignUpReq;
import com.example.findy.entity._common.BaseTimeEntity;
import com.example.findy.entity.file.entity.File;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {
    @Column(nullable = false)
    @Comment("유저 이름")
    private String name;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "file_id")
    private File file;

    @Column(nullable = false, unique = true)
    @Comment("유저 이메일")
    private String email;

    @Column(nullable = false)
    @Comment("암호화 된 패스워드")
    private String password;

    @Column(nullable = false)
    @Comment("관리자 여부")
    private Boolean admin = false;

    @Column(nullable = false)
    @Comment("프로필 공개 여부")
    private Boolean open = true;

    private User(String name, String email, String password, File file) {
        this.file = file;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public static User of(KakaoSignUpReq req, File file){
        return new User(req.name(), req.email(), req.password(), file);
    }

    public static User of(SignUpReq req, String password, File file){
        return new User(req.name(), req.email(), password, file);
    }
}
