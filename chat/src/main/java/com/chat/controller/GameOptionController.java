package com.chat.controller;

import com.chat.dto.GameOptionResponse;
import com.chat.service.GameOptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// 游戏选项 REST API
// 供前端获取游戏列表，用于选项卡展示和游戏选择
@RestController
@RequestMapping("/api/game-options")
public class GameOptionController {

    private final GameOptionService gameOptionService;

    public GameOptionController(GameOptionService gameOptionService) {
        this.gameOptionService = gameOptionService;
    }

    // 获取所有游戏选项
    @GetMapping
    public ResponseEntity<List<GameOptionResponse>> list() {
        return ResponseEntity.ok(gameOptionService.getAll());
    }
}
