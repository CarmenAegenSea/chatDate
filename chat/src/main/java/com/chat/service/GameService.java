package com.chat.service;

import com.chat.dto.*;
import com.chat.model.*;
import com.chat.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GameService {

    private final GameOptionRepository gameOptionRepository;
    private final PlayerRepository playerRepository;
    private final PlayerGameRepository playerGameRepository;
    private final UserRepository userRepository;

    public GameService(GameOptionRepository gameOptionRepository,
                       PlayerRepository playerRepository,
                       PlayerGameRepository playerGameRepository,
                       UserRepository userRepository) {
        this.gameOptionRepository = gameOptionRepository;
        this.playerRepository = playerRepository;
        this.playerGameRepository = playerGameRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<GameResponse> listGames() {
        return gameOptionRepository.findAllByOrderByCategoryAscSortOrderAsc().stream()
                .map(g -> {
                    GameResponse r = new GameResponse();
                    r.setCode(g.getCode());
                    r.setName(g.getName());
                    r.setIcon(g.getIcon());
                    r.setCurrentPlayers(0);
                    return r;
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PlayerResponse getPlayerProfile(Long userId) {
        Player player = playerRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("游戏资料不存在"));
        List<PlayerGame> games = playerGameRepository.findByPlayerId(player.getId());
        return PlayerResponse.from(player, !games.isEmpty(), games);
    }

    @Transactional(readOnly = true)
    public boolean hasProfile(Long userId) {
        return playerRepository.findByUserId(userId).isPresent();
    }

    @Transactional
    public PlayerResponse setupProfile(Long userId, GameSetupRequest request) {
        if (playerRepository.findByUserId(userId).isPresent()) {
            throw new RuntimeException("陪玩账号已存在");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("用户不存在"));

        Player player = new Player();
        player.setUser(user);
        player.setBio(request.getBio() != null ? request.getBio() : "");
        Player saved = playerRepository.save(player);

        if (request.getGameCodes() != null && !request.getGameCodes().isEmpty()) {
            List<PlayerGame> games = request.getGameCodes().stream()
                    .map(code -> new PlayerGame(saved, code, ""))
                    .collect(Collectors.toList());
            playerGameRepository.saveAll(games);
        }

        boolean gamesSetup = !request.getGameCodes().isEmpty();
        return PlayerResponse.from(saved, gamesSetup);
    }

    @Transactional
    public PlayerResponse updateProfile(Long userId, GameSetupRequest request) {
        Player player = playerRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("陪玩账号不存在"));

        player.setBio(request.getBio() != null ? request.getBio() : "");
        playerRepository.save(player);

        playerGameRepository.deleteByPlayerId(player.getId());
        playerGameRepository.flush();

        if (request.getGameCodes() != null && !request.getGameCodes().isEmpty()) {
            List<PlayerGame> games = request.getGameCodes().stream()
                    .map(code -> new PlayerGame(player, code, ""))
                    .collect(Collectors.toList());
            playerGameRepository.saveAll(games);
        }

        boolean gamesSetup = request.getGameCodes() != null && !request.getGameCodes().isEmpty();
        return PlayerResponse.from(player, gamesSetup);
    }

    @Transactional(readOnly = true)
    public List<GameResponse> getLobby() {
        Map<String, Long> playerCounts = playerGameRepository.findAll().stream()
                .collect(Collectors.groupingBy(PlayerGame::getGameCode, Collectors.counting()));

        return gameOptionRepository.findAllByOrderByCategoryAscSortOrderAsc().stream()
                .map(g -> {
                    GameResponse r = new GameResponse();
                    r.setCode(g.getCode());
                    r.setName(g.getName());
                    r.setIcon(g.getIcon());
                    r.setCurrentPlayers(playerCounts.getOrDefault(g.getCode(), 0L).intValue());
                    return r;
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public GameDetailResponse getGameDetail(String gameCode) {
        GameOption option = gameOptionRepository.findByCode(gameCode)
                .orElseThrow(() -> new EntityNotFoundException("游戏不存在"));

        GameResponse gameResp = new GameResponse();
        gameResp.setCode(option.getCode());
        gameResp.setName(option.getName());
        gameResp.setIcon(option.getIcon());
        gameResp.setCurrentPlayers((int) playerGameRepository.countByGameCode(gameCode));

        List<GameDetailResponse.CompanionInfo> companions = getCompanions(gameCode);

        GameDetailResponse detail = new GameDetailResponse();
        detail.setGame(gameResp);
        detail.setCompanions(companions);
        return detail;
    }

    @Transactional(readOnly = true)
    public List<GameDetailResponse.CompanionInfo> getCompanions(String gameCode) {
        List<PlayerGame> playerGames = playerGameRepository.findByGameCode(gameCode);

        return playerGames.stream().map(pg -> {
            Player player = pg.getPlayer();
            GameDetailResponse.CompanionInfo dto = new GameDetailResponse.CompanionInfo();
            dto.setPlayerId(player.getId());
            dto.setUserId(player.getUser().getId());
            dto.setNickname(player.getUser().getNickname());
            dto.setAvatar(player.getUser().getAvatar());
            dto.setBio(player.getBio() != null ? player.getBio() : "");
            dto.setSkillLevel(pg.getSkillLevel() != null ? pg.getSkillLevel() : "");
            dto.setStatus(player.getStatus());
            dto.setHourlyRate(player.getHourlyRate());
            return dto;
        }).collect(Collectors.toList());
    }

    public static class GameSetupRequest {
        private List<String> gameCodes;
        private String bio;

        public List<String> getGameCodes() { return gameCodes; }
        public void setGameCodes(List<String> gameCodes) { this.gameCodes = gameCodes; }
        public String getBio() { return bio; }
        public void setBio(String bio) { this.bio = bio; }
    }
}
