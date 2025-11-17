package com.example.findy.api.user.controller;

import com.example.findy._core.dto.ApiResponse;
import com.example.findy._core.dto.CommonResult;
import com.example.findy._core.dto.SingleResult;
import com.example.findy.api.user.dto.request.UpdateItem;
import com.example.findy.api.user.dto.response.UserRes;
import com.example.findy.api.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth/user")
public class UserController {

    private final UserService userService;

    @PatchMapping("/heart")
    public ResponseEntity<CommonResult> updateHeart(
            @PathVariable int cnt
    ) {
        userService.updateHeart(cnt);
        return ApiResponse.ok();
    }

    @PatchMapping("/item")
    public ResponseEntity<CommonResult> updateItem(
            @RequestBody UpdateItem req
            ) {
        userService.updateItem(req);
        return ApiResponse.ok();
    }

    @GetMapping
    public ResponseEntity<SingleResult<UserRes>> getUser(){
        UserRes res = userService.getUser();
        return ApiResponse.ok(res);
    }

}
