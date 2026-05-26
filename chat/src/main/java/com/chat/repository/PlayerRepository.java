package com.chat.repository;

import com.chat.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// 陪玩账号数据访问接口
public interface PlayerRepository extends JpaRepository<Player, Long> {
    // 按用户 ID 查找陪玩账号（用户与陪玩账号为一对一关系）
    Optional<Player> findByUserId(Long userId);
}
