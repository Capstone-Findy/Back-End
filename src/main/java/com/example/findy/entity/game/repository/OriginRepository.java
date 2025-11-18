package com.example.findy.entity.game.repository;

import com.example.findy.entity.game.exception.NotFoundOriginException;
import com.example.findy.entity.game.origin.entity.Origin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OriginRepository extends JpaRepository<Origin, Long> {
    default Origin getById(long id){
        return findById(id).orElseThrow(NotFoundOriginException::new);
    }
}
