package com.example.findy.entity.game.origin.entity;

import com.example.findy.entity._common.IdentifiableEntity;
import com.example.findy.entity.file.entity.File;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Origin extends IdentifiableEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id")
    private File file;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Comment("국가")
    private Country country;
}
