package com.example.findy.entity.game.repository;

import com.example.findy.api.game.origin.dto.response.QResultRes;
import com.example.findy.api.game.origin.dto.response.ResultRes;
import com.example.findy.api.user.dto.response.QUserRes;
import com.example.findy.entity.game.origin.entity.Country;
import com.example.findy.entity.game.origin.entity.QOrigin;
import com.example.findy.entity.game.origin.entity.QOriginUser;
import com.example.findy.entity.user.entity.QUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OriginUserQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public List<ResultRes> getResult(long userId, Country country){
        QUser user = QUser.user;
        QOrigin origin = QOrigin.origin;
        QOriginUser originUser = QOriginUser.originUser;

        return jpaQueryFactory.select(new QResultRes(originUser.id.originId.id, originUser.score))
                .from(originUser)
                .leftJoin(origin).on(originUser.id.originId.eq(origin))
                .leftJoin(user).on(originUser.id.userId.eq(user))
                .where(user.id.eq(userId).and(origin.country.eq(country)))
                .orderBy(origin.id.asc())
                .fetch();
    }
}
