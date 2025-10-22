package com.example.findy.entity.user.repository;

import com.example.findy.api.user.dto.response.QUserRes;
import com.example.findy.api.user.dto.response.UserRes;
import com.example.findy.entity.user.entity.QUser;
import com.example.findy.entity.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public UserRes getUserDetail(User user){
        QUser qUser = QUser.user;
        return jpaQueryFactory
                .select(new QUserRes(
                        qUser.name,
                        qUser.point,
                        qUser.heart,
                        qUser.item1,
                        qUser.item2,
                        qUser.item3
                ))
                .from(qUser)
                .where(qUser.eq(user))
                .fetchFirst();
    }
}
