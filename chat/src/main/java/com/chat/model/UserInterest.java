package com.chat.model;

import jakarta.persistence.*;

// 用户游戏兴趣实体，映射 user_interests 表
// 记录用户感兴趣的游戏频道，用于大厅个性化展示
@Entity
@Table(name = "user_interests",
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "game_code"}))
public class UserInterest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "game_code", nullable = false, length = 20)
    private String gameCode;

    public UserInterest() {}

    public UserInterest(Long userId, String gameCode) {
        this.userId = userId;
        this.gameCode = gameCode;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getGameCode() { return gameCode; }
    public void setGameCode(String gameCode) { this.gameCode = gameCode; }
}
