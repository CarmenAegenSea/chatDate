package com.chat.repository;

import com.chat.model.PlayerGame;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// 玩家游戏关联数据访问接口
public interface PlayerGameRepository extends JpaRepository<PlayerGame, Long> {
    // 查询某陪玩账号选择的所有游戏
    List<PlayerGame> findByPlayerId(Long playerId);

    // 删除某陪玩账号选择的所有游戏（重新选择时使用）
    void deleteByPlayerId(Long playerId);

    // 按游戏代码查询所有关联
    List<PlayerGame> findByGameCode(String gameCode);

    // 统计某游戏被多少陪玩选择
    long countByGameCode(String gameCode);
}
