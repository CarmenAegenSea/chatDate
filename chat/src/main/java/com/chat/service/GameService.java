package com.chat.service;

import com.chat.dto.*;
import com.chat.model.*;
import com.chat.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

// 游戏陪玩业务逻辑：游戏列表、用户资料、联机大厅、详情、陪玩
@Service
public class GameService {

    private final GameRepository gameRepository;
    private final GamePlayerRepository gamePlayerRepository;
    private final GameOnlineRecordRepository gameOnlineRecordRepository;
    private final UserRepository userRepository;

    public GameService(GameRepository gameRepository,
                       GamePlayerRepository gamePlayerRepository,
                       GameOnlineRecordRepository gameOnlineRecordRepository,
                       UserRepository userRepository) {
        this.gameRepository = gameRepository;
        this.gamePlayerRepository = gamePlayerRepository;
        this.gameOnlineRecordRepository = gameOnlineRecordRepository;
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

    // 获取联机大厅数据（各游戏当前在线人数）
    @Transactional(readOnly = true)
    public List<GameResponse> getLobby() {
        return gameRepository.findAll().stream()
                .map(GameResponse::from)
                .collect(Collectors.toList());
    }

    // 获取游戏详情（含过去一小时在线人数折线图数据 + 陪玩列表）
    @Transactional
    public GameDetailResponse getGameDetail(Long gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new EntityNotFoundException("游戏不存在"));

        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        List<GameOnlineRecord> records = gameOnlineRecordRepository
                .findByGameIdAndRecordedAtAfterOrderByRecordedAtAsc(gameId, oneHourAgo);

        // 如果没有记录则生成样本数据
        if (records.isEmpty()) {
            records = generateSampleRecords(game);
        }

        List<GamePlayer> companions = gamePlayerRepository
                .findByIsCompanionTrueAndFavoriteGamesContains(game);

        GameResponse gameResp = GameResponse.from(game);

        List<GameDetailResponse.OnlineRecord> recordDTOs = records.stream().map(r -> {
            GameDetailResponse.OnlineRecord dto = new GameDetailResponse.OnlineRecord();
            dto.setRecordedAt(r.getRecordedAt());
            dto.setPlayerCount(r.getPlayerCount());
            return dto;
        }).collect(Collectors.toList());

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
        detail.setRecords(recordDTOs);
        detail.setCompanions(companionDTOs);
        return detail;
    }

    // 生成过去一小时的样本在线人数记录（首次查看时）
    private List<GameOnlineRecord> generateSampleRecords(Game game) {
        List<GameOnlineRecord> samples = new ArrayList<>();
        Random random = new Random(game.getId());
        int base = 10 + random.nextInt(50);
        LocalDateTime now = LocalDateTime.now();

        for (int i = 12; i >= 0; i--) {
            GameOnlineRecord r = new GameOnlineRecord();
            r.setGame(game);
            r.setRecordedAt(now.minusMinutes(5L * i));
            int variance = random.nextInt(21) - 10;
            r.setPlayerCount(Math.max(1, base + variance));
            samples.add(r);
        }

        gameOnlineRecordRepository.saveAll(samples);
        return samples;
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
