package com.example.findy.api.user.service;

import com.example.findy.api.auth.dto.JwtAuthentication;
import com.example.findy.entity.user.entity.User;
import com.example.findy.entity.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void updateHeart(int cnt) {
        Long userId = JwtAuthentication.getUserId();
        User user = userRepository.getById(userId);
        user.updateHeart(cnt);
        userRepository.save(user);
    }

}
