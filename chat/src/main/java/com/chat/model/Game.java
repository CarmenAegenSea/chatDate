package com.chat.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// 游戏实体，映射 games 表
@Entity
@Table(name = "games")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 游戏唯一 ID

    @Column(nullable = false, length = 50)
    private String name; // 游戏名称

    @Column(length = 200)
    private String icon; // 游戏图标（emoji）

    @Column(nullable = false)
    private Integer currentPlayers = 0; // 当前在线人数

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt; // 创建时间

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    public Integer getCurrentPlayers() { return currentPlayers; }
    public void setCurrentPlayers(Integer currentPlayers) { this.currentPlayers = currentPlayers; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
