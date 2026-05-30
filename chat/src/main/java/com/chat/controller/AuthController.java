package com.chat.controller;

import com.chat.dto.NicknameUpdateRequest;
import com.chat.dto.OAuthLoginRequest;
import com.chat.dto.PasswordLoginRequest;
import com.chat.dto.UserResponse;
import com.chat.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// 认证相关接口：注册、登录、修改昵称
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // 第三方登录（微信/QQ），首次登录自动注册
    @PostMapping("/oauth")
    public ResponseEntity<UserResponse> oauth(@Valid @RequestBody OAuthLoginRequest request) {
        return ResponseEntity.ok(userService.oauthLogin(request));
    }

    // 手机号+密码注册
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody PasswordLoginRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(request));
    }

    // 手机号+密码登录，仅已注册用户可登录
    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@Valid @RequestBody PasswordLoginRequest request) {
        return ResponseEntity.ok(userService.loginByPassword(request));
    }

    // 修改昵称
    @PostMapping("/nickname")
    public ResponseEntity<UserResponse> updateNickname(
            @RequestParam("userId") Long userId,
            @Valid @RequestBody NicknameUpdateRequest request) {
        return ResponseEntity.ok(userService.updateNickname(userId, request));
    }

}
