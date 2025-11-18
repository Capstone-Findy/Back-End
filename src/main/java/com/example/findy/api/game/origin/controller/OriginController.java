package com.example.findy.api.game.origin.controller;

import com.example.findy._core.dto.ApiResponse;
import com.example.findy._core.dto.CommonResult;
import com.example.findy._core.dto.ListResult;
import com.example.findy._core.dto.SingleResult;
import com.example.findy.api.game.origin.dto.request.ResultReq;
import com.example.findy.api.game.origin.dto.response.ResultRes;
import com.example.findy.api.game.origin.service.OriginService;
import com.example.findy.api.user.dto.request.UpdateItem;
import com.example.findy.api.user.dto.response.UserRes;
import com.example.findy.api.user.service.UserService;
import com.example.findy.entity.game.origin.entity.Country;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth/origin")
public class OriginController {

    private final OriginService originService;

    @PostMapping("/result")
    public ResponseEntity<CommonResult> updateHeart(
            @Valid @RequestBody ResultReq req
            ) {
        originService.updateResult(req);
        return ApiResponse.ok();
    }

    @GetMapping
    public ResponseEntity<ListResult<ResultRes>> getScore(
            @RequestParam Country country
            ) {
        List<ResultRes> res = originService.getResult(country);
        return ApiResponse.ok(res);
    }

}
