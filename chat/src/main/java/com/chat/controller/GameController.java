package com.chat.controller;

import com.chat.dto.*;
import com.chat.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/game")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    // 获取游戏列表
    @GetMapping("/games")
    public ResponseEntity<List<GameResponse>> listGames() {
        return ResponseEntity.ok(gameService.listGames());
    }

    // 获取游戏大厅（含各频道在线人数 + 用户兴趣标记）
    @GetMapping("/lobby")
    public ResponseEntity<List<GameResponse>> getLobby(@RequestParam(required = false) Long userId) {
        return ResponseEntity.ok(gameService.getLobby(userId));
    }

    // 获取游戏详情
    @GetMapping("/{gameCode}/detail")
    public ResponseEntity<GameDetailResponse> getGameDetail(@PathVariable String gameCode) {
        return ResponseEntity.ok(gameService.getGameDetail(gameCode));
    }

    // 加入频道
    @PostMapping("/{gameCode}/join")
    public ResponseEntity<Map<String, String>> joinChannel(
            @PathVariable String gameCode, @RequestParam Long userId) {
        try {
            gameService.joinChannel(userId, gameCode);
            return ResponseEntity.ok(Map.of("message", "已加入频道"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    // 查询频道状态
    @GetMapping("/{gameCode}/status")
    public ResponseEntity<Map<String, Object>> getChannelStatus(
            @PathVariable String gameCode, @RequestParam Long userId) {
        return ResponseEntity.ok(gameService.getChannelStatus(userId, gameCode));
    }

    // 检查用户是否设置过兴趣
    @GetMapping("/interests/check")
    public ResponseEntity<Map<String, Boolean>> checkInterests(@RequestParam Long userId) {
        return ResponseEntity.ok(Map.of("hasInterests", gameService.hasInterests(userId)));
    }

    // 保存用户游戏兴趣
    @PostMapping("/interests")
    public ResponseEntity<Void> saveInterests(
            @RequestParam Long userId,
            @RequestBody Map<String, List<String>> body) {
        gameService.saveInterests(userId, body.getOrDefault("gameCodes", List.of()));
        return ResponseEntity.ok().build();
    }
}
