package com.chat.controller;

import com.chat.dto.*;
import com.chat.service.GameService;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/games")
    public ResponseEntity<List<GameResponse>> listGames() {
        return ResponseEntity.ok(gameService.listGames());
    }

    @GetMapping("/player")
    public ResponseEntity<?> getPlayerProfile(@RequestParam Long userId) {
        try {
            PlayerResponse profile = gameService.getPlayerProfile(userId);
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "游戏资料不存在"));
        }
    }

    @GetMapping("/player/check")
    public ResponseEntity<Map<String, Boolean>> checkProfile(@RequestParam Long userId) {
        return ResponseEntity.ok(Map.of("hasProfile", gameService.hasProfile(userId)));
    }

    @GetMapping("/lobby")
    public ResponseEntity<List<GameResponse>> getLobby(@RequestParam(required = false) Long userId) {
        return ResponseEntity.ok(gameService.getLobby(userId));
    }

    @GetMapping("/interests/check")
    public ResponseEntity<Map<String, Boolean>> checkInterests(@RequestParam Long userId) {
        return ResponseEntity.ok(Map.of("hasInterests", gameService.hasInterests(userId)));
    }

    @PostMapping("/interests")
    public ResponseEntity<Void> saveInterests(@RequestParam Long userId, @RequestBody Map<String, List<String>> body) {
        gameService.saveInterests(userId, body.getOrDefault("gameCodes", List.of()));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{gameCode}/detail")
    public ResponseEntity<GameDetailResponse> getGameDetail(@PathVariable String gameCode) {
        return ResponseEntity.ok(gameService.getGameDetail(gameCode));
    }

    @GetMapping("/{gameCode}/companions")
    public ResponseEntity<List<GameDetailResponse.CompanionInfo>> getCompanions(@PathVariable String gameCode) {
        return ResponseEntity.ok(gameService.getCompanions(gameCode));
    }

    @PostMapping("/{gameCode}/join")
    public ResponseEntity<Void> joinChannel(@PathVariable String gameCode, @RequestParam Long userId) {
        gameService.joinChannel(userId, gameCode);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{gameCode}/become-companion")
    public ResponseEntity<?> becomeCompanion(
            @PathVariable String gameCode,
            @RequestParam Long userId,
            @RequestBody Map<String, Object> body) {
        try {
            String qualifications = (String) body.getOrDefault("qualifications", "");
            Boolean acceptedRules = (Boolean) body.getOrDefault("acceptedRules", false);
            gameService.becomeCompanion(userId, gameCode, qualifications, acceptedRules);
            return ResponseEntity.ok(Map.of("message", "已成为陪玩"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/{gameCode}/status")
    public ResponseEntity<Map<String, Object>> getChannelStatus(
            @PathVariable String gameCode, @RequestParam Long userId) {
        return ResponseEntity.ok(gameService.getChannelStatus(userId, gameCode));
    }
}
