package com.example.findy.api.user.controller;

import com.example.findy._core.dto.ApiResponse;
import com.example.findy._core.dto.CommonResult;
import com.example.findy.api.auth.dto.request.ValidMailReq;
import com.example.findy.api.user.dto.request.UpdateItem;
import com.example.findy.api.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
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
}
