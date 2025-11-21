package com.example.findy.entity.game.repository;

import com.example.findy.entity.game.exception.NotFoundOriginException;
import com.example.findy.entity.game.origin.entity.Origin;
import com.example.findy.entity.game.origin.entity.OriginUser;
import com.example.findy.entity.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OriginUserRepository extends JpaRepository<OriginUser, Long> {
    Optional<OriginUser> findByIdUserIdAndIdOriginId(User user, Origin origin);
}
