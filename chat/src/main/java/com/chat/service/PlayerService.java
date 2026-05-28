package com.chat.service;

import com.chat.dto.GameSelectionRequest;
import com.chat.dto.PlayerCreateRequest;
import com.chat.dto.PlayerResponse;
import com.chat.model.GameOption;
import com.chat.model.Player;
import com.chat.model.PlayerGame;
import com.chat.model.User;
import com.chat.repository.GameOptionRepository;
import com.chat.repository.PlayerGameRepository;
import com.chat.repository.PlayerRepository;
import com.chat.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// 陪玩业务逻辑层
// 提供陪玩账号的创建、查询，以及游戏偏好保存
@Service
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final UserRepository userRepository;
    private final PlayerGameRepository playerGameRepository;
    private final GameOptionRepository gameOptionRepository;

    public PlayerService(PlayerRepository playerRepository,
                         UserRepository userRepository,
                         PlayerGameRepository playerGameRepository,
                         GameOptionRepository gameOptionRepository) {
        this.playerRepository = playerRepository;
        this.userRepository = userRepository;
        this.playerGameRepository = playerGameRepository;
        this.gameOptionRepository = gameOptionRepository;
    }

    // 根据用户 ID 获取陪玩账号，含游戏设置状态
    @Transactional(readOnly = true)
    public PlayerResponse getByUserId(Long userId) {
        Player player = playerRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("陪玩账号不存在"));
        boolean gamesSetup = !playerGameRepository.findByPlayerId(player.getId()).isEmpty();
        return PlayerResponse.from(player, gamesSetup);
    }

    // 创建新的陪玩账号
    @Transactional
    public PlayerResponse create(Long userId, PlayerCreateRequest request) {
        if (playerRepository.findByUserId(userId).isPresent()) {
            throw new RuntimeException("陪玩账号已存在");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("用户不存在"));
        Player player = new Player();
        player.setUser(user);
        player.setBio(request.getBio());
        player.setHourlyRate(request.getHourlyRate());
        return PlayerResponse.from(playerRepository.save(player), false);
    }

    // 获取或自动创建陪玩账号（首次进入模块时使用）
    @Transactional
    public PlayerResponse getOrCreate(Long userId) {
        Player player = playerRepository.findByUserId(userId).orElse(null);
        if (player != null) {
            boolean gamesSetup = !playerGameRepository.findByPlayerId(player.getId()).isEmpty();
            return PlayerResponse.from(player, gamesSetup);
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("用户不存在"));
        player = new Player();
        player.setUser(user);
        player = playerRepository.save(player);
        return PlayerResponse.from(player, false);
    }

    // 获取所有陪玩账号列表（含游戏信息）
    @Transactional(readOnly = true)
    public List<PlayerResponse> listAll() {
        Map<String, String> gameNameMap = gameOptionRepository.findAll().stream()
                .collect(Collectors.toMap(GameOption::getCode, GameOption::getName));
        return playerRepository.findAll().stream().map(player -> {
            List<PlayerGame> games = playerGameRepository.findByPlayerId(player.getId());
            boolean gamesSetup = !games.isEmpty();
            PlayerResponse r = PlayerResponse.from(player, gamesSetup, games);
            r.getGames().forEach(g -> g.setGameName(gameNameMap.getOrDefault(g.getGameCode(), g.getGameCode())));
            return r;
        }).toList();
    }

    // 保存玩家选择的游戏（先删后插，flush 防唯一约束冲突）
    @Transactional
    public void saveGames(Long playerId, GameSelectionRequest request) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new EntityNotFoundException("陪玩账号不存在"));
        playerGameRepository.deleteByPlayerId(playerId);
        playerGameRepository.flush();
        List<PlayerGame> games = request.getGames().stream()
                .map(item -> {
                    PlayerGame pg = new PlayerGame(player, item.getGameCode());
                    pg.setSkillLevel(item.getSkillLevel());
                    return pg;
                })
                .collect(Collectors.toList());
        playerGameRepository.saveAll(games);
    }

    // 获取玩家已选的游戏列表
    @Transactional(readOnly = true)
    public List<GameSelectionRequest.GameItem> getGames(Long playerId) {
        return playerGameRepository.findByPlayerId(playerId).stream()
                .map(pg -> {
                    GameSelectionRequest.GameItem item = new GameSelectionRequest.GameItem();
                    item.setGameCode(pg.getGameCode());
                    item.setSkillLevel(pg.getSkillLevel());
                    return item;
                })
                .toList();
    }
}
