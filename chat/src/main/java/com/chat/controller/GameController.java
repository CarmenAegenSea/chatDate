package com.chat.controller;

import com.chat.dto.*;
import com.chat.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

// 游戏陪玩相关接口：游戏列表、资料设置、联机大厅、详情、陪玩
@RestController
@RequestMapping("/api/game")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    // 获取所有游戏列表
    @GetMapping("/games")
    public ResponseEntity<List<GameResponse>> listGames() {
        return ResponseEntity.ok(gameService.listGames());
    }

    // 获取用户游戏资料（不存在时返回 404）
    @GetMapping("/player")
    public ResponseEntity<?> getPlayerProfile(@RequestParam Long userId) {
        try {
            GamePlayerResponse profile = gameService.getPlayerProfile(userId);
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "游戏资料不存在"));
        }
    }

    // 检查用户是否已有游戏资料
    @GetMapping("/player/check")
    public ResponseEntity<Map<String, Boolean>> checkProfile(@RequestParam Long userId) {
        return ResponseEntity.ok(Map.of("hasProfile", gameService.hasProfile(userId)));
    }

    // 首次设置游戏资料（选择喜欢的游戏、陪玩开关）
    @PostMapping("/player/setup")
    public ResponseEntity<GamePlayerResponse> setupProfile(
            @RequestParam Long userId,
            @RequestBody GameSetupRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(gameService.setupProfile(userId, request));
    }

    // 更新游戏资料（喜欢的游戏、陪玩设置）
    @PutMapping("/player")
    public ResponseEntity<GamePlayerResponse> updateProfile(
            @RequestParam Long userId,
            @RequestBody GameSetupRequest request) {
        return ResponseEntity.ok(gameService.updateProfile(userId, request));
    }

    // 获取联机大厅（各游戏当前频道人数）
    @GetMapping("/lobby")
    public ResponseEntity<List<GameResponse>> getLobby() {
        return ResponseEntity.ok(gameService.getLobby());
    }

    // 获取游戏详情（折线图数据 + 陪玩列表）
    @GetMapping("/{gameId}/detail")
    public ResponseEntity<GameDetailResponse> getGameDetail(@PathVariable Long gameId) {
        return ResponseEntity.ok(gameService.getGameDetail(gameId));
    }

    // 获取某游戏的陪玩人员列表
    @GetMapping("/{gameId}/companions")
    public ResponseEntity<List<GameDetailResponse.CompanionInfo>> getCompanions(@PathVariable Long gameId) {
        return ResponseEntity.ok(gameService.getCompanions(gameId));
    }
}
