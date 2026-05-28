package com.chat.repository;

import com.chat.model.PlayerGame;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

// 玩家游戏关联数据访问接口
public interface PlayerGameRepository extends JpaRepository<PlayerGame, Long> {

    List<PlayerGame> findByPlayerId(Long playerId);

    void deleteByPlayerId(Long playerId);

    List<PlayerGame> findByGameCode(String gameCode);

    long countByGameCode(String gameCode);

    List<PlayerGame> findByGameCodeAndIsCompanionTrue(String gameCode);

    long countByGameCodeAndIsCompanionTrue(String gameCode);

    boolean existsByPlayerIdAndGameCode(Long playerId, String gameCode);

    boolean existsByPlayerIdAndGameCodeAndIsCompanionTrue(Long playerId, String gameCode);

    Optional<PlayerGame> findByPlayerIdAndGameCode(Long playerId, String gameCode);
}
