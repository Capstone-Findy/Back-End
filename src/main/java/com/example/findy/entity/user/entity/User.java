package com.example.findy.entity.user.entity;

import com.example.findy.api.auth.dto.request.KakaoSignUpReq;
import com.example.findy.api.auth.dto.request.GoogleSignUpReq;
import com.example.findy.api.auth.dto.request.SignUpReq;
import com.example.findy.entity.file.entity.File;
import com.findy.processor.RestDocs;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "file_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private File file;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, length = 20)
    private String name;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoginType type;

    @Column(nullable = false)
    private int point;

    @Column(nullable = false)
    private Long item1;

    @Column(nullable = false)
    private Long item2;

    @Column(nullable = false)
    private Long item3;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;



    public static User of(KakaoSignUpReq req, File file) {
        return User.builder()
                .email(req.email())
                .name(req.name())
                .type(LoginType.KAKAO)
                .file(file)
                .point(0)
                .item1(0L)
                .item2(0L)
                .item3(0L)
                .build();
    }

    public static User of(GoogleSignUpReq req, File file) {
        return User.builder()
                .email(req.email())
                .name(req.name())
                .type(LoginType.GOOGLE)
                .file(file)
                .point(0)
                .item1(0L)
                .item2(0L)
                .item3(0L)
                .build();
    }

    public enum LoginType {
        @RestDocs("구글 계정")
        GOOGLE,
        @RestDocs("카카오 계정")
        KAKAO
    }

}
