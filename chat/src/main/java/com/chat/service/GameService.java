package com.chat.service;

import com.chat.dto.*;
import com.chat.model.*;
import com.chat.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GameService {

    private final GameOptionRepository gameOptionRepository;
    private final ChannelMemberRepository channelMemberRepository;
    private final UserInterestRepository userInterestRepository;
    private final UserRepository userRepository;

    public GameService(GameOptionRepository gameOptionRepository,
                       ChannelMemberRepository channelMemberRepository,
                       UserInterestRepository userInterestRepository,
                       UserRepository userRepository) {
        this.gameOptionRepository = gameOptionRepository;
        this.channelMemberRepository = channelMemberRepository;
        this.userInterestRepository = userInterestRepository;
        this.userRepository = userRepository;
    }

    // 获取游戏列表（简单列表，无在线人数）
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

    // 获取游戏大厅（含各频道在线人数 + 用户兴趣标记）
    @Transactional(readOnly = true)
    public List<GameResponse> getLobby(Long userId) {
        Map<String, Long> memberCounts = channelMemberRepository.findAll().stream()
                .collect(Collectors.groupingBy(ChannelMember::getGameCode, Collectors.counting()));

        Set<String> interestCodes;
        if (userId != null) {
            interestCodes = userInterestRepository.findByUserId(userId).stream()
                    .map(UserInterest::getGameCode)
                    .collect(Collectors.toSet());
        } else {
            interestCodes = Set.of();
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

    // 获取游戏详情（含当前在线人数）
    @Transactional(readOnly = true)
    public GameDetailResponse getGameDetail(String gameCode) {
        GameOption option = gameOptionRepository.findByCode(gameCode)
                .orElseThrow(() -> new RuntimeException("游戏不存在"));

        GameResponse gameResp = new GameResponse();
        gameResp.setCode(option.getCode());
        gameResp.setName(option.getName());
        gameResp.setIcon(option.getIcon());
        gameResp.setCurrentPlayers((int) channelMemberRepository.countByGameCode(gameCode));

        GameDetailResponse detail = new GameDetailResponse();
        detail.setGame(gameResp);
        return detail;
    }

    // 加入频道
    @Transactional
    public void joinChannel(Long userId, String gameCode) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("用户不存在");
        }
        gameOptionRepository.findByCode(gameCode)
                .orElseThrow(() -> new RuntimeException("游戏不存在"));

        if (channelMemberRepository.existsByUserIdAndGameCode(userId, gameCode)) {
            throw new RuntimeException("已加入该频道");
        }

        channelMemberRepository.save(new ChannelMember(userId, gameCode));
    }

    // 查询频道状态（是否已加入）
    @Transactional(readOnly = true)
    public Map<String, Object> getChannelStatus(Long userId, String gameCode) {
        Map<String, Object> result = new HashMap<>();
        result.put("gameCode", gameCode);
        result.put("joined", channelMemberRepository.existsByUserIdAndGameCode(userId, gameCode));
        return result;
    }

    // 判断用户是否设置过兴趣
    @Transactional(readOnly = true)
    public boolean hasInterests(Long userId) {
        return userInterestRepository.existsByUserId(userId);
    }

    // 保存用户兴趣（先删后插）
    @Transactional
    public void saveInterests(Long userId, List<String> gameCodes) {
        userInterestRepository.deleteByUserId(userId);
        List<UserInterest> interests = gameCodes.stream()
                .map(code -> new UserInterest(userId, code))
                .collect(Collectors.toList());
        userInterestRepository.saveAll(interests);
    }
}
