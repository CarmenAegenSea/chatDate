package com.chat.service;

import com.chat.dto.*;
import com.chat.model.*;
import com.chat.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

// 游戏陪玩业务逻辑：游戏列表、用户资料、联机大厅、详情、陪玩
@Service
public class GameService {

    private final GameRepository gameRepository;
    private final GamePlayerRepository gamePlayerRepository;
    private final UserRepository userRepository;

    public GameService(GameRepository gameRepository,
                       GamePlayerRepository gamePlayerRepository,
                       UserRepository userRepository) {
        this.gameRepository = gameRepository;
        this.gamePlayerRepository = gamePlayerRepository;
        this.userRepository = userRepository;
    }

    // 获取所有游戏列表
    @Transactional(readOnly = true)
    public List<GameResponse> listGames() {
        return gameRepository.findAll().stream()
                .map(GameResponse::from)
                .collect(Collectors.toList());
    }

    // 获取用户的游戏资料
    @Transactional(readOnly = true)
    public GamePlayerResponse getPlayerProfile(Long userId) {
        GamePlayer gp = gamePlayerRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("游戏资料不存在"));
        return GamePlayerResponse.from(gp);
    }

    // 检查用户是否已有游戏资料
    @Transactional(readOnly = true)
    public boolean hasProfile(Long userId) {
        return gamePlayerRepository.findByUserId(userId).isPresent();
    }

    // 首次设置游戏资料（选择喜欢的游戏 + 陪玩设置）
    @Transactional
    public GamePlayerResponse setupProfile(Long userId, GameSetupRequest request) {
        if (gamePlayerRepository.findByUserId(userId).isPresent()) {
            throw new RuntimeException("游戏资料已存在");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("用户不存在"));

        Set<Game> favoriteGames = new HashSet<>(gameRepository.findAllById(
                request.getFavoriteGameIds() != null ? request.getFavoriteGameIds() : List.of()));

        GamePlayer gp = new GamePlayer();
        gp.setUser(user);
        gp.setFavoriteGames(favoriteGames);
        gp.setIsCompanion(request.getIsCompanion() != null && request.getIsCompanion());
        gp.setBio(request.getBio() != null ? request.getBio() : "");
        return GamePlayerResponse.from(gamePlayerRepository.save(gp));
    }

    // 更新游戏资料（喜欢的游戏、陪玩设置）
    @Transactional
    public GamePlayerResponse updateProfile(Long userId, GameSetupRequest request) {
        GamePlayer gp = gamePlayerRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("游戏资料不存在"));

        Set<Game> favoriteGames = new HashSet<>(gameRepository.findAllById(
                request.getFavoriteGameIds() != null ? request.getFavoriteGameIds() : List.of()));

        gp.setFavoriteGames(favoriteGames);
        gp.setIsCompanion(request.getIsCompanion() != null && request.getIsCompanion());
        gp.setBio(request.getBio() != null ? request.getBio() : "");
        return GamePlayerResponse.from(gamePlayerRepository.save(gp));
    }

    // 获取联机大厅数据（各游戏当前频道人数）
    @Transactional(readOnly = true)
    public List<GameResponse> getLobby() {
        return gameRepository.findAll().stream()
                .map(game -> {
                    GameResponse r = GameResponse.from(game);
                    r.setCurrentPlayers((int) gamePlayerRepository.countByFavoriteGamesContains(game));
                    return r;
                })
                .collect(Collectors.toList());
    }

    // 获取游戏详情（含陪玩列表）
    @Transactional
    public GameDetailResponse getGameDetail(Long gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new EntityNotFoundException("游戏不存在"));

        List<GamePlayer> companions = gamePlayerRepository
                .findByIsCompanionTrueAndFavoriteGamesContains(game);

        GameResponse gameResp = GameResponse.from(game);
        gameResp.setCurrentPlayers((int) gamePlayerRepository.countByFavoriteGamesContains(game));

        List<GameDetailResponse.CompanionInfo> companionDTOs = companions.stream().map(c -> {
            GameDetailResponse.CompanionInfo dto = new GameDetailResponse.CompanionInfo();
            dto.setUserId(c.getUserId());
            dto.setNickname(c.getUser().getNickname());
            dto.setAvatar(c.getUser().getAvatar());
            dto.setBio(c.getBio() != null ? c.getBio() : "");
            return dto;
        }).collect(Collectors.toList());

        GameDetailResponse detail = new GameDetailResponse();
        detail.setGame(gameResp);
        detail.setCompanions(companionDTOs);
        return detail;
    }

    // 获取某游戏的所有可接单陪玩
    @Transactional(readOnly = true)
    public List<GameDetailResponse.CompanionInfo> getCompanions(Long gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new EntityNotFoundException("游戏不存在"));
        return gamePlayerRepository.findByIsCompanionTrueAndFavoriteGamesContains(game).stream().map(c -> {
            GameDetailResponse.CompanionInfo dto = new GameDetailResponse.CompanionInfo();
            dto.setUserId(c.getUserId());
            dto.setNickname(c.getUser().getNickname());
            dto.setAvatar(c.getUser().getAvatar());
            dto.setBio(c.getBio() != null ? c.getBio() : "");
            return dto;
        }).collect(Collectors.toList());
    }
}
