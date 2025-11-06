package com.example.findy.api.auth.mapper;

import com.example.findy.api.auth.dto.request.SignUpReq;
import com.example.findy.entity.file.entity.File;
import com.example.findy.entity.file.repository.FileRepository;
import com.example.findy.entity.user.entity.LoginType;
import com.example.findy.entity.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthMapper {
    private final PasswordEncoder passwordEncoder;
    private final FileRepository fileRepository;

    public User toEntity(SignUpReq req, LoginType type){
        String password = passwordEncoder.encode(req.password());
        File file = File.of(req.file());
        fileRepository.save(file);
        return User.of(req, type, file);
    }
}
