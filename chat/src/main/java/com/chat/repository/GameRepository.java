package com.chat.repository;

import com.chat.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;

// 游戏数据访问接口
public interface GameRepository extends JpaRepository<Game, Long> {
}
