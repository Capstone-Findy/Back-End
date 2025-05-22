package com.example.findy.entity.auth.repository;

import com.example.findy.entity.auth.entity.ValidMail;
import com.example.findy.entity.auth.exception.UnauthorizedEmailException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ValidMailRepository extends CrudRepository<ValidMail, String> {
    Optional<ValidMail> findByMail(String email);

    default void validCheck(String email) {
        if (findByMail(email).isEmpty()) {
            throw new UnauthorizedEmailException();
        }
    }
}
