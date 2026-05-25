package com.chat.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// 游戏在线人数记录实体，映射 game_online_records 表
@Entity
@Table(name = "game_online_records")
public class GameOnlineRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 记录唯一 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game; // 所属游戏

    @Column(nullable = false)
    private Integer playerCount; // 当时在线人数

    @Column(nullable = false)
    private LocalDateTime recordedAt; // 记录时间

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Game getGame() { return game; }
    public void setGame(Game game) { this.game = game; }
    public Integer getPlayerCount() { return playerCount; }
    public void setPlayerCount(Integer playerCount) { this.playerCount = playerCount; }
    public LocalDateTime getRecordedAt() { return recordedAt; }
    public void setRecordedAt(LocalDateTime recordedAt) { this.recordedAt = recordedAt; }
}
