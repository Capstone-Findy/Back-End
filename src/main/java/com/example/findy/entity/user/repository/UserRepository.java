package com.example.findy.entity.user.repository;

import com.example.findy.entity.user.entity.User;
import com.example.findy.entity.user.exception.DuplicateEmailException;
import com.example.findy.entity.user.exception.NotFoundUserException;
import jakarta.persistence.LockModeType;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    default void duplicatedByEmail(String email) {
        if(existsByEmail(email)) throw new DuplicateEmailException();
    }

    boolean existsByEmail(String email);

    default User getByEmail(String email) {
        return findByEmail(email).orElseThrow(NotFoundUserException::new);
    }

    Optional<User> findByEmail(String email);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    default @NonNull User getById(@NonNull Long id) {
        return findById(id).orElseThrow(NotFoundUserException::new);
    }

    Optional<User> findById(@NonNull Long id);
}
