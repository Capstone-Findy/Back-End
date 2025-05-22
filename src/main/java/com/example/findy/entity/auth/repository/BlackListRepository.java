package com.example.findy.entity.auth.repository;

import com.example.findy.entity.auth.entity.BlackList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlackListRepository extends CrudRepository<BlackList, String> {
    Optional<BlackList> findByAccessToken(String accessToken);
}
