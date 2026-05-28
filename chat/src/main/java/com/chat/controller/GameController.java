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

    @PostMapping("/player/setup")
    public ResponseEntity<PlayerResponse> setupProfile(
            @RequestParam Long userId,
            @RequestBody GameService.GameSetupRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(gameService.setupProfile(userId, request));
    }

    @PutMapping("/player")
    public ResponseEntity<PlayerResponse> updateProfile(
            @RequestParam Long userId,
            @RequestBody GameService.GameSetupRequest request) {
        return ResponseEntity.ok(gameService.updateProfile(userId, request));
    }

    @GetMapping("/lobby")
    public ResponseEntity<List<GameResponse>> getLobby() {
        return ResponseEntity.ok(gameService.getLobby());
    }

    @GetMapping("/{gameCode}/detail")
    public ResponseEntity<GameDetailResponse> getGameDetail(@PathVariable String gameCode) {
        return ResponseEntity.ok(gameService.getGameDetail(gameCode));
    }

    @GetMapping("/{gameCode}/companions")
    public ResponseEntity<List<GameDetailResponse.CompanionInfo>> getCompanions(@PathVariable String gameCode) {
        return ResponseEntity.ok(gameService.getCompanions(gameCode));
    }
}
