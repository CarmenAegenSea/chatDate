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

    @Transactional(readOnly = true)
    public List<GameResponse> getLobby(Long userId) {
        Map<String, Long> memberCounts = playerGameRepository.findAll().stream()
                .collect(Collectors.groupingBy(PlayerGame::getGameCode, Collectors.counting()));

        Set<String> interestCodes = new HashSet<>();
        if (userId != null) {
            playerRepository.findByUserId(userId).ifPresent(p ->
                p.getInterests().forEach(opt -> interestCodes.add(opt.getCode()))
            );
        }

        return gameOptionRepository.findAllByOrderByCategoryAscSortOrderAsc().stream()
                .map(g -> {
                    GameResponse r = new GameResponse();
                    r.setCode(g.getCode());
                    r.setName(g.getName());
                    r.setIcon(g.getIcon());
                    r.setCurrentPlayers(memberCounts.getOrDefault(g.getCode(), 0L).intValue());
                    r.setInterested(interestCodes.contains(g.getCode()));
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
        gameResp.setCompanionCount((int) playerGameRepository.countByGameCodeAndIsCompanionTrue(gameCode));

        List<GameDetailResponse.CompanionInfo> companions = getCompanions(gameCode);

        GameDetailResponse detail = new GameDetailResponse();
        detail.setGame(gameResp);
        detail.setCompanions(companions);
        return detail;
    }

    @Transactional(readOnly = true)
    public List<GameDetailResponse.CompanionInfo> getCompanions(String gameCode) {
        List<PlayerGame> playerGames = playerGameRepository.findByGameCodeAndIsCompanionTrue(gameCode);

        return playerGames.stream().map(pg -> {
            Player player = pg.getPlayer();
            GameDetailResponse.CompanionInfo dto = new GameDetailResponse.CompanionInfo();
            dto.setPlayerId(player.getId());
            dto.setUserId(player.getUser().getId());
            dto.setNickname(player.getUser().getNickname());
            dto.setAvatar(player.getUser().getAvatar());
            dto.setBio(player.getBio() != null ? player.getBio() : "");
            dto.setSkillLevel(pg.getSkillLevel() != null ? pg.getSkillLevel() : "");
            dto.setQualifications(pg.getQualifications() != null ? pg.getQualifications() : "");
            dto.setStatus(player.getStatus());
            dto.setHourlyRate(player.getHourlyRate());
            return dto;
        }).collect(Collectors.toList());
    }

    @Transactional
    public void joinChannel(Long userId, String gameCode) {
        Player player = playerRepository.findByUserId(userId).orElseGet(() -> {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("用户不存在"));
            Player p = new Player();
            p.setUser(user);
            return playerRepository.save(p);
        });

        if (playerGameRepository.existsByPlayerIdAndGameCode(player.getId(), gameCode)) {
            throw new RuntimeException("已加入该频道");
        }

        playerGameRepository.save(new PlayerGame(player, gameCode));
    }

    @Transactional
    public void becomeCompanion(Long userId, String gameCode, String qualifications, Boolean acceptedRules) {
        if (acceptedRules == null || !acceptedRules) {
            throw new RuntimeException("请接受陪玩条例");
        }
        if (qualifications == null || qualifications.isBlank()) {
            throw new RuntimeException("请填写资历信息");
        }

        Player player = playerRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("请先加入频道"));

        PlayerGame pg = playerGameRepository.findByPlayerIdAndGameCode(player.getId(), gameCode)
                .orElseThrow(() -> new EntityNotFoundException("请先加入该频道"));

        if (pg.getIsCompanion()) {
            throw new RuntimeException("你已是该频道的陪玩");
        }

        pg.setIsCompanion(true);
        pg.setQualifications(qualifications);
        pg.setAcceptedRules(true);
        playerGameRepository.save(pg);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getChannelStatus(Long userId, String gameCode) {
        Map<String, Object> result = new HashMap<>();
        result.put("gameCode", gameCode);

        Optional<Player> playerOpt = playerRepository.findByUserId(userId);
        if (playerOpt.isEmpty()) {
            result.put("joined", false);
            result.put("isCompanion", false);
            return result;
        }

        Player player = playerOpt.get();
        boolean joined = playerGameRepository.existsByPlayerIdAndGameCode(player.getId(), gameCode);
        boolean isCompanion = playerGameRepository.existsByPlayerIdAndGameCodeAndIsCompanionTrue(player.getId(), gameCode);

        result.put("joined", joined);
        result.put("isCompanion", isCompanion);
        return result;
    }

    @Transactional(readOnly = true)
    public boolean hasInterests(Long userId) {
        return playerRepository.findByUserId(userId)
                .map(p -> !p.getInterests().isEmpty())
                .orElse(false);
    }

    @Transactional
    public void saveInterests(Long userId, List<String> gameCodes) {
        Player player = playerRepository.findByUserId(userId).orElseGet(() -> {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("用户不存在"));
            Player p = new Player();
            p.setUser(user);
            return playerRepository.save(p);
        });
        Set<GameOption> options = new HashSet<>(gameOptionRepository.findByCodeIn(gameCodes));
        player.setInterests(options);
        playerRepository.save(player);
    }
}
