package com.chat.repository;

import com.chat.model.Game;
import com.chat.model.GamePlayer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

// 用户游戏资料数据访问接口
public interface GamePlayerRepository extends JpaRepository<GamePlayer, Long> {
    // 按用户 ID 查询游戏资料
    Optional<GamePlayer> findByUserId(Long userId);
    // 查询某个游戏中所有可接单的陪玩
    List<GamePlayer> findByIsCompanionTrueAndFavoriteGamesContains(Game game);
    // 统计某个游戏的玩家总数
    long countByFavoriteGamesContains(Game game);
}
