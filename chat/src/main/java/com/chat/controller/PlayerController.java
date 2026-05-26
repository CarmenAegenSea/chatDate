package com.chat.controller;

import com.chat.dto.GameSelectionRequest;
import com.chat.dto.PlayerCreateRequest;
import com.chat.dto.PlayerResponse;
import com.chat.service.PlayerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 陪玩相关 REST API
// 提供陪玩账号的查询、创建、游戏偏好保存等接口
@RestController
@RequestMapping("/api/player")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    // 按用户 ID 获取陪玩账号
    @GetMapping("/by-user/{userId}")
    public ResponseEntity<PlayerResponse> getByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(playerService.getByUserId(userId));
    }

    // 创建陪玩账号
    @PostMapping
    public ResponseEntity<PlayerResponse> create(@RequestParam Long userId,
                                                  @Valid @RequestBody PlayerCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(playerService.create(userId, request));
    }

    // 获取或自动创建陪玩账号（首次进入模块时调用）
    @PostMapping("/get-or-create")
    public ResponseEntity<PlayerResponse> getOrCreate(@RequestParam Long userId) {
        return ResponseEntity.ok(playerService.getOrCreate(userId));
    }

    // 保存玩家选择的游戏及对应水平
    @PutMapping("/{playerId}/games")
    public ResponseEntity<Void> saveGames(@PathVariable Long playerId,
                                          @Valid @RequestBody GameSelectionRequest request) {
        playerService.saveGames(playerId, request);
        return ResponseEntity.noContent().build();
    }

    // 获取玩家已选的游戏列表
    @GetMapping("/{playerId}/games")
    public ResponseEntity<List<GameSelectionRequest.GameItem>> getGames(@PathVariable Long playerId) {
        return ResponseEntity.ok(playerService.getGames(playerId));
    }
}
