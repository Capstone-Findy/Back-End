package com.example.findy.api.game.origin.service;

import com.example.findy.api.auth.dto.JwtAuthentication;
import com.example.findy.api.game.origin.dto.request.ResultReq;
import com.example.findy.api.game.origin.dto.response.ResultRes;
import com.example.findy.entity.game.origin.entity.Country;
import com.example.findy.entity.game.origin.entity.Origin;
import com.example.findy.entity.game.origin.entity.OriginUser;
import com.example.findy.entity.game.repository.OriginRepository;
import com.example.findy.entity.game.repository.OriginUserQueryRepository;
import com.example.findy.entity.game.repository.OriginUserRepository;
import com.example.findy.entity.user.entity.User;
import com.example.findy.entity.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OriginService {
    private final UserRepository userRepository;
    private final OriginRepository originRepository;
    private final OriginUserRepository  originUserRepository;
    private final OriginUserQueryRepository originUserQueryRepository;

    public void updateResult (ResultReq req){
        long userId = JwtAuthentication.getUserId();
        User user = userRepository.getById(userId);
        Origin game = originRepository.getById(req.gameId());
        Optional<OriginUser> originUser = originUserRepository.findByIdUserIdAndIdOriginId(user, game);

        if(originUser.isPresent()){
            originUser.get().update(req);
        }
        else{
            OriginUser result = OriginUser.of(user, game, req);
            originUserRepository.save(result);
        }

        user.updateItem(req);
    }

    public List<ResultRes> getResult (Country country) {
        long userId = JwtAuthentication.getUserId();
        return originUserQueryRepository.getResult(userId, country);
    }
}
